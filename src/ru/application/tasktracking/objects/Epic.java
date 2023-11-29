package ru.application.tasktracking.objects;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> listSubtaskId = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getListSubtaskId() {
        return listSubtaskId;
    }

    public void setListSubtaskId(ArrayList<Integer> listSubtaskId) {
        this.listSubtaskId = listSubtaskId;
    }
}
