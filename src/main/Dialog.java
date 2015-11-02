/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Slothmun
 */
public class Dialog {

    //This method will be accessible by the dialog object and will show an error
    //message when supplied with the string and parent frame
    public void showError(String error, JFrame frame) {
        System.out.println(error);
        JOptionPane.showMessageDialog(frame, error, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    //This is similar to the error message but is set to an information message
    //and does not print to the console
    public void showInfo(String message, JFrame frame) {
        JOptionPane.showMessageDialog(frame, message, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public int showYesNo(String message, JFrame frame) {
        return JOptionPane.showConfirmDialog(frame, message, "Info",
                JOptionPane.YES_NO_OPTION);
    }
}
