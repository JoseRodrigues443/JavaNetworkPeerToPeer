/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkElements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class TextConnectionTCP {

    private int tcpPort;
    private ServerSocket s;
    /**
     * 
     * @param tcpPort 
     */
    public TextConnectionTCP(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    /**
     * starts
     *
     * @param server
     * @return
     */
    public TCPServerReceiver tcpListener(Server server) {
        TCPServerReceiver a = null;
        
        try {
            s = new ServerSocket(0);
            tcpPort = s.getLocalPort();    // returns the port the system selected
            a = new TCPServerReceiver(s, server);
            a.start();
        } catch (IOException ex) {
            Logger.getLogger(TextConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return a;
    }

    /**
     * starts
     *
     * @param messageToSend
     * @param addresToSend
     */
    public void tcpSenderSendMessage(String messageToSend, Address addresToSend) {
        TCPClientSender a = new TCPClientSender(messageToSend);
        a.sendMessage(addresToSend);
    }

    /**
     * receives a message from the client
     */
    public class TCPServerReceiver extends Thread {

        private String message;
        private final Server server;

        public TCPServerReceiver(ServerSocket s, Server server) {
            this.server = server;
            message = "";
            tcpPort = s.getLocalPort();
        }

        public String getMessage() {
            return message;
        }

        public int getTcpPort() {
            return tcpPort;
        }

        /**
         *
         */
        @Override
        public void run() {
            try {
                
                while (true) {
                    Socket connectionSocket = s.accept();
                    BufferedReader inFromClient
                            = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                    message = inFromClient.readLine();
                    System.out.println("»»» Received: " + message);
                    String[] splited = message.split(ConnectionUDP.MESSAGE_SEPARATOR_DEFAULT);
                        int tcpValue = Integer.valueOf(splited[3]);
                        Address a = new Address(IpProtocol.IPV4, splited[1],
                                server.getServerAddress().getMaskAddress(),
                                server.getServerAddress().getBroadcastAddress(),
                                server.getServerAddress().getUdpPort(), tcpValue);
                        server.getServerAddress().setTcpPort(tcpValue);
                        server.createLogAndAddItToList(a, message, tcpValue);
                        server.sendTCP(message, tcpValue, a);
                }
            } catch (IOException ex) {
                Logger.getLogger(TextConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * sends a message to server
     */
    public class TCPClientSender {

        private final String messageToSend;

        public TCPClientSender(String messageToSend) {
            this.messageToSend = messageToSend;
        }

        public void sendMessage(Address addressToSend) {
            try {
                /**
                 * open socket
                 */
                Socket clientSocket = new Socket(addressToSend.getAddress(), tcpPort);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                
                outToServer.writeBytes(messageToSend + '\n');
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(TextConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
