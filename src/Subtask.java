
/*задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:
        Для каждой подзадачи известно, в рамках какого эпика она выполняется.
        Каждый эпик знает, какие подзадачи в него входят.
        Завершение всех подзадач эпика считается завершением эпика.*/


import java.util.ArrayList;

public class Subtask extends Task { //подзадачи

    int idEpic;
    public Subtask(String name, String description, int status, int id) {
        super(name, description, status);
        idEpic = id;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                '}';
    }
}
