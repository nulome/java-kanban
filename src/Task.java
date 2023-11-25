
//Он представляет отдельно стоящую задачу.
 /*кирпичиком такой системы является задача (англ. task). У задачи есть следующие свойства:
         Название, кратко описывающее суть задачи (например, «Переезд»).
         Описание, в котором раскрываются детали.
         Уникальный идентификационный номер задачи, по которому её можно будет найти.
         Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
         a. NEW — задача только создана, но к её выполнению ещё не приступили.
         b. IN_PROGRESS — над задачей ведётся работа.
         c. DONE — задача выполнена.*/


/*Идентификатор задачи
        У каждого типа задач есть идентификатор. Это целое число, уникальное для всех типов задач.
        Для генерации идентификаторов можно использовать числовое поле класса менеджер, увеличивая его на 1,
        когда нужно получить новое значение.*/




public class Task {
    String name;
    String description;
    boolean NEW = false;
    boolean IN_PROGRESS = false;
    boolean DONE = false;

    public Task(String name, String description, int status) {
        this.name = name;
        this.description = description;
        updateStatus(status);
    }

    void updateStatus(int status){
        switch (status){
            case 1 : NEW = true;
            break;
            case 2 : NEW = false; IN_PROGRESS = true;
            break;
            case 3 : NEW = false; IN_PROGRESS = false; DONE = true;
            break;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
