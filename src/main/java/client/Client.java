package client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static config.SocketConfig.LOCALHOST;
import static config.SocketConfig.SERVER_PORT;
import static utils.CommonUtils.withClientPrefix;
import static utils.CommonUtils.withServerPrefix;

public class Client {

    private Socket socket;
    private DataInputStream in;
    private DataInputStream server;
    private DataOutputStream out;

    public Client(String address, int port) {

        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            in = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
            server = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String clientLine = "";
        String serverLine = "";
        while (!clientLine.contains("BYE") && !serverLine.contains("BYE")) {

            try {
                clientLine = in.readLine();
                System.out.println(withClientPrefix(clientLine));
                out.writeUTF(clientLine);

                serverLine = server.readUTF();
                System.out.println(withServerPrefix(serverLine));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new Client(LOCALHOST, SERVER_PORT);
    }
}
