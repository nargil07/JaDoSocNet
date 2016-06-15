/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.message;

/**
 *
 * @author antony
 */
public class StringMessage extends ObjectMessage<String>{

    public StringMessage(final String message) {
        this.setContent(message);
    }
}
