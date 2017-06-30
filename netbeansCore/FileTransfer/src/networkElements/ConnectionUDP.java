/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkElements;

import client.ClientInterface;
import java.io.*;
import java.net.*;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues -
 * 1150710 Tiago Vilaça - 1140412
 */
public class ConnectionUDP {

    private final int portNumber;

    private static final int BYTE_SIZE_DEFAULT = 2048;
    /**
     * for server to clients
     */
    public static final int PORT_UDP_FOR_SERVER_UPLOAD_DEFAULT = 32011;
    /**
     * for clients to server
     */
    public static final int PORT_UDP_FOR_CLIENT_CONTACT_SERVER_DEFAULT = 32012;
    public static final String MESSAGE_SEPARATOR_DEFAULT = ";";

    public ConnectionUDP(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     *
     * @param toSend
     * @param clientAddress
     * @return
     */
    public boolean serverSendMessage(String toSend, Address clientAddress) {
        UDPServerStringManager dPServerStringManager
                = new UDPServerStringManager(toSend, clientAddress);

        return dPServerStringManager.send();
    }

    /**
     *
     * @param c
     * @return
     */
    public UDPClientStringManager clientStartReceiveMessage(ClientInterface c) {
        UDPClientStringManager clientStringManager
                = new UDPClientStringManager(c);
        clientStringManager.start();
        return clientStringManager;
    }

    public class UDPServerStringManager {

        /**
         * client address to send , or broadcast address
         */
        private final Address clientAddress;
        private final String messageToSend;
        private static final String MESSAGE_TO_SEND_DEFAULT = "No message inserted";

        /**
         *
         * @param messageToSend
         * @param clientAddress
         *
         */
        public UDPServerStringManager(String messageToSend, Address clientAddress) {
            if (!messageToSend.isEmpty()) {
                this.messageToSend = messageToSend;
            } else {
                this.messageToSend = MESSAGE_TO_SEND_DEFAULT;
            }
            this.clientAddress = clientAddress;
        }

        public boolean send() {
            try {
                String host = clientAddress.getAddress();
                if (host.equals("172.0.0.2")) {
                    host = "172.0.0.1";
                }
                int port = portNumber;

                /**
                 * String to send
                 */
                byte[] message = messageToSend.getBytes();
                utils.Utils.print("--> Port: " + portNumber + " -->To send " + messageToSend);

                // Get the internet address of the specified host
                InetAddress address = InetAddress.getByName(host);

                // Initialize a datagram packet with data and address
                DatagramPacket packet = new DatagramPacket(message, message.length,
                        address, port);

                try {
                    // Create a datagram socket, send the packet through it, close it.
                    DatagramSocket dsocket = new DatagramSocket();
                    dsocket.send(packet);
                } catch (IOException e) {
                    utils.Utils.print(e.toString());
                    return false;
                }
            } catch (IOException e) {
                System.err.println(e);
                return false;
            }

            return true;
        }
    }

    public class UDPClientStringManager extends Thread {

        private String messageUDP;
        ClientInterface c;

        public UDPClientStringManager(ClientInterface c) {
            this.c = c;
            messageUDP = "";
        }

        public String getReceivedString() {
            return messageUDP;
        }

        /**
         * this will work in a diferent tread
         *
         *
         * IS enabled by the "start()" command
         *
         */
        @Override
        public void run() {
            String toReturn;
            try {
                int port = portNumber;

                // Create a socket to listen on the port.
                DatagramSocket dsocket = new DatagramSocket(port);

                // Create a buffer to read datagrams into. If a
                // packet is larger than this buffer, the
                // excess will simply be discarded!
                byte[] buffer = new byte[BYTE_SIZE_DEFAULT];

                // Create a packet to receive data into the buffer
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Now loop forever, waiting to receive packets and printing them.
                while (true) {
                    utils.Utils.print("--> UDP Tread PORT: " + portNumber + " --> Waiting for UDP package...");
                    // Wait to receive a datagram
                    dsocket.receive(packet);

                    // Convert the contents to a string, and display them
                    toReturn = new String(buffer, 0, packet.getLength());
                    //System.out.println(packet.getAddress().getHostName() + ": "  + msg);s
                    if (toReturn.contains(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT)) {
                        Utils.print("#####--> UDP Tread --> RECEIVED!!!!!!!!!  -->  " + toReturn);
                        messageUDP = toReturn;
                        c.updateFileList(toReturn);
                    }

                    // Reset the length of the packet before reusing it.
                    packet.setLength(buffer.length);
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            this.interrupt();
        }
    }

}
