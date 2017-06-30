/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package list;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class FileList {
    ArrayList<File> filesList;

    public FileList(ArrayList<File> filesList) {
        this.filesList = filesList;
    }

    public FileList() {
        filesList = new ArrayList<>();
    }

    public ArrayList<File> getFilesList() {
        return filesList;
    }

    public void setFilesList(ArrayList<File> filesList) {
        this.filesList = filesList;
    }

    public File get(int i) {
        return filesList.get(i);
    }

    public boolean add(File e) {
        return filesList.add(e);
    }

    public void add(int i, File e) {
        filesList.add(i, e);
    }
    
}
