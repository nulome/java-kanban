package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path saveFile;

    public FileBackedTasksManager(Path file) {
        this.saveFile = file;
    }

    static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy..HH:mm");

    private void save() {
        try (Writer fileWriter = new FileWriter(String.valueOf(saveFile.getFileName()))) {

            fileWriter.write("id,type,name,status,description,duration,startTime,epic\n");

            for (Task task : getTaskMap().values()) {
                fileWriter.write(toString(task, TypeTask.TASK) + "\n");
            }
            for (Epic task : getEpicMap().values()) {
                fileWriter.write(toString(task, TypeTask.EPIC) + "\n");
            }
            for (Subtask task : getSubtaskMap().values()) {
                fileWriter.write(toString(task, TypeTask.SUBTASK) + "\n");
            }
            fileWriter.write("\n");

            if (!getHistory().isEmpty()) {
                fileWriter.write(historyToString(inHistory));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не удается сохранить в файл" + saveFile.getFileName(), e);
        }
    }


    private String toString(Task task, TypeTask type) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getUniqueId()).append(",").append(type).append(",").append(task.getName())
                .append(",").append(task.getStatus()).append(",").append(task.getDescription()).append(",")
                .append(task.getDuration().toMinutes()).append(",");

        if(task.getStartTime() == null) {
            sb.append("null,");
        } else {
            sb.append(task.getStartTime().format(formater)).append(",");
        }
        if (type == TypeTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(subtask.getEpicId());
        }

        return sb.toString();
    }

    private static Task fromString(String value) {

        String[] stringTask = value.split(",");
        final int uniqueId = Integer.parseInt(stringTask[0]);
        final TypeTask typeTask = TypeTask.valueOf(stringTask[1]);
        final String nameTask = stringTask[2];
        final StatusTask status = StatusTask.valueOf(stringTask[3]);
        final String descriptionTask = stringTask[4];
        final Duration duration = Duration.ofMinutes(Integer.parseInt(stringTask[5]));
        final LocalDateTime startTime;
        if(stringTask[6].equals("null")){
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(stringTask[6], formater);
        }


        Task task = null;

        switch (typeTask) {
            case TASK:
                task = new Task(nameTask, descriptionTask, status, uniqueId, duration, startTime);
                break;

            case EPIC:
                task = new Epic(nameTask, descriptionTask, status, uniqueId, duration, startTime);
                break;

            case SUBTASK:
                int epicNum = Integer.parseInt(stringTask[7]);
                task = new Subtask(nameTask, descriptionTask, status, uniqueId, duration, startTime, epicNum);
                break;
        }

        return task;
    }


    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getUniqueId()).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.reverse();
        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] stringHistory = value.split(",");
        List<Integer> listHistory = new ArrayList<>();
        for (String strNumber : stringHistory) {
            Integer number = Integer.parseInt(strNumber);
            listHistory.add(number);
        }
        return listHistory;
    }


    public static FileBackedTasksManager loadFromFile(Path file) {

        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file);

        try {
            String strData = Files.readString(Path.of(file.toUri()));
            String[] stringsTask = strData.split("\n");
            List<Integer> history = Collections.emptyList();
            int saveMaxId = 0;

            for (int i = 1; i < stringsTask.length; i++) {
                String line = stringsTask[i];
                if (line.isEmpty()) {
                    history = historyFromString(stringsTask[i + 1]);
                    break;
                }

                final Task task = fromString(line);
                final int id = task.getUniqueId();
                if (id > saveMaxId) {
                    saveMaxId = id;
                }

                switch (task.getType()) {
                    case TASK:
                        fileBacked.taskMap.put(id, task);
                        fileBacked.sortPrioritizedTasks.add(task);
                        break;

                    case EPIC:
                        fileBacked.epicMap.put(id, (Epic) task);
                        break;

                    case SUBTASK:
                        fileBacked.subtaskMap.put(id, (Subtask) task);
                        fileBacked.sortPrioritizedTasks.add(task);
                        break;
                }
            }

            for (Subtask subtask : fileBacked.subtaskMap.values()) {
                ArrayList<Integer> listEpicId = fileBacked.epicMap.get(subtask.getEpicId()).getListSubtaskId();
                listEpicId.add(subtask.getEpicId());
            }

            for (Integer taskId : history) {
                Task task;
                if (fileBacked.taskMap.containsKey(taskId)) {
                    task = fileBacked.taskMap.get(taskId);
                } else if (fileBacked.epicMap.containsKey(taskId)) {
                    task = fileBacked.epicMap.get(taskId);
                } else {
                    task = fileBacked.subtaskMap.get(taskId);
                }

                fileBacked.inHistory.addHistory(task);
            }

            fileBacked.setNewId(saveMaxId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileBacked;
    }


    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public Integer creationTask(Task task) {
        Integer id = super.creationTask(task);
        save();
        return id;
    }

    @Override
    public void clearTaskMap() {
        super.clearTaskMap();
        save();
    }

    @Override
    public void clearEpicMap() {
        super.clearEpicMap();
        save();
    }

    @Override
    public void clearSubtaskMap() {
        super.clearSubtaskMap();
        save();
    }

    @Override
    public Task getTaskById(Integer key) {
        Task task = super.getTaskById(key);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer key) {
        Epic task = super.getEpicById(key);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer key) {
        Subtask task = super.getSubtaskById(key);
        save();
        return task;
    }

    @Override
    public Integer creationEpic(Epic epic) {
        Integer id = super.creationEpic(epic);
        save();
        return id;
    }

    @Override
    public Integer creationSubtask(Subtask subtask) {
        Integer id = super.creationSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void delIdTaskMap(int id) {
        super.delIdTaskMap(id);
        save();
    }

    @Override
    public void delIdEpicMap(int id) {
        super.delIdEpicMap(id);
        save();
    }

    @Override
    public void delIdSubtaskMap(int id) {
        super.delIdSubtaskMap(id);
        save();
    }
}
