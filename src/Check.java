/*
Привет!
- По задачам не проработаны исключения. Практически во всех методах. По ТЗ не было такой задачи. (null)
- Очень смущает создание пустого эпика или просто задачи. Наверно причина будет понятна дальше по спритам.
- Тестовые примеры могут ломаться из-за исключений.
- Добавлял задачи с учетом, что информация будет откуда то поступать, наподобии: ид эпика для связки подзадачи,
ид для обновления и установки статусов.

Не нравится:
- Можно переработать сохранение статуса, чтобы был один статус в виде самого текста (NEW, IN_PROGRESS, DONE),
но в ТЗ не понятно, как это должно выглядеть в финале.
- Не добавил обновление списка подзадач в эпике после удаления самой подзадачи.
*/

public class Check {
    Manager manager = new Manager();
    int counterAdd = 1;
    void switchMenu(int scan){
        switch (scan) {
            case 1:
                creationAdd();
                break;
            case 2:
                printTaskAll();
                break;
            case 3:
                addSubtaskToId();
                break;
            case 4:
                addEpicTask();
                break;
            case 5:
                addEpic5Subtask();
                break;
            case 6:
                manager.printListEpicMap();
                break;
            case 7:
                manager.printListSubtaskMap();
                break;
            case 8:
                manager.clearListSubtaskMap();
                break;
            case 9:
                manager.clearListEpicMap();
                break;
            case 10:
                manager.clearListTaskMap();
                break;
            case 11:
                printTaskAllId();
                break;
            case 12:
                updateTaskId();
                break;
            case 13:
                manager.updateSubtaskToId("Обновление задачи", "Обновление", 2, 1);
                break;
            case 14:
                manager.updateEpicToId("Обновление задачи", "Обновление", 2, 1);
                break;
            case 15:
                manager.delIdEpicMap(3);
                break;
            case 16:
                manager.delIdTaskMap(3);
                break;
            case 17:
                manager.delIdSubtaskMap(3);
                break;
            case 18:
                manager.showListEpicToId(2);
                break;
            default:
                if (scan != 0) System.out.println("Команда не найдена. Повторите");
        }
    }
    void printMenu() {
        System.out.println("1 - добавить все задачи по одной");
        System.out.println("2 - показать задачи");
        System.out.println("3 - добавить подзадачу по ид эпика");
        System.out.println("4 - добавить эпик без подзадач");
        System.out.println("5 - добавить эпик с 5 подзадачами");
        System.out.println("6 - показать эпики");
        System.out.println("7 - показать подзадачи");
        System.out.println("8 - удалить все подзадачи");
        System.out.println("9 - удалить эпики");
        System.out.println("10 - удалить обычные задачи");
        System.out.println("11 - показать задачу/эпик/подзадачу по ид");
        System.out.println("12 - обновление задачи по ид");
        System.out.println("13 - обновление подзадачи по ид");
        System.out.println("14 - обновление эпик по ид");
        System.out.println("15 - удалить эпик по ид");
        System.out.println("16 - удалить задачу по ид");
        System.out.println("17 - удалить подзадачу по ид");
        System.out.println("18 - показать подзадачи по ид эпик");

        System.out.println("0 - выйти");
    }

    void creationAdd(){
        manager.creationTask("Задача тест 1", "Описание тест 1", 1);
        manager.creationEpic("Задача тест 1", "Описание тест 1", 1);
        manager.creationSubtask("Задача тест 1", "", 1, manager.idEpic);
        System.out.println("добавлено по 1 задаче");
    }

    void printTaskAll(){
        manager.printListTaskMap();
        manager.printListEpicMap();
        manager.printListSubtaskMap();
    }
    void addSubtaskToId(){
        manager.creationSubtask("Добавление подзадачи", "", 1, 1);
        System.out.println("добавлена подзадача к 1 эпику");
    }
    void addEpicTask(){
        manager.creationEpic("Задача эпик", "Задача без подзадач", 1);

    }
    void addEpic5Subtask(){
        manager.creationEpic("Задача эпик", "Описание тест 1", 1);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        System.out.println("добавили эпик и 5 задач");
    }

    void printTaskAllId(){
        int id = 2;
        System.out.println(manager.taskMap.get(id));
        System.out.println(manager.epicMap.get(id));
        System.out.println(manager.subtaskMap.get(id));
    }

    void updateTaskId(){
        int id = 1;
        manager.updateTaskToId("Обновление задачи", "Обновление", 2, id);
    }


}


