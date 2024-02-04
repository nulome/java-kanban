package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Epic;
import ru.application.tasktracking.objects.Subtask;
import ru.application.tasktracking.objects.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path saveFile;

    public FileBackedTasksManager(Path file) {
        this.saveFile = file;
    }




    private void save() {
        try {
            Writer fileWriter = new FileWriter("Tasks.csv");
            fileWriter.write("id,type,name,status,description,epic\n");
            for(Task task : super.getTaskMap().values()){
                fileWriter.write(toString(task, TypeTask.TASK) + "\n");
            }
            for(Epic task : super.getEpicMap().values()){
                fileWriter.write(toString(task, TypeTask.EPIC)+ "\n");
            }
            for(Subtask task : super.getSubtaskMap().values()){
                fileWriter.write(toString(task, TypeTask.SUBTASK)+ "\n");
            }
            fileWriter.write("\n");

            if (!super.getHistory().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Task task : super.getHistory()) {
                    sb.append(task.getUniqueId() + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                fileWriter.write(sb.toString());
            }

            fileWriter.close();

        } catch (IOException e){
            System.out.println("Error");
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
