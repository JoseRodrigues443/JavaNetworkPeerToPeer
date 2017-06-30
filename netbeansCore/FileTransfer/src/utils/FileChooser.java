/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Joao Fernandes 1141114@isep.ipp.pt
 */
public class FileChooser {

    public FileChooser() {
    }

    public String openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(fileChooser, "File selected!", "Information message", JOptionPane.INFORMATION_MESSAGE);
        }
        return String.valueOf(fileChooser.getSelectedFile());
    }
}
