/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import fr.nargilcorp.jadosocnet.kernel.Kernel;
import fr.nargilcorp.jadosocnet.message.ObjectMessage;
import fr.nargilcorp.jadosocnet.kernel.JavaProcess;
import fr.nargilcorp.jadosocnet.kernel.KernelInterface;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public abstract class AbstractAgent implements Serializable, Runnable {

    private Thread instance;
    private final AtomicBoolean alive;
    protected KernelInterface kernel;
    protected AdresseAgent agentAdresse;
    private DatagramSocket datagramSocket;

    public AbstractAgent() {
        alive = new AtomicBoolean(false);
    }

    public void launchAgent() {
        instance = new Thread(this);
        instance.start();
    }

    /**
     * Méthode à implementé quand il est vivant.
     */
    protected abstract void alive();

    protected void requestRole(String community, String groupe, String role) {
        try {

            datagramSocket = new DatagramSocket();
            agentAdresse = new AdresseAgent(Inet4Address.getLocalHost().getHostAddress(), datagramSocket.getLocalPort());
            kernel.requestRole(community, groupe, role, agentAdresse);
        } catch (SocketException ex) {
            Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void sendMessage(AdresseAgent adresseAgent, ObjectMessage message) {
        try {
            message.setSender(this.agentAdresse);
            message.setReceiver(adresseAgent);
            byte[] buffer = messageToByte(message);
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(adresseAgent.getAddress()), adresseAgent.getPort());
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Appelé au moment ou l'agent s'active.
     */
    protected void activate() {
        searchKernel();
        alive.set(true);
    }

    protected void broadcastMessage(String community, String groupe, String role, ObjectMessage message) {
        try {
            message.setSender(agentAdresse);
            this.kernel.sendMessage(community, groupe, role, message);
        } catch (RemoteException ex) {
            Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected ObjectMessage waitMessage() {
        ObjectMessage objectMessage = null;
        try {
            byte[] buffer = new byte[8196];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(buffer));
            objectMessage = (ObjectMessage) ois.readObject();
            datagramSocket.close();
            datagramSocket = new DatagramSocket(agentAdresse.getPort(), InetAddress.getByName(agentAdresse.getAddress()));
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return objectMessage;
    }

    /**
     *
     * @param timeout time in ms
     * @return
     */
    protected ObjectMessage waitMessage(int timeout) {
        ObjectMessage objectMessage = new ObjectMessage();
        try {
            byte[] buffer = new byte[8196];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            TimeoutThread timeoutThread = new TimeoutThread(timeout, datagramSocket, agentAdresse);
            timeoutThread.start();
            datagramSocket.receive(datagramPacket);
            timeoutThread.setTodo(false);
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(buffer));
            objectMessage = (ObjectMessage) ois.readObject();
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return objectMessage;
    }

    /**
     * Appelé au moment de la destruction de l'agent.
     */
    protected void destroy() {
        alive.set(false);
    }

    protected void searchKernel() {
        boolean error = false;
        do {
            try {
                kernel = (KernelInterface) Naming.lookup("//localhost/Kernel");
                System.out.println("Kernel trouvé");
                error = false;
            } catch (Exception e) {
                try {
                    
                    JavaProcess.exec(Kernel.class);
                    error = true;
                } catch (Exception ex) {
                    System.out.println("Server not connected: " + ex);
                    error = true;
                }
            }
        } while (error);
    }

    public void ecrireMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {
        activate();
        alive();
        destroy();
    }

    private static byte[] messageToByte(ObjectMessage message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(message);
        return byteArrayOutputStream.toByteArray();
    }
    
    private class TimeoutThread extends Thread{

        private final int timeout;
        private final DatagramSocket socket;
        private final AdresseAgent agent;
        private boolean todo = true;

        public TimeoutThread(int timeout, DatagramSocket socket, AdresseAgent agent) {
            this.timeout = timeout;
            this.socket = socket;
            this.agent = agent;
        }

        public boolean isTodo() {
            return todo;
        }

        public void setTodo(boolean todo) {
            this.todo = todo;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(timeout);
                if(todo){
                    try {
                        this.socket.close();
                        this.socket.connect(InetAddress.getByName(agent.getAddress()), agent.getPort());
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(AbstractAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
