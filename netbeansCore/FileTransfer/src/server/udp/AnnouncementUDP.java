/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.udp;

import java.io.File;
import java.util.ArrayList;
import networkElements.Address;
import networkElements.ConnectionUDP;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class AnnouncementUDP {

    /**
     * server IP
     */
    private final Address senderAddress;
    /**
     * client (or broadcast) ip
     */
    private final Address receiverAddress;
    private final ArrayList<File> files;

    /**
     * sender id, aka server
     */
    private final int computerId;
    /**
     * {"@", "$"}; for file size separator
     */
    public static String fileSizeSeparator[] = {"@", "$"};

    /**
     *
     * @param senderAddress
     * @param receiverAddress
     * @param files
     * @param computerId
     */
    public AnnouncementUDP(Address senderAddress, Address receiverAddress, ArrayList<File> files, int computerId) {
        this.senderAddress = senderAddress;
        this.receiverAddress = receiverAddress;
        this.files = files;
        this.computerId = computerId;
        /*if the anoucement is send by the server, you will know that id, if is the client you will also know*/
    }

    /**
     *
     * return format:
     *
     * "sender_address";"receiver_address";"COMPUTER_id";"file_name_1"«size»;"file_name_2"«size»;"file_name_3"«size»;"file_name_N"«size»;
     *
     *
     * EX:
     *
     * 1192.168.1.102;192.168.1.255;0;Destroi Salsicha.mp4«410224»;Puppy
     * Photo.jpg«171402»;doggy
     * Video.mp4«1073391»;ficheiroLusiadas1.txt«297»;ficheiroLusiadas2.txt«302»;
     * ficheiroLusiadas3.txt«287»;ficheiroLusiadas4.txt«243»;ficheiroLusiadas5.txt«290»;
     * ficheiroLusiadas6.txt«199»;ficheiroLusiadas7.txt«290»;ficheiroLusiadas8.txt«238»;
     * ficheiroLusiadas9.txt«297»;nao gosto nada de
     * gatos.jpg«19303»;spam.jpg«13310»;
     *
     * @return
     */
    public String writeAnnoucementToSendFileListUpdate() {

        String messageToSend = "" + senderAddress.getAddress() + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT
                + receiverAddress.getAddress() + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT
                + computerId + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT;
        ;
        if (files.isEmpty() && files == null) {
            messageToSend += ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT;
            /*it will result in the case ";;", equivalent to null*/
        } else {
            for (File file : files) {
                String filebytes = utils.Utils.fileBytesOfFile(file);
                messageToSend += file.getName() + fileSizeSeparator[0]
                        + filebytes + fileSizeSeparator[1]
                        + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT;
            }
        }

        return messageToSend;
    }

    /**
     *
     * return format:
     *
     * "sender_address";"receiver_address";"COMPUTER_id";"portNumber";"file_name_1";"file_name_2";"file_name_3";"file_name_N";
     *
     *
     * EX:
     *
     * 192.168.0.1;192.168.0.3;21;12000;banana.txt;how_to_build_a_spaceship_for_dummies.txt;pao.txt;muhahaha.txt;
     *
     * @param arrayListFile
     * @return
     */
    public String writeAnnoucementToSendDownloadRequest(ArrayList<String> arrayListFile, int tcpPort) {
        String messageToSend = senderAddress.getAddress() + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT
                + receiverAddress.getAddress() + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT
                + computerId + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT
                + tcpPort + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT;
        ;
        if (arrayListFile == null) {

        } else {
            if (arrayListFile.isEmpty()) {
                messageToSend += ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT;
                /*it will result in the case ";;", equivalent to null*/
            } else {
                for (String file : arrayListFile) {
                    messageToSend += file + ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT;
                }
            }
        }
        return messageToSend;
    }

}
