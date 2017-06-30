/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import client.Client;
import java.io.File;
import java.util.ArrayList;
import networkElements.Address;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues -
 * 1150710 Tiago Vilaça - 1140412
 */
public class ClientController {

    private final Client client;

    public ClientController(Client client) {
        this.client = client;
    }

    public ArrayList<File> filesDownloaded() {
        return client.getFilesDownloaded().getFilesList();
    }

    public Address getClientAddress() {
        return client.getClientAddress();
    }

    public ArrayList<String> getFilesInDownloadList() {
        return client.getFilesInDownloadList();
    }

    public ArrayList<String> getFilesInDownloadListCleaned() {
        return client.getFilesInDownloadListCleaned();
    }

    public boolean sendDownloadRequestTcp(String nodeToSendAddress, ArrayList<String> fileList) {
        if (client.getServerAddress() != null) {
            return client.sendDownloadRequestTcp(nodeToSendAddress, fileList);
        }
        return false;
    }

    public void setServerAddress(Address serverAddress) {
        client.setServerAddress(serverAddress);
    }

    public Address getServerAddress() {
        return client.getServerAddress();
    }

}
