/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 *
 * @author antony
 */
public class AdresseAgent implements Serializable{
    private final String address;
    private final int port;

    public AdresseAgent(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
    
    
    
    
}
