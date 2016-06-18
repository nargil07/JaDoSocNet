/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.kernel;

import fr.nargilcorp.jadosocnet.message.ObjectMessage;
import fr.nargilcorp.jadosocnet.agent.AdresseAgent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public void requestRole(String community, String groupe, String role, AdresseAgent agentAdresse) throws RemoteException {
        Community objectCommunity = linkedHashMap.get(community);
        if (objectCommunity == null) {
            objectCommunity = new Community(community, this);
            Groupe objectGroupe = new Groupe(groupe, this);
            Role objectRole = new Role(role, this);
            objectRole.add(agentAdresse);
            objectGroupe.put(role, objectRole);
            objectCommunity.put(groupe, objectGroupe);
            linkedHashMap.put(community, objectCommunity);
        } else {
            Groupe objectGroupe = objectCommunity.get(groupe);
            if (objectGroupe == null) {
                objectGroupe = new Groupe(groupe, this);
                Role objectRole = new Role(role, this);
                objectRole.add(agentAdresse);
                objectGroupe.put(role, objectRole);
                objectCommunity.put(groupe, objectGroupe);
                linkedHashMap.put(community, objectCommunity);
            } else {
                Role objectRole = objectGroupe.get(groupe);
                if (objectGroupe == null) {
                    objectRole = new Role(role, this);
                    objectRole.add(agentAdresse);
                    objectGroupe.put(role, objectRole);
                    linkedHashMap.put(community, objectCommunity);
                } else {
                    objectRole.add(agentAdresse);
                }
            }
        }
    }

    @Override
    public Object[] getAllAgent() throws RemoteException {
        LinkedList<AdresseAgent> results = new LinkedList<>();
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

    @Override
    public void sendMessage(String community, String groupe, String Role, ObjectMessage message) throws RemoteException {
        LinkedList<AdresseAgent> agents = linkedHashMap.get(community).get(groupe).get(Role);
        for (AdresseAgent agent : agents) {
            boolean testAdresse = agent.getAddress().equals(message.getSender().getAddress());
            boolean testPort = agent.getPort() == message.getSender().getPort();
            if (!(testAdresse && testPort)) {
                try {
                    message.setReceiver(agent);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(message);
                    objectOutputStream.close();
                    DatagramPacket datagramPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), InetAddress.getByName(agent.getAddress()), agent.getPort());
                    new DatagramSocket().send(datagramPacket);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SocketException ex) {
                    Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public static void main(String[] args) throws RemoteException {
        Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);//1099 is the port number
        Kernel kernel = new Kernel();
        r.rebind("Kernel", kernel);
        System.out.println("Server is connected and ready for operation.");
    }
    private class TimeOutThread extends Thread{
        
    }
}
