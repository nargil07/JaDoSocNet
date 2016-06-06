/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import fr.nargilcorp.jadosocnet.rmi.KernelInterface;
import java.io.Serializable;
import static java.lang.Boolean.FALSE;
import java.rmi.Naming;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author antony
 */
public abstract class AbstractAgent implements Serializable, Runnable{
    private Thread instance;
    private final AtomicBoolean alive;
    private KernelInterface kernel;

    public AbstractAgent() {
        alive = new AtomicBoolean(false);
    }
    
    public void launchAgent(){
        instance = new Thread(this);
        instance.start();
    }
    
    /**
     * Méthode à implementé quand il est vivant.
     */
    protected abstract void alive();
    
    /**
     * Appelé au moment ou l'agent s'active.
     */    
    protected void activate(){
        alive.set(true);
    }
    
    /**
     * Appelé au moment de la destruction de l'agent.
     */
    protected void destroy(){
        alive.set(false);
    }
    
    protected void searchKernel(){
        try {
            kernel = (KernelInterface) Naming.lookup("//localhost/Kernel");
        } catch (Exception e) {
            
        }
    }

    @Override
    public void run() {
        activate();
        alive();
        destroy();
    }
    
}
