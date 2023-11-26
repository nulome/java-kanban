import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    Task task;
    Epic epic;
    Subtask subtask;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    int idTask, idEpic, idSubtask = 0;


    void printListTaskMap() {
        System.out.println("Задачи: " + taskMap);
    }

    void printListEpicMap() {
        System.out.println("Эпики: " + epicMap);
    }

    void printListSubtaskMap() {
        System.out.println("Подзадачи: " + subtaskMap);
    }

    void clearListTaskMap() {
        taskMap.clear();
        this.idTask = 0;
        System.out.println("Простые задачи удалены");
    }

    void clearListEpicMap() {
        epicMap.clear();
        this.idEpic = 0;
        System.out.println("Эпики удалены");
    }

    void clearListSubtaskMap() {
        subtaskMap.clear();
        this.idSubtask = 0;
        System.out.println("Подзадачи удалены");
    }

    void showTaskToId(Integer key) {
        System.out.println(taskMap.get(key));
    }

    void showEpicMapToId(Integer key) {
        System.out.println(epicMap.get(key));
    }

    void showSubtaskMapToId(Integer key) {
        System.out.println(subtaskMap.get(key));
    }

    void creationTask(String name, String description, int status) {
        task = new Task(name, description, status);
        this.idTask++;
        taskMap.put(this.idTask, task);
    }

    void creationEpic(String name, String description, int status) {
        epic = new Epic(name, description, status);
        this.idEpic++;
        epicMap.put(this.idEpic, epic);
    }

    void creationSubtask(String name, String description, int status, int idInEpic) {
        subtask = new Subtask(name, description, status, idInEpic);
        this.idSubtask++;
        subtaskMap.put(this.idSubtask, subtask);
        epicMap.get(idInEpic).idSubtask.add(this.idSubtask);
    }

    void updateTaskToId(String name, String description, int newIsStatus, int id) {
        task = new Task(name, description, newIsStatus);
        taskMap.put(id, task);
    }

    void updateEpicToId(String name, String description, int newIsStatus, int id) {

        epic = new Epic(name, description, newIsStatus);
        statusUpEpic(epic, newIsStatus, id);
        epicMap.put(id, epic);
    }

    void updateSubtaskToId(String name, String description, int newIsStatus, int id) {
        subtask = new Subtask(name, description, newIsStatus, subtaskMap.get(id).idEpic);
        subtaskMap.put(id, subtask);
        statusUpEpic(epicMap.get(subtask.idEpic), newIsStatus, subtask.idEpic);
    }

    void statusUpEpic(Epic epic, int newIsStatus, int id) {
        if (!epicMap.get(id).idSubtask.isEmpty()) {
            int NEW = 0;
            int DONE = 0;           //потратил время на данный ребус. Почему-то инты не объявляются в одну строчку.
            int i = 0;
            for (Integer idList : epicMap.get(id).idSubtask) {
                if (i == NEW && (subtaskMap.get(idList).NEW) && (newIsStatus == 1)) {
                    epic.updateStatus(1);
                    NEW++;
                    continue;
                } else if ((subtaskMap.get(idList).DONE) && (newIsStatus == 3) && i == DONE) {
                    epic.updateStatus(3);
                    DONE++;
                    continue;
                } else {
                    epic.updateStatus(2);
                }
                i++;
            }
            epic.idSubtask = epicMap.get(id).idSubtask;
        }
    }

    void delIdTaskMap(int id) {
        taskMap.remove(id);
        System.out.println("Удалена задача " + id);
    }

    void delIdEpicMap(int id) {
        epicMap.remove(id);
        System.out.println("Удален эпик " + id);
    }

    void delIdSubtaskMap(int id) {
        subtaskMap.remove(id);
        System.out.println("Удалена подзадача " + id);
    }

    void showListEpicToId(int id) {
        ArrayList<Integer> list = epicMap.get(id).idSubtask;
        System.out.println(list);
    }
}
