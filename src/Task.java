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

    void updateStatus(int status) {
        switch (status) {
            case 1:
                NEW = true;
                IN_PROGRESS = false;
                DONE = false;
                break;
            case 2:
                NEW = false;
                IN_PROGRESS = true;
                DONE = false;
                break;
            case 3:
                NEW = false;
                IN_PROGRESS = false;
                DONE = true;
                break;
        }
    }

    String printStatus() {
        if (IN_PROGRESS) {
            return "IN_PROGRESS";
        } else if (DONE) {
            return "DONE";
        }
        return "NEW";
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}