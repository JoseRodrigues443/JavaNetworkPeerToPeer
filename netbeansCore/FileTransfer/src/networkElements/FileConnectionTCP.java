/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkElements;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class FileConnectionTCP {

    private final int portNumberTCP;

    /**
     * minimum port number that garanties that isnt being used by protocols like
     * http, dns, ssh, etc
     */
    public static final int MINIMUM_PORT_NUMBER_DEFAULT = 9999;

    public FileConnectionTCP(int portNumber) {
        this.portNumberTCP = portNumber;
    }

    public boolean sendFile(File filesToSend) {

        SenderTCP senderTCP = new SenderTCP(filesToSend);
        senderTCP.sendFile();
        return true;
    }

    public File receiveFile(String location, Address addressToSend) {
        ReceiverTCP rtcp = new ReceiverTCP(location);
        return rtcp.receiveFile(addressToSend);
    }

    /**
     * server that send tcp file
     */
    public class SenderTCP {

        File fileToSend;

        public SenderTCP(File fileToSend) {
            this.fileToSend = fileToSend;
        }

        public boolean sendFile() {
            boolean continueWhile = true;
            while (continueWhile) {
                Utils.print("Waiting for tcp request to send file...");
                try {
                    //Initialize Sockets
                    ServerSocket ssock = new ServerSocket(portNumberTCP);
                    //System.out.println("Port = " + portNumberTCP);
                    //System.out.println("File = " + fileToSend.getAbsolutePath());

                    /**
                     * it blocks until an connection in made
                     */
                    Socket socket = ssock.accept();
                    FileInputStream fis = new FileInputStream(fileToSend);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    //Get socket's output stream
                    OutputStream os = socket.getOutputStream();

                    //Read File Contents into contents array
                    byte[] contents;
                    long fileLength = fileToSend.length();
                    long current = 0;

                    long start = System.nanoTime();
                    while (current != fileLength) {
                        int size = 10000;
                        if (fileLength - current >= size) {
                            current += size;
                        } else {
                            size = (int) (fileLength - current);
                            current = fileLength;
                        }
                        contents = new byte[size];
                        bis.read(contents, 0, size);
                        os.write(contents);
                        Utils.print("Sending file ... " + (current * 100) / fileLength + "% complete!");
                    }

                    os.flush();
                    //File transfer done. Close the socket connection!
                    socket.close();
                    ssock.close();
                    Utils.print("File sent succesfully!");
                    continueWhile = false;

                    return true;
                } catch (IOException ex) {
                    Logger.getLogger(FileConnectionTCP.class.getName()).log(Level.SEVERE, null, ex);
                    Utils.print("Error!!! TCP file error\n--> :" + ex.toString() + "\n--> --> Port: " + portNumberTCP);
                    return false;
                }

            }
            return true;
        }

    }

    public class ReceiverTCP {

        String locationToSaveFile;

        public ReceiverTCP(String locationToSave) {
            this.locationToSaveFile = locationToSave;
        }

        public File receiveFile(Address addressOfFileServer) {
            File toReturn = null;
            //Initialize socket, for connect to server address and tcp port
            try {
                Socket socket;
                /**
                 * request for server and address and port
                 */
                socket = new Socket(addressOfFileServer.getAddress(), portNumberTCP);
                byte[] contents = new byte[10000];

                //Initialize the FileOutputStream to the output file's full path.
                FileOutputStream fos = new FileOutputStream(locationToSaveFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                InputStream is = socket.getInputStream();

                //No of bytes read in one read() call
                int bytesRead = 0;

                while ((bytesRead = is.read(contents)) != -1) {
                    bos.write(contents, 0, bytesRead);
                }
                bos.flush();
                toReturn = new File(locationToSaveFile);
                Utils.print("!!!!! --> #### --> File saved successfully!  »»»» " + toReturn.getAbsolutePath());
            } catch (IOException e) {
                Logger.getLogger(FileConnectionTCP.class.getName()).log(Level.SEVERE, null, e);
                Utils.print("--> ERROR: " + e.toString());
            }

            return toReturn;
        }

    }
}
