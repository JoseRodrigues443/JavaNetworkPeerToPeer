/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.Client;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import list.ClientLogList;
import list.FileList;
import networkElements.Address;
import networkElements.FileConnectionTCP;
import networkElements.ConnectionUDP;
import networkElements.IpProtocol;
import networkElements.TextConnectionTCP;
import networkElements.TextConnectionTCP.TCPServerReceiver;
import server.udp.AnnouncementUDP;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class Server {

    private int idServer;
    private Address serverAddress;
    private Address addressToSendUdp;
    private File folderToSharePath;
    private int secondsToSendUDP;
    private ClientLogList clientLogList;
    /**
     * on or off, to send the message
     */
    private boolean serverStatusActive;

    private static int serverCount = 0;
    public static final String FOLDER_PATH_TO_SHARE_DEFAULT
            = "share";
    private static final int SECONDS_TO_SEND_ANNOUNCEMENT_DEFAULT = 30;
    /**
     * gets the value of the download request
     */
    private static final int SECONDS_TO_RECEIVE_DOWNLOAD_REQUEST_ANNOUNCEMENT_DEFAULT = 1;

    /**
     * to get the udp received
     */
    private TCPServerReceiver listen;

    /**
     *
     * @param serverAddress
     * @param generateTcp true will generate as personal TCP port
     */
    public Server(Address serverAddress, boolean generateTcp) {
        this.idServer = serverCount;
        serverCount++;
        this.serverAddress = serverAddress;
        /**
         * this will create an usable/personal TCP port for the server
         */
        if (generateTcp) {
            this.serverAddress.setTcpPort(idServer + Utils.sumOfAllIpv4Digits(serverAddress.getAddress()));
        }

        if (Utils.validadeFileCreateIfMissing(FOLDER_PATH_TO_SHARE_DEFAULT, false, true)) {
            folderToSharePath = new File(FOLDER_PATH_TO_SHARE_DEFAULT);
        }
        secondsToSendUDP = SECONDS_TO_SEND_ANNOUNCEMENT_DEFAULT;
        serverStatusActive = false;
        clientLogList = new ClientLogList();
        addressToSendUdp = new Address(true);
        run();
    }

    public Server(int idServer, Address serverAddress, File folderToSharePath, int secondsToSendUDP) {
        this.idServer = idServer;
        this.serverAddress = serverAddress;
        Utils.validadeFileCreateIfMissing(folderToSharePath.getAbsolutePath(), false, true);
        this.folderToSharePath = folderToSharePath;
        this.secondsToSendUDP = secondsToSendUDP;
        serverStatusActive = false;
        clientLogList = new ClientLogList();
        addressToSendUdp = new Address(true);
        run();
    }

    private boolean run() {
        //receiveTcpRequestToSendTcpFilesTimed();
        clientLogList.add(new ClientLog(serverAddress, new list.FileList(listAllFiles()), -1, true));
        startReceiveTcpAnnoucement();
        sendUDPAnnoucementTimed();
        return true;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public Address getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(Address serverAddress) {
        this.serverAddress = serverAddress;
    }

    public File getFolderToSharePath() {
        return folderToSharePath;
    }

    public void setFolderToSharePath(File folderToSharePath) {
        this.folderToSharePath = folderToSharePath;
    }

    public int getSecondsToSendUDP() {
        return secondsToSendUDP;
    }

    public void setSecondsToSendUDP(int secondsToSendUDP) {
        this.secondsToSendUDP = secondsToSendUDP;
    }

    public boolean isServerStatusActive() {
        return serverStatusActive;
    }

    public void setServerStatusActive(boolean serverStatusActive) {
        this.serverStatusActive = serverStatusActive;
    }

    public static int getServerCount() {
        return serverCount;
    }

    public static void setServerCount(int serverCount) {
        Server.serverCount = serverCount;
    }

    public ClientLogList getClientLogList() {
        return clientLogList;
    }

    public void setClientLogList(ClientLogList clientLogList) {
        this.clientLogList = clientLogList;
    }

    public Address getAddressToSendUdp() {
        return addressToSendUdp;
    }

    public void setAddressToSendUdp(Address addressToSendUdp) {
        this.addressToSendUdp = addressToSendUdp;
    }

    /**
     *
     * @return
     */
    public boolean turnOnServer() {
        serverStatusActive = true;
        /**
         * start runnable
         */
        run();
        return serverStatusActive;
    }

    /**
     *
     * @return
     */
    public boolean turnOffServer() {
        serverStatusActive = false;
        return serverStatusActive;
    }


    /*
    
        returns list of files in the "./src/server/share" folder
    
     */
    public ArrayList<File> listAllFiles() {
        File[] listOfFiles = folderToSharePath.listFiles();
        ArrayList<File> usableFiles = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                usableFiles.add(listOfFiles[i]);
            } else if (listOfFiles[i].isDirectory()) {
                /*IGNORE*/
            }
        }
        Collections.sort(usableFiles);
        return usableFiles;
    }

    /**
     * for each n secondas will send an udp annoucement
     *
     * @return
     */
    private boolean sendUDPAnnoucementTimed() {
        /*amount of time to the run method be executed again*/
        int timeInterval = Utils.secondsToMiliseconds(secondsToSendUDP, SECONDS_TO_SEND_ANNOUNCEMENT_DEFAULT);
        Timer timerToSendUdp = new Timer();
        timerToSendUdp.schedule(new TimerTask() {
            @Override
            public void run() {
                /**
                 * if server broadcast file list is on (=true)....
                 */
                if (serverStatusActive) {
                    /*not implemented yet*/
                    server.udp.AnnouncementUDP announcementUDP
                            = new AnnouncementUDP(serverAddress, addressToSendUdp, listAllFiles(), idServer);

                    String toSend = announcementUDP.writeAnnoucementToSendFileListUpdate();
                    utils.Utils.print("--> Server --> ESTOU ATIVO!! Id: " + idServer + "  Ip address: " + serverAddress.getAddress());
                    utils.Utils.print("--> Server --> To send: " + toSend);
                    /**
                     * send to client in 32011
                     */
                    ConnectionUDP connectionUDP = new ConnectionUDP(ConnectionUDP.PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT);
                    connectionUDP.serverSendMessage(toSend, addressToSendUdp);

                } else {
                    //System.out.println("--> Inativo");
                    utils.Utils.print("--> Inativo");
                    timerToSendUdp.cancel();
                    timerToSendUdp.purge();
                }
            }
        }, 0, timeInterval);

        return true;
    }

    public boolean sendTCP(String udpRequest, int tcp, Address clientAddress) {
        File zipped = filesAfterUDPCompressedFile(udpRequest);
        return sendTCPFile(zipped, tcp, clientAddress);
    }

    /**
     *
     * FORMAT: 
     * 
     * "sender_address";"receiver_address";"COMPUTER_id";"tcp
     * port";"file_name_1";"file_name_2";"file_name_3";"file_name_N";
     *
     * zips file from an udp request from the client
     *
     * @param udpRequest
     * @return
     */
    private File filesAfterUDPCompressedFile(String udpRequest) {
        String zippedFileName = "/" + Utils.ZIPPED_FILE_NAME_DEFAULT;
        String[] splited = udpRequest.split(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT);
        ArrayList<String> filesName = new ArrayList<>();
        for (int i = 4; i < splited.length; i++) {
            if (Utils.listAllFilesStringList(listAllFiles()).contains(splited[i])) {
                /**
                 * if file requested exist in list
                 */
                filesName.add(splited[i]);
            }
        }
        File zipped = Utils.fileZipper(this.folderToSharePath, filesName,
                folderToSharePath.getAbsolutePath() + zippedFileName);
        return zipped;
    }

    /**
     *
     * @param compressedFile
     * @param tcp
     * @param clientAddress
     *
     * @return
     */
    private boolean sendTCPFile(File compressedFile, int tcp, Address clientAddress) {
        FileConnectionTCP cP = new FileConnectionTCP(tcp);
        cP.sendFile(compressedFile);

        /**
         * clean zipped file
         */
        Utils.removeFile(compressedFile.getAbsolutePath());
        return true;
    }

    private void startReceiveTcpAnnoucement() {
        TextConnectionTCP connectionTCP = new TextConnectionTCP(0);
        listen = connectionTCP.tcpListener(this);
        setIdServer(listen.getTcpPort());
    }

    private boolean receiveTcpRequestToSendTcpFilesTimed() {
        /*amount of time to the run method be executed again*/
        int timeInterval = Utils.secondsToMiliseconds(SECONDS_TO_RECEIVE_DOWNLOAD_REQUEST_ANNOUNCEMENT_DEFAULT, Client.SECONDS_TO_RECEIVE_UDP_DEFAULT);
        Timer timerToSendUdp = new Timer();
        timerToSendUdp.schedule(new TimerTask() {
            @Override
            public void run() {
                String received;
                received = listen.getMessage();
                if (received.contains(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT)
                        && !received.contains(AnnouncementUDP.fileSizeSeparator[0])
                        && !received.contains(AnnouncementUDP.fileSizeSeparator[1])) {
                    
                    if (received.contains("") || received.isEmpty()) {
                        /**
                         * nothing
                         */

                    } else {

                        Utils.print("--> UDP RECEIVED --> SERVER --> \n\nMESSAGE_CLIENT_RECEIVED = " + received);
                        /**
                         * 192.168.0.1;192.168.0.3;21;12000;banana.txt;how_to_build_a_spaceship_for_dummies.txt;pao.txt;muhahaha.txt;
                         */
                        String[] splited = received.split(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT);
                        int tcpValue = Integer.valueOf(splited[3]);
                        Address a = new Address(IpProtocol.IPV4, splited[1],
                                serverAddress.getMaskAddress(),
                                serverAddress.getBroadcastAddress(),
                                serverAddress.getUdpPort(), tcpValue);
                        serverAddress.setTcpPort(tcpValue);
                        createLogAndAddItToList(a, received, tcpValue);

                        sendTCP(received, tcpValue, a);
                    }
                }

            }

        }, 0, timeInterval);

        return true;
    }

    /**
     * add an log entry
     *
     *
     * @param clientAddress
     * @param udpRequest
     * @param tcpPort
     */
    public void createLogAndAddItToList(Address clientAddress, String udpRequest, int tcpPort) {
        FileList a = new FileList();
        String[] splited = udpRequest.split(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT);
        for (int i = 4; i < splited.length; i++) {
            if (Utils.listAllFilesStringList(listAllFiles()).contains(splited[i])) {
                /**
                 * if file requested exist in list
                 */
                a.add(new File(splited[i]));
            }
        }
        ClientLog e = new ClientLog(clientAddress, a, tcpPort, false);
        clientLogList.add(e);
    }

}
