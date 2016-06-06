/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import fr.nargilcorp.jadosocnet.agent.AbstractAgent;
import fr.nargilcorp.jadosocnet.agent.Community;
import fr.nargilcorp.jadosocnet.agent.Groupe;
import fr.nargilcorp.jadosocnet.agent.Role;
import fr.nargilcorp.jadosocnet.rmi.KernelInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author antony
 */
public class Kernel extends UnicastRemoteObject implements KernelInterface {

    LinkedHashMap<String, Community> linkedHashMap;

    public Kernel() throws RemoteException {
        linkedHashMap = new LinkedHashMap<>();
    }

    @Override
    public void requestRole(String community, String groupe, String role, AbstractAgent abstractAgent) throws RemoteException {
        Community objectCommunity = linkedHashMap.get(community);
        if (objectCommunity == null) {
            objectCommunity = new Community(community, this);
            Groupe objectGroupe = new Groupe(groupe, this);
            Role objectRole = new Role(role, this);
            objectRole.add(abstractAgent);
            objectGroupe.put(role, objectRole);
            objectCommunity.put(groupe, objectGroupe);
            linkedHashMap.put(community, objectCommunity);
        } else {
            Groupe objectGroupe = objectCommunity.get(groupe);
            if (objectGroupe == null) {
                objectGroupe = new Groupe(groupe, this);
                Role objectRole = new Role(role, this);
                objectRole.add(abstractAgent);
                objectGroupe.put(role, objectRole);
                objectCommunity.put(groupe, objectGroupe);
                linkedHashMap.put(community, objectCommunity);
            } else {
                Role objectRole = objectGroupe.get(groupe);
                if (objectGroupe == null) {
                    objectRole = new Role(role, this);
                    objectRole.add(abstractAgent);
                    objectGroupe.put(role, objectRole);
                    linkedHashMap.put(community, objectCommunity);
                } else {
                    objectRole.add(abstractAgent);
                }
            }
        }
    }

    @Override
    public Object[] getAllAgent() throws RemoteException {
        LinkedList<AbstractAgent> results = new LinkedList<>();
        for (Map.Entry<String, Community> entry : linkedHashMap.entrySet()) {
            Community value = entry.getValue();
            for (Map.Entry<String, Groupe> entry1 : value.entrySet()) {
                Groupe value1 = entry1.getValue();
                for (Map.Entry<String, Role> entry2 : value1.entrySet()) {
                    Role value2 = entry2.getValue();
                    results.addAll(value2);
                }
            }
        }

        return results.toArray();
    }

}
