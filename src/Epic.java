import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> idSubtask;

    public Epic(String name, String description, int status) {
        super(name, description, status);
        idSubtask = new ArrayList<>();
        updateStatus(status);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                "status='" + printStatus() +
                '}';
    }
}
