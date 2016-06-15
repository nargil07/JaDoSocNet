/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import fr.nargilcorp.jadosocnet.message.Message;
import fr.nargilcorp.jadosocnet.message.StringMessage;

/**
 *
 * @author antony
 */
public class WritterAgent extends AbstractAgent {

    @Override
    protected void alive() {
        requestRole("test", "test", "test");
        broadcastMessage("test", "test", "test", new StringMessage("plop"));
        Message m = waitMessage();
        System.out.println(((StringMessage)m).getContent());
    }

}
