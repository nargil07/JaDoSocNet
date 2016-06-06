/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.rmi;

import fr.nargilcorp.jadosocnet.agent.AbstractAgent;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author antony
 */
public interface KernelInterface extends Remote{
    public void requestRole(String community, String groupe, String Role, AbstractAgent abstractAgent) throws RemoteException;
    public Object[] getAllAgent() throws RemoteException;
}
