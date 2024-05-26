/*
 * This source file was generated by the Gradle 'init' task
 */
package com.shrishdeshpande.qe;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        System.out.println(new App().getGreeting());
    }
}
