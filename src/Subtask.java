
public class Subtask extends Task {

    int idEpic;
    public Subtask(String name, String description, int status, int id) {
        super(name, description, status);
        idEpic = id;
    }

    @Override
    public String toString() {

        return "Subtask{" +
                "name='" + name + '\'' +
                "idEpic=" + idEpic + '\'' +
                "status='" + printStatus() +
                '}';
    }
}
