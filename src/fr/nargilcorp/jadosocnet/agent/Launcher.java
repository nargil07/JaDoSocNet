/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nargilcorp.jadosocnet.agent;

/**
 *
 * @author antony
 */
public class Launcher {
    public static void main(String[] args) throws InterruptedException {
        new PingPongAgent().launchAgent();
    }
}
