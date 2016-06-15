/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.message;

import java.io.Serializable;

/**
 *
 * @author antony
 * @param <T>
 */
public class ObjectMessage<T extends Serializable> extends Message{
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
    
    
}
