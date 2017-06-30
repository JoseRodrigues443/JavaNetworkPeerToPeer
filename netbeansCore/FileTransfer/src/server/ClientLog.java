/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import list.FileList;
import networkElements.Address;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class ClientLog {

    private int id;
    private Address clientAddress;
    private FileList downloadedFiles;
    private Date date;
    private int tcpPort;
    private boolean isServer;

    private static int clientCount = 0;
    private static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy HH:mm:ss";

    public ClientLog(Address clientAddress, FileList downloadedFiles, int tcpPort, boolean isServer) {
        id = clientCount;
        clientCount++;
        this.clientAddress = clientAddress;
        this.downloadedFiles = downloadedFiles;
        date = new Date();
        this.tcpPort = tcpPort;
        this.isServer = isServer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public FileList getDownloadedFiles() {
        return downloadedFiles;
    }

    public void setDownloadedFiles(FileList downloadedFiles) {
        this.downloadedFiles = downloadedFiles;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static int getClientCount() {
        return clientCount;
    }

    public static void setClientCount(int clientCount) {
        ClientLog.clientCount = clientCount;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public boolean isIsServer() {
        return isServer;
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }

    
    public String dateFormat() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
        return dateFormat.format(date);
    }
    
    public String listFiles(){
        String toReturn = "";
        for (File f : downloadedFiles.getFilesList()){
            toReturn += f.getName() + "|";
        }
        return toReturn;
    }
}
