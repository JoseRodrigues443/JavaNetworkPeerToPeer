/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filetransfer;

import client.Client;
import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkElements.Address;
import networkElements.ConnectionUDP;
import networkElements.IpProtocol;
import peerToPeer.ClientForPeerToPeer;
import server.Server;
import ui.ArranqueUi;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class FileTransfer {

    /*
    
        Title: Peer-to-peer file download
This project consists of developing a network application, either in C or
Java language. It’s a peer-to-peer network application, this means it’s a
single application, but several instances of it will communicate with
each other.
The application main objective is providing download access to some local
files. Once it starts it will periodically announce to the network a list
of local filenames it is willing to serve.
Other application instances, will listen for these announcements, and
build a list of available filenames on the network. The application’s
user may then select one available file to be downloaded. 
    
     */
    /**
     * args[0]: "-client" "-server" args[1]:
     *
     * ip address
     *
     * args[2]:
     *
     * network mask
     *
     * args[3]:
     *
     * others
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Utils.removeFile(Client.FOLDER_PATH_TO_SAVE_DOWNLOAD_DEFAULT + Utils.ZIPPED_FILE_NAME_DEFAULT);
        Utils.removeFile(Server.FOLDER_PATH_TO_SHARE_DEFAULT + Utils.ZIPPED_FILE_NAME_DEFAULT);
        Utils.setLookAndFeel("autodetect");
        String appType = "empty";
        /**
         * if (args.length <= 0) { System.out.println("\nBem vindo ao
         * FILE_SHARE_ISEP 1.0\n" + "\nVocê é um servidor (partilha ficheiros)."
         * + "\n\nOu um cliente? (recebe ficheiros)" +
         * "\n\n\n___________________________\n\n" + "s) Servidor" + "\nc)
         * Cliente"); Scanner input = new Scanner(System.in); boolean
         * correctImput; do { appType = input.nextLine(); if
         * (appType.equalsIgnoreCase("s")) { appType = "-server"; correctImput =
         * true; } else if (appType.equalsIgnoreCase("c")) { appType =
         * "-client"; correctImput = true; } else { correctImput = false; } }
         * while (correctImput == false);
         *
         * } else { /*Com argumentos appType = args[0]; }
         */

        if (!(args.length <= 0)) {
            appType = args[0];
        }

        if (appType.equals(
                "-server")) {
            Utils.print("\n--> Você é um servidor");
            new ui.ServerMenuUI(createServer());
        } else if (appType.equals(
                "-client")) {
            Utils.print("\n--> Você é um cliente");
            new ui.ClientMenuUI(createClient());
        } else {
            Utils.print("--> Startup program activeted");
            new ArranqueUi();
        }
    }

    public static Server createServer() {
        String address = Utils.getMachineIpAddress(IpProtocol.IPV4.toString());
        String maskAddress = Utils.getMachineMaskAddressIpv4();
        String broadcastAddress = Utils.getMachineBroadcastAddressIpv4();
        Address serverAddress = new Address(IpProtocol.IPV4, address,
                maskAddress, broadcastAddress,
                ConnectionUDP.PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT,
                -1);
        return new Server(serverAddress, true);
    }

    public static Client createClient() {
        String address = Utils.getMachineIpAddress(IpProtocol.IPV4.toString());
        String maskAddress = Utils.getMachineMaskAddressIpv4();
        String broadcastAddress = Utils.getMachineBroadcastAddressIpv4();
        Address clientAddress = new Address(IpProtocol.IPV4, address,
                maskAddress, broadcastAddress,
                ConnectionUDP.PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT,
                -1);
        return new Client(clientAddress, true);
    }
}
