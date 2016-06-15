/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.kernel;

import java.util.LinkedHashMap;

/**
 *
 * @author antony
 */
public class Groupe extends LinkedHashMap<String, Role>{
    private String nom;
    private Kernel kernelMaitre;

    public Groupe(String nom, Kernel kernel) {
        this.nom = nom;
        this.kernelMaitre = kernel;
    }
    
    
    
}
