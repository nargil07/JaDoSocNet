/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import fr.nargilcorp.jadosocnet.message.StringMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class BasicAgent extends AbstractAgent implements Runnable {

    @Override
    protected void alive() {
        requestRole("test", "test", "test");
        try {
            StringMessage message = (StringMessage) waitMessage();
            System.out.println("nouveau message : " + message.getContent());
            sendMessage(message.getSender(), new StringMessage("je te répond"));
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BasicAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
