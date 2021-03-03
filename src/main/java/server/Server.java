package server;

import session.Session;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static config.SocketConfig.SERVER_PORT;
import static utils.CommonUtils.withClientPrefix;
import static utils.CommonUtils.withServerPrefix;

public class Server {

    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;
    private DataOutputStream out;
    private Session session;

    public Server() {
        try {
            startServer();
            waitForTheClient();
            acceptClient();
            introduceToClient();
            listen();
            closeConnection();
        } catch (SocketTimeoutException e) {
            sendGoodbyeMessageAndCloseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void introduceToClient() throws IOException {
        String message = "HI, I AM " + session.getUuid();
        sendMessageToClient(message);
    }

    private void waitForTheClient() {
        System.out.println("Waiting for a client");
    }

    private void listen() throws IOException {
        String line = "";

        while (!line.equals("BYE MATE!")) {

            line = in.readUTF();
            System.out.println(withClientPrefix(line));

            handleIntroduction(line);
            handleGoodbye(line);
        }
    }

    private void handleGoodbye(String line) {
        if (line.equals("BYE MATE!")) {
            sendGoodbyeMessageAndCloseConnection();
        }
    }

    private void handleIntroduction(String line) throws IOException {
        String introduction = "HI, I AM ";
        if (line.contains(introduction)) {
            String name = line.substring(introduction.length());
            session.setName(name);
            sendMessageToClient("HI, " + name);
        }
    }

    private void acceptClient() throws IOException {
        socket = server.accept();
        System.out.println("Client accepted");

        session = new Session();
        socket.setSoTimeout(30000);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    private void startServer() throws IOException {
        server = new ServerSocket(SERVER_PORT);
        System.out.println("Server started");
    }

    private void sendGoodbyeMessageAndCloseConnection() {
        try {
            String message = String.format("BYE %s WE SPOKE FOR %s MS", session.getName(), session.getSessionTime());
            sendMessageToClient(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void sendMessageToClient(String message) throws IOException {
        out.writeUTF(message);
        System.out.println(withServerPrefix(message));
    }

    private void closeConnection() {
        try {
            System.out.println("Closing connection");
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
