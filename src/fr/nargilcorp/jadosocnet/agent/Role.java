/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

import java.util.LinkedList;

/**
 *
 * @author antony
 */
public class Role extends LinkedList<AbstractAgent>{
    private String nom;
    private Kernel kernelMaitre;
    
    public Role(String nom, Kernel kernel) {
        this.nom = nom;
        this.kernelMaitre = kernel;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o); //To change body of generated methods, choose Tools | Templates.
        if(this.isEmpty()){
            kernelMaitre = null;
        }
        return result;
    }
    
    
}
