/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peerToPeer;

import controller.ServerController;
import static java.awt.image.ImageObserver.ABORT;
import networkElements.Address;
import networkElements.ConnectionUDP;
import networkElements.IpProtocol;
import server.Server;
import ui.PeerToPeerMenuUI;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues -
 * 1150710 Tiago Vilaça - 1140412
 */
public class PeerToPeer {

    /**
     * A peerToPeer is both an client and a server
     */
    public PeerToPeer() {
    }

    public void run() {
        Server s = filetransfer.FileTransfer.createServer();
        ServerController serverController = new ServerController(s);
        String broadcast = serverController.getServerAddress().getBroadcastAddress();
        if (utils.Utils.validateAddress(broadcast, "ipv4")) {
            serverController.setAddressToSendUdp(new Address(IpProtocol.IPV4, broadcast,
                    serverController.getServerAddress().getMaskAddress(),
                    serverController.getServerAddress().getBroadcastAddress(), ABORT, ABORT));
        }

        /**
         * Server off, turn on
         */
        serverController.turnOnServer();
        String address = Utils.getMachineIpAddress(IpProtocol.IPV4.toString());
        String maskAddress = Utils.getMachineMaskAddressIpv4();
        String broadcastAddress = Utils.getMachineBroadcastAddressIpv4();
        Address clientAddress = new Address(IpProtocol.IPV4, address,
                maskAddress, broadcastAddress,
                ConnectionUDP.PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT,
                -1);

        ClientForPeerToPeer c = new ClientForPeerToPeer(clientAddress, true, s);
        //new ClientMenuUI(c);
        new PeerToPeerMenuUI(c);
    }
}
