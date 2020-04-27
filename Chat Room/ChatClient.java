
package chatclient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ChatClient {
    
    static boolean done = false;
    static boolean sentUsername = false;
    static boolean connected = false;
   
    private static Socket socket;
    static Scanner sIn;
    static PrintStream sOut;
    static Connect serverConn;

    static JFrame frame;
    static JTextField input;
    static JTextArea output;

    public static final String SERVER_IP = "127.0.0.1"; // localhost
    public static int SERVER_PORT = -1;
    
    public static void main(String[] args) {
        // Setup the JFrame
        frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);

        JPanel panel = new JPanel();
        output = new JTextArea();
        output.setLineWrap(true);

        JButton sendButton = new JButton("Send");
        ButtonListener listener = new ButtonListener(sendButton);
        sendButton.addActionListener(listener);

        input = new JTextField(30);

        panel.add(input);
        panel.add(sendButton);

        frame.add(panel, BorderLayout.SOUTH);
        frame.add(output, BorderLayout.CENTER);

        output.append("Enter a port # or click 'Send' to connect to the default port: 5190");
        
        //display gui
        frame.setVisible(true);

        
        //Main thread handles user input via the ActionListener
        while (!done) {}

        sOut.close();
        sIn.close();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("IOError: closing client socket");
        }
        System.exit(0);
    }

    public static boolean connectServer() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
        } catch (IOException e) {
            System.out.println("IOError: connecting to server");
            return false;
        }
        System.out.println("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
        try {
            sIn = new Scanner(socket.getInputStream());
            sOut = new PrintStream(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("IOError: creating i/o streams");
            return false;
        }
        return true;
    }

}

class ButtonListener implements ActionListener {
    JButton button;
    
    ButtonListener(JButton button) {
        this.button = button;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if ((JButton)e.getSource() == button) {
            String message = ChatClient.input.getText(); // retrieve text from the textField
            ChatClient.input.setText(null);  // clear the text field
            
             // check if client is connected
            if (!ChatClient.connected) {                    // not conected
                if (!message.isEmpty()) {               // user entered something
                    ChatClient.SERVER_PORT = Integer.parseInt(message);
                }
                else {                                  // use the default port
                    ChatClient.SERVER_PORT = 5190;
                }
                ChatClient.output.setText(null);        // clear the output
                // try to connect
                if (!ChatClient.connectServer()) {                // could not connect
                    ChatClient.output.setText("Could not connect to port: " + ChatClient.SERVER_PORT + ". Try another one." +
                            " Or click Submit for default port: 5190");
                    return;
                }
                // successfully connected
                ChatClient.connected = true;
                ChatClient.output.setText("Successfully connected. Enter your username to join the chat.");
            }
            // connected and user entered something
            else if (message != null) {
                if (message.equalsIgnoreCase("EXIT")) { // end the session
                    ChatClient.done = true;
                }
                ChatClient.sOut.println(message);
                if (!ChatClient.sentUsername) { //  update the username
                    ChatClient.sentUsername = true;
                    ChatClient.output.setText(null);
                    ChatClient.output.append("You have now entered the chat room!\r\n");
                    // start accepting data from the server
                    ChatClient.serverConn = new Connect(ChatClient.sIn);
                    ChatClient.serverConn.start();
                }
            }
        }
    }
}


class Connect extends Thread {
    private final Scanner inputStream;
    
    Connect(Scanner socketIn) {
        this.inputStream = socketIn;
    }
    
    @Override
    public void run() {
        try {
            while (!ChatClient.done) {
                String message = inputStream.nextLine();
                if (message == null) {
                    System.out.println("Server has closed the connection");
                    break;
                }
                ChatClient.output.append(message + "\r\n");
            }
        } catch (NoSuchElementException e) {}
        finally {
            inputStream.close();
        }
    }
}