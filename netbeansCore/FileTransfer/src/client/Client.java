/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import list.FileList;
import networkElements.Address;
import networkElements.FileConnectionTCP;
import networkElements.ConnectionUDP;
import networkElements.ConnectionUDP.UDPClientStringManager;
import networkElements.IpProtocol;
import networkElements.TextConnectionTCP;
import server.udp.AnnouncementUDP;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues -
 * 1150710 Tiago Vilaça - 1140412
 */
public class Client implements ClientInterface{

    private int idClient;
    private int serverTCPListenPort;
    private Address clientAddress;
    private Address serverAddress;

    //Files________________________________________
    /**
     * files that may be downlaoded from server
     */
    private ArrayList<String> filesInDownloadList;
    /**
     * files alreasy downloaded and in directorys
     */
    private FileList filesDownloaded;
    private int secondsToUpdateList;
    private File folderToDownloadPath;

    //End________________________________________
    private static int clientCount = 0;

    /**
     * to get the udp received
     */
    private UDPClientStringManager listen;

    public static final String FOLDER_PATH_TO_SAVE_DOWNLOAD_DEFAULT
            = "download";

    public static final int SECONDS_TO_UPDATE_FILE_LIST_DEFAULT = 45;
    public static final int SECONDS_TO_RECEIVE_UDP_DEFAULT = 1;

    /**
     *
     * @param clientAddress
     * @param generateTcp true will generate as personal TCP port
     */
    public Client(Address clientAddress, boolean generateTcp) {
        serverTCPListenPort = -1;
        idClient = clientCount;
        clientCount++;
        this.clientAddress = clientAddress;
        this.filesInDownloadList = new ArrayList<>();
        /**
         * this will create an usable/personal TCP port for the client
         */
        if (generateTcp) {
            this.clientAddress.setTcpPort(returnTcpPort());
        }
        secondsToUpdateList = SECONDS_TO_UPDATE_FILE_LIST_DEFAULT;

        if (Utils.validadeFileCreateIfMissing(FOLDER_PATH_TO_SAVE_DOWNLOAD_DEFAULT, false, true)) {
            folderToDownloadPath = new File(FOLDER_PATH_TO_SAVE_DOWNLOAD_DEFAULT);
        }
        filesDownloaded = filesDownloaded();
        serverAddress = new Address(IpProtocol.IPV4, "Not known yet",
                "Not known yet", "Not known yet", -22, -22);
        run();
    }

    public Client(Address clientAddress, File folderToDownloadPath) {
        serverTCPListenPort = -1;
        idClient = clientCount;
        clientCount++;
        this.clientAddress = clientAddress;
        this.filesInDownloadList = new ArrayList<>();
        secondsToUpdateList = SECONDS_TO_UPDATE_FILE_LIST_DEFAULT;
        this.folderToDownloadPath = folderToDownloadPath;
        filesDownloaded = filesDownloaded();
        serverAddress = new Address(IpProtocol.IPV4, "Not known yet",
                "Not known yet", "Not known yet", -22, -22);
        run();
    }

    public Client(Address clientAddress, File folderToDownloadPath, int secondsToUpdateList) {
        serverTCPListenPort = -1;
        idClient = clientCount;
        clientCount++;
        this.clientAddress = clientAddress;
        this.filesInDownloadList = new ArrayList<>();
        this.secondsToUpdateList = secondsToUpdateList;
        this.folderToDownloadPath = folderToDownloadPath;
        filesDownloaded = filesDownloaded();
        serverAddress = new Address(IpProtocol.IPV4, "Not known yet",
                "Not known yet", "Not known yet", -22, -22);
    }

    private boolean run() {
        receiveUDPAnnoucement();
        updateFileListTimed();

        return true;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Address getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(Address clientAddress) {
        this.clientAddress = clientAddress;
    }

    public int getSecondsToUpdateList() {
        return secondsToUpdateList;
    }

    public void setSecondsToUpdateList(int secondsToUpdateList) {
        this.secondsToUpdateList = secondsToUpdateList;
    }

    public File getFolderToDownloadPath() {
        return folderToDownloadPath;
    }

    public void setFolderToDownloadPath(File folderToDownloadPath) {
        this.folderToDownloadPath = folderToDownloadPath;
    }

    public int getClientCount() {
        return clientCount;
    }

    public ArrayList<String> getFilesInDownloadList() {
        return filesInDownloadList;
    }

    public Address getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(Address serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerTCPListenPort() {
        return serverTCPListenPort;
    }

    public void setServerTCPListenPort(int serverTCPListenPort) {
        this.serverTCPListenPort = serverTCPListenPort;
    }

    /**
     * to show to the user without the file size in bytes
     *
     * @return
     */
    public ArrayList<String> getFilesInDownloadListCleaned() {
        ArrayList<String> toReturn = new ArrayList<>();
        String toAdd = "";
        for (String cicle : filesInDownloadList) {
            toAdd = cicle.replaceAll(AnnouncementUDP.fileSizeSeparator[0] + "[0-9]*\\" + AnnouncementUDP.fileSizeSeparator[1], "");
            //toAdd.trim();
            toReturn.add(toAdd);
            //System.out.println("@@@@@ --> " + toAdd);
        }
        return toReturn;
    }

    public void setFilesInDownloadList(ArrayList<String> filesInDownloadList) {
        this.filesInDownloadList = filesInDownloadList;
    }

    public FileList getFilesDownloaded() {
        return filesDownloaded;
    }

    public void setFilesDownloaded(FileList filesDownloaded) {
        this.filesDownloaded = filesDownloaded;
    }

    /**
     * RECEVES a string like: 192.168.1.102;192.168.1.255;0;Destroi
     * Salsicha.mp4«410224»; Puppy Photo.jpg«171402»;doggy Video.mp4«1073391»;
     * ficheiroLusiadas1.txt«297»;ficheiroLusiadas2.txt«302»;ficheiroLusiadas3.txt«287»;
     * ficheiroLusiadas4.txt«243»;ficheiroLusiadas5.txt«290»;ficheiroLusiadas6.txt«199»;
     * ficheiroLusiadas7.txt«290»;ficheiroLusiadas8.txt«238»;ficheiroLusiadas9.txt«297»;nao
     * gosto nada de gatos.jpg«19303»;spam.jpg«13310»;
     *
     *
     * updates file list with that data [only information in index 3 - infinity
     * will be reviewed]
     *
     * @param announcementFromUDP
     * @return
     */
    @Override
    public boolean updateFileList(String announcementFromUDP) {
        String[] fileNames = announcementFromUDP.split(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT);
        String toAdd = "";
        filesInDownloadList = new ArrayList<>();
        /**/
        if (fileNames.length < 3) {
            /*UDP sented without files*/
            return false;
        }
        for (int i = 3; i < fileNames.length; i++) {
            toAdd = fileNames[i];
            filesInDownloadList.add(toAdd);
        }

        return true;
    }

    /**
     * RECEVES a string like: 192.168.1.102;192.168.1.255;0;Destroi
     * Salsicha.mp4«410224»; Puppy Photo.jpg«171402»;doggy Video.mp4«1073391»;
     * ficheiroLusiadas1.txt«297»;ficheiroLusiadas2.txt«302»;ficheiroLusiadas3.txt«287»;
     * ficheiroLusiadas4.txt«243»;ficheiroLusiadas5.txt«290»;ficheiroLusiadas6.txt«199»;
     * ficheiroLusiadas7.txt«290»;ficheiroLusiadas8.txt«238»;ficheiroLusiadas9.txt«297»;nao
     * gosto nada de gatos.jpg«19303»;spam.jpg«13310»;
     *
     *
     *
     * @return
     */
    private void updateServerAddress(String announcementFromUDP) {
        String[] splitted = announcementFromUDP.split(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT);
        if (Utils.validateAddress(splitted[0], IpProtocol.IPV4.toString())
                && Utils.validateAddress(splitted[1], IpProtocol.IPV4.toString())) {
            Address a = new Address(IpProtocol.IPV4, splitted[0],
                    splitted[0], splitted[1], clientAddress.getUdpPort(), clientAddress.getTcpPort());
            setServerAddress(a);
            int portTcp = Integer.parseInt(splitted[2]);
            setServerTCPListenPort(portTcp);
        }

    }

    public boolean updateFilesDownloaded() {
        filesDownloaded = filesDownloaded();
        return true;
    }

    /**
     *
     * the list of files downloaded by the client
     *
     * @return
     */
    private FileList filesDownloaded() {
        File[] listOfFiles = folderToDownloadPath.listFiles();
        ArrayList<File> usableFiles = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                usableFiles.add(listOfFiles[i]);
            } else if (listOfFiles[i].isDirectory()) {
                /*IGNORE*/
            }
        }
        Collections.sort(usableFiles);
        return new FileList(usableFiles);
    }

    /**
     *
     * returns the number o tcp port that is going to be used by that element
     *
     * @return
     */
    public int returnTcpPort() {
        int tcpPort = 0;
        String ipAddress = utils.Utils.getMachineIpAddress("ipv4");
        tcpPort = utils.Utils.sumOfAllIpv4Digits(ipAddress) + FileConnectionTCP.MINIMUM_PORT_NUMBER_DEFAULT;

        return (tcpPort > FileConnectionTCP.MINIMUM_PORT_NUMBER_DEFAULT)
                ? tcpPort : FileConnectionTCP.MINIMUM_PORT_NUMBER_DEFAULT;
    }

    /**
     *
     * @param nodeToSendAddress
     * @param fileList
     * @return
     */
    public boolean sendDownloadRequestTcp(String nodeToSendAddress, ArrayList<String> fileList) {
        int tcpPort = returnTcpPort();
        getClientAddress().setTcpPort(tcpPort);
        Address addressToSendTcp = new Address(IpProtocol.IPV4, nodeToSendAddress,
                clientAddress.getBroadcastAddress(), clientAddress.getMaskAddress(),
                ConnectionUDP.PORT_UDP_FOR_CLIENT_CONTACT_SERVER_DEFAULT, tcpPort);
        AnnouncementUDP announcementUDP
                = new AnnouncementUDP(this.clientAddress, addressToSendTcp, new ArrayList<>(), idClient);
        String toSend = announcementUDP.writeAnnoucementToSendDownloadRequest(fileList, clientAddress.getTcpPort());

        utils.Utils.print("###--> Client --> Download request!! Id: " + idClient + "  Ip address: " + clientAddress.getAddress());
        utils.Utils.print("###--> Client --> UDP To send: " + toSend);
        TextConnectionTCP cPClientSender = new TextConnectionTCP(serverTCPListenPort);
        cPClientSender.tcpSenderSendMessage(toSend, addressToSendTcp);
        downloadFileAndUnzip(Utils.ZIPPED_FILE_NAME_DEFAULT, addressToSendTcp);
        return true;
    }

    /**
     *
     */
    private void receiveUDPAnnoucement() {
        /**
         * listens from server 32011
         */
        ConnectionUDP connectionUDP = new ConnectionUDP(ConnectionUDP.PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT);
        listen = connectionUDP.clientStartReceiveMessage(this);

    }

    /**
     *
     * @return
     */
    private boolean updateFileListTimed() {
        /*amount of time to the run method be executed again*/
        int timeInterval = Utils.secondsToMiliseconds(SECONDS_TO_UPDATE_FILE_LIST_DEFAULT, SECONDS_TO_RECEIVE_UDP_DEFAULT);
        Timer timerToSendUdp = new Timer();
        timerToSendUdp.schedule(new TimerTask() {
            @Override
            public void run() {
                String received = listen.getReceivedString();
                if (received.contains(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT)
                        && received.contains(AnnouncementUDP.fileSizeSeparator[0])
                        && received.contains(AnnouncementUDP.fileSizeSeparator[1])) {
                    Utils.print("--> UDP RECEIVED --> CLIENT --> \n\nMESSAGE_CLIENT_RECEIVED = " + received);
                    updateFileList(received);
                    updateServerAddress(received);
                }

            }

        }, 0, timeInterval);

        return true;
    }

    public boolean downloadFileAndUnzip(String fileName, Address serverAddress) {
        File fileF = new File(this.folderToDownloadPath.getAbsolutePath() + "/" + fileName);
        //System.out.println(toReturn.getAbsolutePath());
        FileConnectionTCP cP = new FileConnectionTCP(clientAddress.getTcpPort());
        try {
            Thread.sleep(Utils.secondsToMiliseconds(5, 20));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        File file = cP.receiveFile(fileF.getAbsolutePath(), serverAddress);
        Utils.fileUnziper(file.getAbsolutePath(), this.folderToDownloadPath.getAbsolutePath(), true);
        Utils.removeFile(file.getAbsolutePath());
        updateFilesDownloaded();
        return true;
    }

}
