/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import fr.nargilcorp.jadosocnet.message.StringMessage;

/**
 *
 * @author antony
 */
public class PingPongAgent extends AbstractAgent{

    @Override
    protected void alive() {
        requestRole("co", "co", "co");
        broadcastMessage("co", "co", "co", new StringMessage("ping"));
        System.out.println("L'agent dit : ping");
        int i = 0;
        while(i < 10){
            StringMessage m = (StringMessage) waitMessage();
            sendMessage(m.getSender(), new StringMessage("Pong"));
            System.out.println("L'agent dit : pong");
            m = (StringMessage) waitMessage();
            sendMessage(m.getSender(), new StringMessage("Ping"));
            System.out.println("L'agent dit : ping");
            i++;
        }
    }
    
}
