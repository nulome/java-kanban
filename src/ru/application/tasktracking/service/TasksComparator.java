package ru.application.tasktracking.service;

import ru.application.tasktracking.objects.Task;

import java.util.Comparator;

public class TasksComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {

        /*if(task1.getStartTime() != null && task2.getStartTime() != null){
            if(task1.getStartTime().isAfter(task2.getStartTime())){
                return 1;
            } else {
                return -1;
            }
        } else {
                return 1;
        }*/

        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            if (task1.getStartTime().isAfter(task2.getStartTime())) { // 2020 - 2000
                return 1;
            } else {
                return -1;
            }
        } else {
            if(task1.getStartTime() == null){
                return 1;
            }else {
                return -1;
            }
        }
    }
}
