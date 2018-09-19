/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EricCode;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SocketClient extends JFrame
{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private int serverPort;
    private Socket connection;
    
    /**
     * Constructor
     */
    public SocketClient(String host, int port)
    {
        super("Instant Messenger");
        serverIP = host;
        serverPort = port;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(400,250);
        setVisible(true);
    }
    
    /**
     * Sets up and runs server
     */
    public void startRunning()
    {
        try
        {
            connectToServer();
            setupStreams();
            whileChatting();
        }
        catch(EOFException eofException)
        {
            showMessage("\nClient ended messenger");
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            close();
        }
    }
    
    /**
     * Connects to server
     */
    private void connectToServer() throws IOException
    {
        showMessage("Attempting connection...\n");
        connection = new Socket(InetAddress.getByName(serverIP), serverPort);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
    }
    
    /**
     * Sets up stream to send and receive messages
     */
    private void setupStreams() throws IOException
    {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are open...enter END to quit\n");
    }
    
    /**
     * While chatting
     */
    private void whileChatting() throws IOException
    {
        ableToType(true);
        do
        {
            try
            {
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                showMessage("\nError");
            }
        }
        while(!message.equals("SERVER - END"));
    }
    
    /**
     * Closes streams and sockets
     */
    private void close()
    {
        showMessage("\nClosing down...");
        ableToType(false);
        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    
    /**
     * Sends message to server
     */
    private void sendMessage(String message)
    {
        try
        {
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\nCLIENT - " + message);
        }
        catch(IOException ioException)
        {
            chatWindow.append("\nError sending");
        }
    }
    
    /**
     * Updates chatWindow
     */
    private void showMessage(final String message)
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    chatWindow.append(message);
                }    
            }
        );
    }
    
    /**
     * Lets the user type
     */
    private void ableToType(final boolean tof)
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    userText.setEditable(tof);
                }    
            }
        );
    }
}