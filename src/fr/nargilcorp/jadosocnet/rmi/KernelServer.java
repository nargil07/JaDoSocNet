/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.rmi;

import fr.nargilcorp.jadosocnet.kernel.Kernel;
import java.rmi.registry.Registry;

/**
 *
 * @author antony
 */
public class KernelServer {
    public static void main(String[] args) {
        try {
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);//1099 is the port number
            r.rebind("Kernel", new Kernel());
            System.out.println("Server is connected and ready for operation.");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
        }
    }
}
