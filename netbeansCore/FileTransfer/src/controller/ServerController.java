/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.util.ArrayList;
import list.ClientLogList;
import networkElements.Address;
import networkElements.IpProtocol;
import server.Server;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class ServerController {

    private final Server server;

    public ServerController(Server server) {
        this.server = server;
    }

    public Address getServerAddress() {
        return server.getServerAddress();
    }

    public ArrayList<File> listAllFiles() {
        return server.listAllFiles();
    }

    

    public int amountOfFiles() {
        return listAllFiles().size();
    }

    public int getSecondsToSendUDP() {
        return server.getSecondsToSendUDP();
    }

    public void setFolderToSharePath(File folderToSharePath) {
        server.setFolderToSharePath(folderToSharePath);
    }

    public boolean turnOnServer() {
        return server.turnOnServer();
    }

    public boolean turnOffServer() {
        return server.turnOffServer();
    }

    public boolean isServerStatusActive() {
        return server.isServerStatusActive();
    }

    public ClientLogList getClientLogList() {
        return server.getClientLogList();
    }

    public void setAddressToSendUdp(Address addressToSendUdp) {
        server.setAddressToSendUdp(addressToSendUdp);
    }

    public Address getAddressToSendUdp() {
        return server.getAddressToSendUdp();
    }
    
   
    
    
}
