import java.util.Objects;

public class Check {

    Objects print;
    void addTaskOneEpic () {
        String name = "Выполнение ТЗ спринт 3";
        String description = "Эпик с 1 подзадачей";

    }
    void addTaskTwoEpic() {
        String name = "Выполнение ТЗ спринт 3";
        String description = "Эпик с 2 подзадачами";

    }

    void addTaskTest(){
        String name = "Тест";
        String description = "Задача";

    }

    void printTask () {

    }


    void printMenu() {
        System.out.println("1 - добавить задачи");
        System.out.println("2 - показать задачи");
        System.out.println("3 - добавить подзадачу");
        System.out.println("0 - выйти");
    }
}


