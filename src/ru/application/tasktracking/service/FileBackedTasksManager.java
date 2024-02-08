package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path saveFile;

    public FileBackedTasksManager(Path file) {
        this.saveFile = file;
    }


    public static void main(String[] args) {

        Path myFile = Paths.get("Tasks.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(myFile);

        System.out.println("Создание");
        Task task1 = new Task("Task #taskId1", "Task description", StatusTask.NEW);
        int taskId1 = fileBackedTasksManager.creationTask(task1);
        Epic epic1 = new Epic("Epic #epicId1", "Epic description", StatusTask.NEW);
        int epicId1 = fileBackedTasksManager.creationEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask #sub1", "Subtask description", StatusTask.NEW, epicId1);
        int sub1 = fileBackedTasksManager.creationSubtask(subtask1);

        System.out.println("таск - " + fileBackedTasksManager.getTasks());
        System.out.println("эпики - " + fileBackedTasksManager.getEpics());
        System.out.println("подзадачи - " + fileBackedTasksManager.getSubtasks());


        Task task = fileBackedTasksManager.getTaskById(taskId1);
        task = fileBackedTasksManager.getEpicById(epicId1);
        task = fileBackedTasksManager.getSubtaskById(sub1);

        System.out.println("История " + fileBackedTasksManager.getHistory());


        System.out.println();
        System.out.println("Сохранение/запись");

        FileBackedTasksManager fileBacked = new FileBackedTasksManager(myFile);
        System.out.println("История fileBacked" + fileBacked.getHistory());

        fileBacked = fileBacked.loadFromFile(myFile);
        System.out.println("История fileBacked" + fileBacked.getHistory());

    }


    private void save() {
        try {
            try (Writer fileWriter = new FileWriter(String.valueOf(saveFile.getFileName()))) {

                fileWriter.write("id,type,name,status,description,epic\n");

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
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }


    private String toString(Task task, TypeTask type) {
        String result = task.getUniqueId() + "," + type + "," + task.getName() + "," + task.getStatus()
                + "," + task.getDescription() + ",";
        if (type == TypeTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return result + subtask.getEpicId();
        }
        return result;
    }

    private static Task fromString(String value) {

        String[] stringTask = value.split(",");
        final int uniqueId = Integer.parseInt(stringTask[0]);
        final TypeTask typeTask = TypeTask.valueOf(stringTask[1]);
        final String nameTask = stringTask[2];
        final StatusTask status = StatusTask.valueOf(stringTask[3]);
        final String descriptionTask = stringTask[4];
        Task task = null;

        switch (typeTask) {
            case TASK:
                task = new Task(nameTask, descriptionTask, status, uniqueId);
                break;

            case EPIC:
                task = new Epic(nameTask, descriptionTask, status, uniqueId);
                break;

            case SUBTASK:
                int epicNum = Integer.parseInt(stringTask[5]);
                task = new Subtask(nameTask, descriptionTask, status, uniqueId, epicNum);
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

    static FileBackedTasksManager loadFromFile(Path file) {

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
                        break;

                    case EPIC:
                        fileBacked.epicMap.put(id, (Epic) task);
                        break;

                    case SUBTASK:
                        fileBacked.subtaskMap.put(id, (Subtask) task);
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
