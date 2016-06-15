/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.rmi;

import fr.nargilcorp.jadosocnet.agent.AbstractAgent;
import fr.nargilcorp.jadosocnet.agent.BasicAgent;
import java.rmi.Naming;
import java.util.Arrays;

/**
 *
 * @author antony
 */
public class KernelClient {
    public static void main(String[] argv) {
        try {
            KernelInterface hello = (KernelInterface) Naming.lookup("//localhost/Kernel");
            //hello.requestRole("com", "com", "com", new BasicAgent());
            System.out.println(Arrays.toString(hello.getAllAgent()));
        } catch (Exception e) {
            System.out.println("KernelCLient exception: " + e);
        }
    }
}
