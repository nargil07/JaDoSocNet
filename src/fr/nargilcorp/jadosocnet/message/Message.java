package fr.nargilcorp.jadosocnet.message;

import fr.nargilcorp.jadosocnet.agent.AdresseAgent;
import java.io.Serializable;

/**
 *
 * @author antony
 * @param <T>
 */
public class Message implements Serializable{
    private AdresseAgent sender, receiver;

    public AdresseAgent getSender() {
        return sender;
    }

    public void setSender(AdresseAgent sender) {
        this.sender = sender;
    }

    public AdresseAgent getReceiver() {
        return receiver;
    }

    public void setReceiver(AdresseAgent receiver) {
        this.receiver = receiver;
    }
}
