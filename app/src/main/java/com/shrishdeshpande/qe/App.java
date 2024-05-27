/*
 * This source file was generated by the Gradle 'init' task
 */
package com.shrishdeshpande.qe;

import com.shrishdeshpande.qe.api.Block;
import com.shrishdeshpande.qe.api.transaction.Transaction;
import com.shrishdeshpande.qe.client.BlockchainClient;
import com.shrishdeshpande.qe.common.Blockchain;
import com.shrishdeshpande.qe.server.BlockchainServer;
import com.shrishdeshpande.qe.util.SocketMessages;
import com.shrishdeshpande.qe.util.UnixSocketClient;
import com.shrishdeshpande.qe.util.UnixSocketServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        LOGGER.info("Starting up QE CLI");
        Security.addProvider(new BouncyCastleProvider());
        System.out.println(new App().getGreeting());

        if (args.length != 2) {
            LOGGER.error("Invalid number of arguments");
            System.exit(1);
        }

        String path = args[0];

        String ipcRole = args[1];
        boolean server = ipcRole.equals("server");

        LOGGER.info("Initializing blockchain with path {}", path);

        BlockchainClient.init(path);
        BlockchainServer.initWithName(BlockchainClient.getInstance().name);
        Blockchain.init(path);

        if (server) {
            UnixSocketServer.start();
        } else {
            UnixSocketClient.start();
        }

        Scanner sc = new Scanner(System.in);

        label:
        while (true) {
            String command = sc.nextLine();
            line();
            switch (command) {
                case "exit":
                    break label;
                case "transact":
                    System.out.println("Enter recipient:");
                    String recipient = sc.nextLine();
                    System.out.println("Enter amount:");
                    int amount = Integer.parseInt(sc.nextLine());
                    BlockchainClient.getInstance().transact(recipient, amount);
                    break;
                case "transactions":
                    Blockchain.getInstance()
                            .getBlocks()
                            .stream()
                            .map(Block::getTransactions)
                            .flatMap(List::stream)
                            .sorted()
                            .distinct()
                            .map(Transaction::readable).forEach(System.out::println);
                    break;
                case "genesis":
                    Block gen = BlockchainServer.getInstance().genesis();
                    Blockchain.getInstance().addBlock(gen);
                    SocketMessages.newBlock(Blockchain.getInstance().getBlocks());
                    break;
                case "mine":
                    Block last = Blockchain.getInstance().getBlocks().getLast();
                    Block block = BlockchainServer.getInstance().mine(last);
                    Blockchain.getInstance().addBlock(block);
                    SocketMessages.newBlock(Blockchain.getInstance().getBlocks());
                    BlockchainServer.getInstance().clearMemPool();
                    break;
                case "mempool":
                    BlockchainServer.getInstance().mempool.stream().map(Transaction::readable).forEach(System.out::println);
                    break;
                case "blocks":
                    Blockchain.getInstance().getBlocks().stream().map(Block::readable).forEach(System.out::println);
                    break;
            }
            line();
        }

        LOGGER.info("Shutting down QE CLI");
    }

    private static void line() {
        System.out.println("---------------------------------------------------------------------");
    }
}
