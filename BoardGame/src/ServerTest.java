import servercom.ServerWorker;

import java.net.Socket;
import java.util.Scanner;

public class ServerTest {

    private boolean active = true;

    public static void main(String[] args) {
        ServerTest main = new ServerTest();
    }

    public ServerTest() {
        ServerWorker worker = new ServerWorker("localhost", 7789);
        Thread thread = new Thread(worker);
        thread.start();

        Scanner scanner = new Scanner(System.in);
        String input;
        while (active) {
            try {
                switch((input = scanner.nextLine())) {
                    case "exit":
                        active = false;
                        break;
                    default:
                        worker.sendMessage(input);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
