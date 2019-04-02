package servercom;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ServerWorker implements Runnable {

    private String address ;
    private int port;
    private InputStream in;
    private OutputStream out;

    private byte[] bytes = new byte[3200];
    private int count;

    public ServerWorker(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket connection = new Socket(address, port);
            in = connection.getInputStream();
            out = connection.getOutputStream();

            while ((count = in.read(bytes)) > 0) {
                handleResponse(new String((Arrays.copyOfRange(bytes, 0, count))).split(" ", 4));
            }
            System.out.println("Stream has closed");
            in.close();
            out.close();
            connection.close();
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void handleResponse(String[] response) {
        switch (response[0]) {
            case "OK":
                // TODO: Do something with confirmation?
                System.out.println("Confirmed");
                break;
            case "SVR":
                if (response[1].equals("GAME")) {
                    handleGameResponse(response);
                } else {
                    // TODO: Handle unknown response (help should not be called here)
                }
                break;
            default:
                // TODO: Handle unknown response
                break;
        }
    }

    private void handleGameResponse(String[] response) {
        switch (response[2]) {
            case "WIN":
                break;
            case "LOSE":
                break;
            case "DRAW":
                break;
            case "MATCH":
                break;
            case "MOVE":
                break;
            case "YOURTURN":
                break;
            default:
                // TODO: Handle unknown response
                System.out.println("Panic");
                break;
        }
        System.out.println(response[3]);
    }

    private void handleHelpResponse(String[] response) {
        switch (response[2]) {

        }
        System.out.println("Panic Help");
    }

    public void sendMessage(String s) {
        try {
            out.write(s.getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
