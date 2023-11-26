import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Check check = new Check();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Поехали!");
        check.printMenu();

        while (true) {
            //check.printMenu();
            int scan = scanner.nextInt();
            check.switchMenu(scan);
            if (scan == 0) break;
        }
    }
}

