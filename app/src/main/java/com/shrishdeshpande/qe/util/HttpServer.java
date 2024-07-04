package com.shrishdeshpande.qe.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrishdeshpande.qe.api.Block;
import com.shrishdeshpande.qe.client.BlockchainClient;
import com.shrishdeshpande.qe.common.Blockchain;
import com.shrishdeshpande.qe.server.BlockchainServer;
import io.javalin.Javalin;

import java.util.List;

public class HttpServer implements Runnable {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void start() {
        Thread httpServerThread = new Thread(new HttpServer());
        httpServerThread.setName("qe-http-server");
        httpServerThread.start();
    }

    @Override
    public void run() {
        Javalin.create()
                .get("/tickets", ctx -> {
                    List<String> nfts = Blockchain.getInstance().getCurrentNfts();
                    ctx.result(OBJECT_MAPPER.writeValueAsBytes(nfts));
                })
                .get("/contract", ctx -> {
                    BlockchainClient.getInstance().contract("Alice", 100);
                })
                .post("/mine", ctx -> {
                    Block last = Blockchain.getInstance().getBlocks().getLast();
                    Block block = BlockchainServer.getInstance().mine(last);
                    BlockchainServer.getInstance().clearMemPool();
                    Blockchain.getInstance().addBlock(block);
                    SocketMessages.newBlock(Blockchain.getInstance().getBlocks());
                })
                .get("/balance", ctx -> {
                    ctx.result(String.valueOf(Blockchain.getInstance().getBalance("Bob")));
                })
                .start(7070);
    }
}
