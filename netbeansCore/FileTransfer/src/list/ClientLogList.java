/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package list;

import java.util.ArrayList;
import server.ClientLog;
import utils.Settings;

/**
 *
 * @author João Fernandes - 1141114 José Barros - 1151117 José Rodrigues - 1150710 Tiago Vilaça - 1140412
 */
public class ClientLogList {

    private ArrayList<ClientLog> clientLogList;

    public ClientLogList() {
        clientLogList = new ArrayList<>();
    }

    public ClientLogList(ArrayList<ClientLog> clientLogList) {
        this.clientLogList = clientLogList;
    }

    public ArrayList<ClientLog> getClientLogList() {
        return clientLogList;
    }

    public void setClientLogList(ArrayList<ClientLog> clientLogList) {
        this.clientLogList = clientLogList;
    }

    /**
     * 
     * @param e
     * @return 
     */
    public boolean add(ClientLog e) {
        if (Settings.isCreateLogs()){
            return clientLogList.add(e);
        }
        return false;
    }

    /**
     * May be used to export to csv
     *
     * @return
     */
    public String[][] logMatrix() {
        /**
         * private int id; private Address clientAddress; private FileList
         * downloadedFiles; private Date date;private int tcpPort;
         *
         *
         */
        int defaultParametersNumber = 5;
        String[][] stringMatrix = new String[clientLogList.size()][defaultParametersNumber];
        /**
         * stringMatrix[0][0] = "Client ID"; stringMatrix[0][1] = "IP Address";
         * stringMatrix[0][2] = "TCP port"; stringMatrix[0][3] = "Files
         * Downloaded"; stringMatrix[0][4] = "Date";
         */

        for (int i = 0; i < clientLogList.size(); i++) {
            stringMatrix[i][0] = "" + clientLogList.get(i).getId();
            stringMatrix[i][1] = clientLogList.get(i).getClientAddress().getAddress();
            stringMatrix[i][2] = "" + clientLogList.get(i).getTcpPort();
            stringMatrix[i][3] = clientLogList.get(i).listFiles();
            stringMatrix[i][4] = clientLogList.get(i).dateFormat();

        }

        return stringMatrix;
    }

    public String[] tableColumnNames() {
        String[] toReturn = {"Client ID", "IP Address", "TCP port", "Files Downloaded", "Date"};
        return toReturn;
    }

    public ArrayList<String> logLines() {
        ArrayList<String> list = new ArrayList<>();
        for (ClientLog cl : clientLogList) {
            list.add("Id: " + cl.getId() + " | " + cl.getClientAddress().getAddress() + " | " + cl.dateFormat());
        }
        return list;
    }
}
