
package hw04_sharieff;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ChatServer {
    static final int PORT = 5190;
    static ArrayList<ClientThread> users = new ArrayList<ClientThread>();
    static int id = 0;

    public static void main(String[] args) {
        try {
            ServerSocket sock_listesner = new ServerSocket(PORT);
            while (true) {
                // server waiting for clients on port 5190
                System.out.println("Server waiting for clients on port " + PORT);
                Socket client = sock_listesner.accept(); // blocking call
                // client connected - connection established 
                ClientThread cliThread = new ClientThread(client);
                users.add(cliThread);
                // this client added to user list 
                cliThread.start();
            }

        } catch(IOException e) {
            System.out.println("IOError: ServerSocket()");
        }
    }
}

class ClientThread extends Thread {
    private Socket user;
    private Scanner sIn;
    private PrintStream sOut;
    private String username;

    public ClientThread(Socket client_socket) {
        this.user = client_socket;
        try {
            sIn = new Scanner(client_socket.getInputStream());
            sOut = new PrintStream(client_socket.getOutputStream(), true);
            username = sIn.nextLine();// blocking call 
            
        } catch (IOException e) {
            System.out.println("IOError: creating i/o streams");

        }
    }

    private synchronized void relayMessage(String message) {
        for (ClientThread cThread : ChatServer.users) {  
            if (cThread.user.isConnected()) {
                cThread.sOut.println(username + ": " + message);
            }
        }
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                String message = sIn.nextLine(); // blocking call
                if (message.equalsIgnoreCase("EXIT")) break;
                relayMessage(message);
            }
        } catch (NoSuchElementException e) {}
        finally {
            sOut.close();
            sIn.close();
            try {
                user.close();
            } catch (IOException e) {
                System.out.println("IOError: Closing client socket");
            }
        }
    }     
}
