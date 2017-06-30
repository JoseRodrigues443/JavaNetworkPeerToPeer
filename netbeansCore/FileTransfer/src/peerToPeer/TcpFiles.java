/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peerToPeer;

import java.util.ArrayList;
import networkElements.Address;
import server.udp.AnnouncementUDP;

/**
 *
 * @author arch-admin
 */
public class TcpFiles {

    private int tcpPort;
    private Address address;
    private ArrayList<String> fileList;

    public TcpFiles(int tcpPort, Address address, ArrayList<String> fileList) {
        this.tcpPort = tcpPort;
        this.address = address;
        this.fileList = fileList;
    }

    public TcpFiles(int tcpPort) {
        this.tcpPort = tcpPort;
        this.fileList = new ArrayList<>();
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    
    public ArrayList<String> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<String> fileList) {
        this.fileList = fileList;
    }

    public boolean add(String e) {
        if (!fileList.contains(e)) {
            return fileList.add(e);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        TcpFiles tmp = (TcpFiles) o;
        if (this.tcpPort != tmp.tcpPort) {
            return false;
        }
        return true;
    }

    public ArrayList<String> getFileListCleaned() {
        ArrayList<String> toReturn = new ArrayList<>();
        String toAdd = "";
        for (String cicle : this.fileList){
            toAdd = cicle.replaceAll(AnnouncementUDP.fileSizeSeparator[0] + "[0-9]*\\" + AnnouncementUDP.fileSizeSeparator[1], "");
            toReturn.add(toAdd);
        }
        return toReturn;
    }

}
