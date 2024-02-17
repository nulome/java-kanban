package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    public HistoryManager inHistory = Managers.getDefaultHistory();
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    TasksComparator tasksComparator = new TasksComparator();
    protected TreeSet<Task> sortPrioritizedTasks = new TreeSet<>(tasksComparator);
    private final HashSet<LocalDateTime> checkTableTimeOfPrioritized = new HashSet<>();
    static final long TIME_CONSTANT = 15;


    private int newId = 0;

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HashMap<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public HashMap<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public void setInHistory(HistoryManager inHistory) {
        this.inHistory = inHistory;
    }

    public void setTaskMap(HashMap<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public void setEpicMap(HashMap<Integer, Epic> epicMap) {
        this.epicMap = epicMap;
    }

    public void setSubtaskMap(HashMap<Integer, Subtask> subtaskMap) {
        this.subtaskMap = subtaskMap;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortPrioritizedTasks);
    }

    @Override
    public void clearTaskMap() {
        for (Task task : taskMap.values()) {
            inHistory.removeHistory(task.getUniqueId());
        }
        taskMap.clear();
        updateSetPrioritized();
    }

    @Override
    public void clearEpicMap() {
        for (Task task : epicMap.values()) {
            inHistory.removeHistory(task.getUniqueId());
        }
        for (Task task : subtaskMap.values()) {
            inHistory.removeHistory(task.getUniqueId());
        }
        epicMap.clear();
        subtaskMap.clear();
        updateSetPrioritized();
    }

    @Override
    public void clearSubtaskMap() {
        for (Epic epic : epicMap.values()) {
            epic.setListSubtaskId(new ArrayList<>());
            updateStatusEpic(epic);
            updateTimeEpic(epic);
        }
        for (Task task : subtaskMap.values()) {
            inHistory.removeHistory(task.getUniqueId());
        }
        subtaskMap.clear();
        updateSetPrioritized();
    }

    @Override
    public Task getTaskById(Integer key) {
        inHistory.addHistory(taskMap.get(key));
        return taskMap.get(key);
    }

    @Override
    public Epic getEpicById(Integer key) {
        inHistory.addHistory(epicMap.get(key));
        return epicMap.get(key);
    }

    @Override
    public Subtask getSubtaskById(Integer key) {
        inHistory.addHistory(subtaskMap.get(key));
        return subtaskMap.get(key);
    }

    @Override
    public Integer creationTask(Task task) {
        if (validationOfTheIntersection(task)) {
            this.newId++;
            task.setUniqueId(newId);
            taskMap.put(task.getUniqueId(), task);
            updateSetPrioritized();
            return newId;
        } else {
            throw new ManagerSaveException("Время для выполнения задачи уже занято.");
        }
    }

    @Override
    public Integer creationEpic(Epic epic) {
        this.newId++;
        epic.setUniqueId(newId);
        epicMap.put(epic.getUniqueId(), epic);
        return newId;
    }

    @Override
    public Integer creationSubtask(Subtask subtask) {
        if (validationOfTheIntersection(subtask)) {
            this.newId++;
            subtask.setUniqueId(newId);
            subtaskMap.put(subtask.getUniqueId(), subtask);

            Epic updateEpic = epicMap.get(subtask.getEpicId());
            ArrayList<Integer> subtaskIds = updateEpic.getListSubtaskId();
            subtaskIds.add(subtask.getUniqueId());
            updateEpic.setListSubtaskId(subtaskIds);

            updateStatusEpic(updateEpic);
            updateTimeEpic(updateEpic);
            updateSetPrioritized();
            return subtask.getUniqueId();
        } else {
            throw new ManagerSaveException("Время для выполнения задачи уже занято.");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (validationOfTheIntersection(task)) {
            int id = task.getUniqueId();
            taskMap.put(id, task);
            updateSetPrioritized();
        } else {
            throw new ManagerSaveException("Время для выполнения задачи уже занято.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getUniqueId();
        epic.setListSubtaskId(epicMap.get(id).getListSubtaskId());
        epicMap.put(id, epic);
        updateStatusEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (validationOfTheIntersection(subtask)) {
            subtaskMap.put(subtask.getUniqueId(), subtask);
            updateStatusEpic(epicMap.get(subtask.getEpicId()));
            updateTimeEpic(epicMap.get(subtask.getEpicId()));
            updateSetPrioritized();
        } else {
            throw new ManagerSaveException("Время для выполнения задачи уже занято.");
        }
    }

    private void updateStatusEpic(Epic epic) {
        if (!epic.getListSubtaskId().isEmpty()) {
            int NEW = 0;
            int DONE = 0;
            int i = 0;
            for (Integer idList : epic.getListSubtaskId()) {
                if (i == NEW && subtaskMap.get(idList).getStatus() == StatusTask.NEW) {
                    epic.setStatus(StatusTask.NEW);
                    NEW++;
                } else if (i == DONE && subtaskMap.get(idList).getStatus() == StatusTask.DONE) {
                    epic.setStatus(StatusTask.DONE);
                    DONE++;
                } else {
                    epic.setStatus(StatusTask.IN_PROGRESS);
                }
                i++;
            }
        } else {
            epic.setStatus(StatusTask.NEW);
        }
    }

    private void updateTimeEpic(Epic epic) {
        Duration duration = Duration.ofMinutes(0);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (!epic.getListSubtaskId().isEmpty()) {
            for (Integer idList : epic.getListSubtaskId()) {
                Subtask subtask = subtaskMap.get(idList);
                duration = duration.plusMinutes(subtask.getDuration().toMinutes());
                if (startTime == null && subtask.getStartTime() != null) {
                    startTime = subtask.getStartTime();
                } else if (subtask.getStartTime() != null) {
                    if (startTime.isAfter(subtask.getStartTime())) {
                        startTime = subtask.getStartTime();
                    }
                }
                if (endTime == null && subtask.getEndTime() != null) {
                    endTime = subtask.getEndTime();
                } else if (subtask.getEndTime() != null) {
                    if (endTime.isBefore(subtask.getEndTime())) {
                        endTime = subtask.getStartTime();
                    }
                }

            }

        }
        epic.setDuration(duration);
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
    }


    private void updateSetPrioritized() {
        sortPrioritizedTasks.clear();
        sortPrioritizedTasks.addAll(taskMap.values());
        sortPrioritizedTasks.addAll(subtaskMap.values());
        createTimeDurationToTableCheck();
    }

    private boolean validationOfTheIntersection(Task task) { // сложность от 1 в лучш.сл. до от N значений (периода/констант времени).
        LocalDateTime timeTransferTask = task.getStartTime();
        if (timeTransferTask == null) {
            return true;
        }

        long durationTransferTask = task.getDuration().toMinutes();
        Task oldTask = null;
        LocalDateTime oldTaskMapTime = null;

        if (taskMap.containsKey(task.getUniqueId()) || subtaskMap.containsKey(task.getUniqueId())) {
            oldTask = taskMap.containsKey(task.getUniqueId()) ?
                    taskMap.get(task.getUniqueId()) : subtaskMap.get(task.getUniqueId());
            // достаем старый таск, если ранее сохранялся
            oldTaskMapTime = oldTask.getStartTime();
        }

        if (oldTaskMapTime != null) { // если у прошлого имеющегося таска нет времени, то поф идем дальше
            long checkOldDuration = oldTask.getDuration().toMinutes();
            if (checkOldDuration == 0) { // проверяем менялось ли значение времени в новом

                if (timeTransferTask.equals(oldTaskMapTime)) {
                    return true;
                } else {
                    return !checkTableTimeOfPrioritized.contains(timeTransferTask);
                }

            } else {
                List<LocalDateTime> checkListTime = new ArrayList<>();
                LocalDateTime timeEnd = timeTransferTask.plusMinutes(durationTransferTask);

                for (LocalDateTime forTime = timeTransferTask; forTime.isBefore(timeEnd) || forTime.equals(timeEnd);
                     forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                    checkListTime.add(forTime);
                } //собрали значения периодов от нового таска

                timeEnd = oldTaskMapTime.plusMinutes(checkOldDuration);

                for (LocalDateTime forTime = oldTaskMapTime; forTime.isBefore(timeEnd) || forTime.equals(timeEnd);
                     forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                    checkListTime.remove(forTime);
                } // убираем из листа с периодами уже занятые периоды старым таском. они нам и так доступны

                for (LocalDateTime time : checkListTime) {
                    if (checkTableTimeOfPrioritized.contains(time)) {
                        return false;
                    }
                } // если хоть один имеется, то выходим на отмену, так как время занято
            }

            // task 5-7 .......  .....2-6..../....6-7task..../....7-8..../.....1-9...../...1-9task....
            return true; // если все прошло успешно, то можно обновлять/добавлять

        } else {
            // проверка только свободных значений для нового таска
            if (durationTransferTask == 0) {
                return true;
            }

            LocalDateTime timeEnd = timeTransferTask.plusMinutes(durationTransferTask);
            long saveDuration = -TIME_CONSTANT;
            for (LocalDateTime forTime = timeTransferTask; forTime.isBefore(timeEnd) || forTime.equals(timeEnd);
                 forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                if (!checkTableTimeOfPrioritized.contains(forTime)) {
                    saveDuration = saveDuration + TIME_CONSTANT;
                } // если уже есть значение, то не считаем его. если нет, то сохраняем в save
            }
            if (saveDuration == durationTransferTask) { // если ниодного значения не занято, то будет равно
                return true;
            }
            return false;
        }
    }

    private void createTimeDurationToTableCheck() {
        checkTableTimeOfPrioritized.clear();
        LocalDateTime time;
        long saveDuration;
        LocalDateTime timeEnd;
        for (Task task : sortPrioritizedTasks) {

            time = task.getStartTime();
            if (time == null) {
                return;
            }
            saveDuration = task.getDuration().toMinutes();
            if (saveDuration > 0) {
                timeEnd = time.plusMinutes(saveDuration);
                for (LocalDateTime forTime = time; forTime.isBefore(timeEnd);
                     forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                    checkTableTimeOfPrioritized.add(forTime);
                }
            }
        }
    }

    @Override
    public void delIdTaskMap(int id) {
        taskMap.remove(id);
        inHistory.removeHistory(id);
        updateSetPrioritized();
    }

    @Override
    public void delIdEpicMap(int id) {
        for (Integer idList : epicMap.get(id).getListSubtaskId()) {
            subtaskMap.remove(idList);
            inHistory.removeHistory(idList);
        }
        epicMap.remove(id);
        inHistory.removeHistory(id);
        updateSetPrioritized();
    }

    @Override
    public void delIdSubtaskMap(int id) {
        Subtask subtask = subtaskMap.remove(id);
        if (subtask == null) {
            return;
        }
        Epic updateEpic = epicMap.get(subtask.getEpicId());
        ArrayList<Integer> listIds = updateEpic.getListSubtaskId();
        listIds.remove((Integer) id);

        updateStatusEpic(updateEpic);
        updateTimeEpic(updateEpic);
        inHistory.removeHistory(id);
        updateSetPrioritized();
    }

    @Override
    public ArrayList<Subtask> subtasksListToEpic(int id) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        if (!epicMap.get(id).getListSubtaskId().isEmpty()) {
            for (Integer key : epicMap.get(id).getListSubtaskId()) {
                subtasksList.add(subtaskMap.get(key));
            }
        }
        return subtasksList;
    }

    @Override
    public List<Task> getHistory() {
        return inHistory.getHistory();
    }

}