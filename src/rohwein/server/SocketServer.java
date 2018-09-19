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

public class SocketServer extends JFrame
{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    /**
     * Constructor
     */
    public SocketServer(){
        super("Instant Messenger");
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
        add(new JScrollPane(chatWindow));
        setSize(400,250);
        setVisible(true);
    }
    
    /**
     * Sets up and runs server
     * 
     */
    public void startRunning()
    {
        try
        {
            server = new ServerSocket(1234, 100);
            InetAddress IP=InetAddress.getLocalHost();
            showMessage("Server IP Address: "  + IP.getHostAddress() + "\n");
            showMessage("Server Port: " + server.getLocalPort()+"\n");
            while(true)
            {
                try
                {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }
                //contect and have chat
                catch(EOFException eofException)
                {
                    showMessage("\nServer ended the chat");
                }
                finally
                {
                    close();
                }
            }    
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    
    /**
     * Waits for connection and displays information
     * 
     */
    private void waitForConnection() throws IOException
    {                
        showMessage("Waiting for someone to connect...\n");                
        connection = server.accept();        
        showMessage("Now connected to " + connection.getInetAddress().getHostName());
    }
    
    /**
     * Sets up stream to send and receive messages
     * 
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
        String message = "You are connected";
        sendMessage(message);
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
        while(!message.equals("CLIENT - END"));
    }
    
    /**
     * Closes streams and sockets after chat ends
     */
    private void close()
    {
        showMessage("\nClosing connections...\n ");
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
     * Sends message to client
     */
    private void sendMessage(String message)
    {
        try
        {
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        }
        catch(IOException ioException)
        {
            chatWindow.append("\nError sending");
        }            
    }
    
    /**
     * Updates chatWindow
     */
    private void showMessage(final String text)
    {
        SwingUtilities.invokeLater(
            new Runnable()
            {
                public void run()
                {
                    chatWindow.append(text);
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