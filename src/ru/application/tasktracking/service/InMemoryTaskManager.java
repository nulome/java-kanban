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
    protected TreeSet<Task> sortPrioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getUniqueId));
    protected final HashSet<LocalDateTime> checkTableTimeOfPrioritized = new HashSet<>();
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

    public void setSortPrioritizedTasks(TreeSet<Task> sortPrioritizedTasks) {
        this.sortPrioritizedTasks = sortPrioritizedTasks;
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
            updateSetPrioritizedDelete(task);
        }
        taskMap.clear();
    }

    @Override
    public void clearEpicMap() {
        for (Task task : epicMap.values()) {
            inHistory.removeHistory(task.getUniqueId());
        }
        for (Task task : subtaskMap.values()) {
            inHistory.removeHistory(task.getUniqueId());
            updateSetPrioritizedDelete(task);
        }
        epicMap.clear();
        subtaskMap.clear();
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
            updateSetPrioritizedDelete(task);
        }
        subtaskMap.clear();
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
            updateSetPrioritizedAdd(task);
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
            updateSetPrioritizedAdd(subtask);
            return subtask.getUniqueId();
        } else {
            throw new ManagerSaveException("Время для выполнения задачи уже занято.");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (validationOfTheIntersection(task)) {
            int id = task.getUniqueId();
            updateSetPrioritizedDelete(taskMap.get(id));
            taskMap.put(id, task);
            updateSetPrioritizedAdd(task);
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
            updateSetPrioritizedDelete(subtaskMap.get(subtask.getUniqueId()));
            subtaskMap.put(subtask.getUniqueId(), subtask);
            updateSetPrioritizedAdd(subtask);

            updateStatusEpic(epicMap.get(subtask.getEpicId()));
            updateTimeEpic(epicMap.get(subtask.getEpicId()));
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


    private void updateSetPrioritizedAdd(Task task) {
        sortPrioritizedTasks.add(task);
        updateTimeDurationToTableCheckAdd(task);
    }

    private void updateSetPrioritizedDelete(Task task) {
        sortPrioritizedTasks.remove(task);
        updateTimeDurationToTableCheckDelete(task);
    }

    private void updateTimeDurationToTableCheckAdd(Task task) {
        LocalDateTime time = task.getStartTime();
        if (time == null) {
            return;
        }

        long saveDuration = task.getDuration().toMinutes();
        if (saveDuration > 0) {
            LocalDateTime timeEnd = time.plusMinutes(saveDuration);
            for (LocalDateTime forTime = time; forTime.isBefore(timeEnd);
                 forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                checkTableTimeOfPrioritized.add(forTime);
            }
        }
    }

    private void updateTimeDurationToTableCheckDelete(Task task) {
        LocalDateTime time = task.getStartTime();
        if (time == null) {
            return;
        }
        long saveDuration = task.getDuration().toMinutes();
        if (saveDuration > 0) {
            LocalDateTime timeEnd = time.plusMinutes(saveDuration);
            for (LocalDateTime forTime = time; forTime.isBefore(timeEnd);
                 forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                checkTableTimeOfPrioritized.remove(forTime);
            }
        }

    }

    private boolean validationOfTheIntersection(Task task) {
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
            oldTaskMapTime = oldTask.getStartTime();
        }

        if (oldTaskMapTime != null) {
            long checkOldDuration = oldTask.getDuration().toMinutes();
            if (checkOldDuration == 0) {

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
                }

                timeEnd = oldTaskMapTime.plusMinutes(checkOldDuration);

                for (LocalDateTime forTime = oldTaskMapTime; forTime.isBefore(timeEnd) || forTime.equals(timeEnd);
                     forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                    checkListTime.remove(forTime);
                }

                for (LocalDateTime time : checkListTime) {
                    if (checkTableTimeOfPrioritized.contains(time)) {
                        return false;
                    }
                }
            }
            return true;

        } else {
            if (durationTransferTask == 0) {
                return true;
            }

            LocalDateTime timeEnd = timeTransferTask.plusMinutes(durationTransferTask);
            long saveDuration = -TIME_CONSTANT;
            for (LocalDateTime forTime = timeTransferTask; forTime.isBefore(timeEnd) || forTime.equals(timeEnd);
                 forTime = forTime.plusMinutes(TIME_CONSTANT)) {
                if (!checkTableTimeOfPrioritized.contains(forTime)) {
                    saveDuration = saveDuration + TIME_CONSTANT;
                }
            }
            if (saveDuration == durationTransferTask) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void delIdTaskMap(int id) {
        updateSetPrioritizedDelete(taskMap.get(id));
        taskMap.remove(id);
        inHistory.removeHistory(id);
    }

    @Override
    public void delIdEpicMap(int id) {
        for (Integer idList : epicMap.get(id).getListSubtaskId()) {
            updateSetPrioritizedDelete(subtaskMap.get(idList));
            subtaskMap.remove(idList);
            inHistory.removeHistory(idList);
        }
        epicMap.remove(id);
        inHistory.removeHistory(id);
    }

    @Override
    public void delIdSubtaskMap(int id) {
        updateSetPrioritizedDelete(subtaskMap.get(id));
        Subtask subtask = subtaskMap.remove(id);
        Epic updateEpic = epicMap.get(subtask.getEpicId());
        ArrayList<Integer> listIds = updateEpic.getListSubtaskId();
        listIds.remove((Integer) id);

        updateStatusEpic(updateEpic);
        updateTimeEpic(updateEpic);
        inHistory.removeHistory(id);
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