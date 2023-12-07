package ru.application.tasktracking.objects;

import ru.application.tasktracking.service.StatusTask;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> listSubtaskId = new ArrayList<>();

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getListSubtaskId() {
        return listSubtaskId;
    }

    public void setListSubtaskId(ArrayList<Integer> listSubtaskId) {
        this.listSubtaskId = listSubtaskId;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                //", listSubtaskId=" + listSubtaskId +
                '}';
    }
}