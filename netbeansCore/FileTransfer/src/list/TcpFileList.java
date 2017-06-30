/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package list;

import java.util.ArrayList;
import peerToPeer.TcpFiles;
import utils.Utils;

/**
 *
 * @author arch-admin
 */
public class TcpFileList {

    private ArrayList<TcpFiles> tcpFileses;
    private int machineTcpPort;

    public TcpFileList() {
        tcpFileses = new ArrayList<>();
        machineTcpPort = -1;
    }

    public TcpFileList(ArrayList<TcpFiles> tcpFileses, int machineTcpPort) {
        this.tcpFileses = tcpFileses;
        this.machineTcpPort = machineTcpPort;
    }

    public TcpFileList(int machineTcpPort) {
        this.tcpFileses = new ArrayList<>();
        this.machineTcpPort = machineTcpPort;
    }

    public int getMachineTcpPort() {
        return machineTcpPort;
    }

    public void setMachineTcpPort(int machineTcpPort) {
        this.machineTcpPort = machineTcpPort;
    }
    

    public ArrayList<TcpFiles> getTcpFileses() {
        return tcpFileses;
    }

    public void setTcpFileses(ArrayList<TcpFiles> tcpFileses) {
        this.tcpFileses = tcpFileses;
    }

    public ArrayList<String> getFileListByTcpPort(int tcpPort) {
        for (TcpFiles f : tcpFileses) {
            if (f.getTcpPort() == tcpPort) {
                return f.getFileListCleaned();
            }
        }
        return null;
    }

    public TcpFiles getByTcpPort(int tcpPort) {
        for (TcpFiles f : tcpFileses) {
            if (f.getTcpPort() == tcpPort) {
                return f;
            }
        }
        return null;
    }

    public ArrayList<String> getAllTcpPort() {
        ArrayList<String> toReturn = new ArrayList<>();
        for (TcpFiles f : tcpFileses) {
            toReturn.add("" + f.getTcpPort());
        }
        return toReturn;
    }

    public ArrayList<Integer> getAllTcpPortNumeric() {
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (TcpFiles f : tcpFileses) {
            toReturn.add(f.getTcpPort());
        }
        return toReturn;
    }

    public boolean updateFileList(int tcpPort, ArrayList<String> s) {
        for (TcpFiles f : tcpFileses) {
            if (f.getTcpPort() == tcpPort) {
                f.setFileList(s);
                return true;
            }
        }
        return false;
    }

    public boolean cointainsTcp(int tcpPort) {
        for (TcpFiles f : tcpFileses) {
            if (f.getTcpPort() == tcpPort) {
                return true;
            }
        }
        return false;
    }

    public boolean addValidated(TcpFiles e) {
        /**
         * if alreasy exists or it the own machine port
         */
        if (cointainsTcp(e.getTcpPort())
                || e.getTcpPort() == this.machineTcpPort) {
            return false;
        }
        Utils.print("▓▓▓▓▓▓▓ --> added tcpFiles to tcpFilesList »» " +e.getTcpPort() );
        return tcpFileses.add(e);
    }

    @Override
    public String toString() {
        return "TcpFileList{" + "tcpFileses=" + tcpFileses + '}';
    }

}
