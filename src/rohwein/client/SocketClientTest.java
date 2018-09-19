/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EricCode;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
/**
 *
 * @author Eric
 */
public class SocketClientTest 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        String strAddress = JOptionPane.showInputDialog(null, "Enter the IP Address of the server: ", "Server IP Address", 1);
        String strPort = JOptionPane.showInputDialog(null, "Enter the Port of the server: ", "Server Port", 1);
        

        SocketClient bob = new SocketClient(strAddress, Integer.parseInt(strPort));
        bob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bob.startRunning();
    }
    
}