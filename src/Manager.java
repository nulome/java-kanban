
/*Менеджер
        Кроме классов для описания задач, вам нужно реализовать класс для объекта-менеджера. Он будет запускаться на
         старте программы и управлять всеми задачами. В нём должны быть реализованы следующие функции:
        Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
        Методы для каждого из типа задач(Задача/Эпик/Подзадача):
        a. Получение списка всех задач.
        b. Удаление всех задач.
        c. Получение по идентификатору.
        d. Создание. Сам объект должен передаваться в качестве параметра.
        e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        f. Удаление по идентификатору.
        Дополнительные методы:
        a. Получение списка всех подзадач определённого эпика.
        Управление статусами осуществляется по следующему правилу:
        a. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией
         о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.

        b. Для эпиков:

        если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        во всех остальных случаях статус должен быть IN_PROGRESS.*/


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    Task task;
    Epic epic;
    Subtask subtask;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    int idTask, idEpic, idSubtask = 0;
    Objects objects;

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
        System.out.println();
    }
    void clearListEpicMap() {
        epicMap.clear();
        System.out.println();
    }
    void clearListSubtaskMap() {
        subtaskMap.clear();
        System.out.println();
    }

    void showTaskToId(Integer key){
        System.out.println(taskMap.get(key));
    }
    void showEpicMapToId(Integer key){
        System.out.println(epicMap.get(key));
    }
    void showSubtaskMapToId(Integer key){
        System.out.println(subtaskMap.get(key));
    }

    void creationTask(String name, String description){ // Создание
        task = new Task(name, description);
        this.idTask++;
        taskMap.put(this.idTask, task);
    }
    void creationEpic(String name, String description){
        epic = new Epic(name, description);
        this.idEpic++;
        epicMap.put(this.idEpic, epic);
    }
    void creationSubtask(String name, String description, int idInEpic){
        subtask = new Subtask(name, description, idInEpic);
        this.idSubtask++;
        subtaskMap.put(this.idTask, subtask);
        epicMap.get(idInEpic).idSubtask.add(this.idSubtask);
    }

    void updateTaskToId(String name, String description, int id){
        task = new Task(name, description);
        taskMap.put(id, task);
    }

    void updateEpicToId(String name, String description, int id){
        epic = new Epic(name, description);
        epicMap.put(id, epic);
    }

    void updateSubtaskToId(String name, String description, int id){
        subtask = new Subtask(name, description, subtaskMap.get(id).idEpic);
        taskMap.put(id, subtask);
    }

    void delIdTaskMap(int id) {
        taskMap.remove(id);
        System.out.println();
    }
    void delIdEpicMap(int id) {
        epicMap.remove(id);
        System.out.println();
    }
    void delIdSubtaskMap(int id) {
        subtaskMap.remove(id);
        System.out.println();
    }

    void showListEpicToId(int id) {
        ArrayList<Integer> list = epicMap.get(id).idSubtask;
        System.out.println(list);
    }

}
