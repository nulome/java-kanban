/*
Привет!
- По задачам не проработаны исключения. Практически во всех методах. По ТЗ не было такого упоминания. (null)
- Очень смущает создание пустого эпика или просто задачи. Наверно причина будет понятна дальше по спринтам.
- Тестовые примеры могут ломаться из-за исключений.
- Добавлял задачи с учетом, что информация будет откуда-то поступать, наподобии: ид эпика для связки подзадачи,
ид для обновления и установки статусов.
- Также текст, как в методах, так и в описаниях, только для проверки,

Не нравится:
- Можно переработать сохранение статуса, чтобы был один статус в виде самого текста, вместо трех булевых (NEW,
 IN_PROGRESS, DONE), но в ТЗ не понятно, как это должно выглядеть в финале.
- Не добавил обновление списка подзадач в эпике после удаления самой подзадачи. То есть эпик будет считать,
 что в нем есть задача, но она уже удалена. Доработаем думаю дальше, пока что не стал уделять время.
*/

public class Check {
    Manager manager = new Manager();

    void switchMenu(int scan) {
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
                manager.updateSubtaskToId("Подзадача обнолвена команда 13", "Обновление", 2, 1);
                break;
            case 14:
                manager.updateEpicToId("Обновление задачи команда 14", "Обновление", 2, 1);
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
                manager.showListEpicToId(1);
                break;
            case 19:
                System.out.println(manager.epicMap.get(1).printStatus());
                break;
            case 20:
                testUpdateStatusEpic();
                break;
            case 21:
                testUpdateStatusEpicToSubtask();
                break;
            case 22:
                testUpdateStatusSubtaskFive();
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
        System.out.println("18 - показать список подзадач по ид эпик");
        System.out.println("19 - проверка статуса ид 1 эпик");
        System.out.println("20 - проверка обновления статуса ид 1 эпик");
        System.out.println("21 - проверка обновления статуса при изменении 1 подзадачи");
        System.out.println("22 - статус эпика по 5 подзадачам");

        System.out.println("0 - выйти");
    }

    void creationAdd() {
        manager.creationTask("Задача тест 1", "Описание тест 1", 1);
        manager.creationEpic("Задача тест 1", "Описание тест 1", 1);
        manager.creationSubtask("Задача тест 1", "", 1, manager.idEpic);
        System.out.println("добавлено по 1 задаче");
    }

    void printTaskAll() {
        manager.printListTaskMap();
        manager.printListEpicMap();
        manager.printListSubtaskMap();
    }

    void addSubtaskToId() {
        manager.creationSubtask("Добавление подзадачи", "", 1, 1);
        System.out.println("добавлена подзадача к 1 эпику");
    }

    void addEpicTask() {
        manager.creationEpic("Задача эпик", "Задача без подзадач", 1);
        System.out.println("добавлена задача эпик без подзадач");
    }

    void addEpic5Subtask() {
        manager.creationEpic("Задача эпик", "Описание тест 1", 1);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        manager.creationSubtask("Задача Эпик 5", "", 1, manager.idEpic);
        System.out.println("добавили эпик и 5 подзадач");
    }

    void printTaskAllId() {
        int id = 2;
        manager.showTaskToId(id);
        manager.showEpicMapToId(id);
        manager.showSubtaskMapToId(id);
    }

    void updateTaskId() {
        int id = 1;
        manager.updateTaskToId("Обновление задачи", "Обновление", 2, id);
    }

    void testUpdateStatusEpic() {
        System.out.println(manager.epicMap.get(1).printStatus());
        manager.updateEpicToId("Обновление задачи", "Обновление статуса на progress", 2, 1);
        System.out.println(manager.epicMap.get(1).printStatus());
        manager.updateEpicToId("Обновление задачи", "Обновление статуса на done", 3, 1);
        System.out.println(manager.epicMap.get(1).printStatus());
        manager.updateEpicToId("Обновление задачи", "Обновление статуса на new", 1, 1);
        System.out.println(manager.epicMap.get(1).printStatus());
    }

    void testUpdateStatusEpicToSubtask() {
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus() + " / ст подзад " + manager.subtaskMap);
        manager.updateSubtaskToId("Обновление подзадачи ст2", "Обновление", 2, 1);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus() + " / ст подзад " + manager.subtaskMap);
        manager.updateSubtaskToId("Обновление подзадачи ст3", "Обновление", 3, 1);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus() + " / ст подзад " + manager.subtaskMap);
        manager.updateSubtaskToId("Обновление подзадачи ст1", "Обновление", 1, 1);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus() + " / ст подзад " + manager.subtaskMap);
    }

    void testUpdateStatusSubtaskFive() {        // для проверки 5, потом 22
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus());
        manager.updateSubtaskToId("Обновление подзадачи 1", "Обновление", 3, 1);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus());
        manager.updateSubtaskToId("Обновление подзадачи 2", "Обновление", 3, 2);
        manager.updateSubtaskToId("Обновление подзадачи 3", "Обновление", 3, 3);
        printTaskAll();
        manager.updateSubtaskToId("Обновление подзадачи 4", "Обновление", 3, 4);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus());
        manager.updateSubtaskToId("Обновление подзадачи 5", "Обновление", 3, 5);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus());

        manager.updateSubtaskToId("Обновление подзадачи 1", "Обновление", 1, 1);
        manager.updateSubtaskToId("Обновление подзадачи 2", "Обновление", 1, 2);
        manager.updateSubtaskToId("Обновление подзадачи 3", "Обновление", 1, 3);
        manager.updateSubtaskToId("Обновление подзадачи 4", "Обновление", 1, 4);
        manager.updateSubtaskToId("Обновление подзадачи 5", "Обновление", 1, 5);
        System.out.println("статус эпик " + manager.epicMap.get(1).printStatus());
    }

}


