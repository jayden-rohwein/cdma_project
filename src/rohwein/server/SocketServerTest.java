/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EricCode;

import javax.swing.JFrame;

/**
 *
 * @author Eric
 */
public class SocketServerTest 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        SocketServer eric = new SocketServer();
        eric.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        eric.startRunning();
    }
    
}