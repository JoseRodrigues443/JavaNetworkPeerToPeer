/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import peerToPeer.ClientForPeerToPeer;

/**
 *
 * @author arch-admin
 */
public class PeerToPeerController {
    private ClientForPeerToPeer peer;

    public PeerToPeerController(ClientForPeerToPeer peer) {
        this.peer = peer;
    }

    public ClientForPeerToPeer getPeer() {
        return peer;
    }

    public void setPeer(ClientForPeerToPeer peer) {
        this.peer = peer;
    }
    
    
    
}
