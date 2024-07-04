package com.shrishdeshpande.qe.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrishdeshpande.qe.api.Block;
import com.shrishdeshpande.qe.api.transaction.CryptoTransaction;
import com.shrishdeshpande.qe.api.transaction.Transaction;
import com.shrishdeshpande.qe.common.Blockchain;
import com.shrishdeshpande.qe.server.BlockchainServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SocketMessages {
    public static final ConcurrentLinkedQueue<String> MESSAGE_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Logger LOGGER = LogManager.getLogger(SocketMessages.class);
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private static void newMessage(IpcRepresentable representable) {
        String str;
        try {
            str = OBJECT_MAPPER.writerFor(IpcRepresentable.class).writeValueAsString(representable);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error sending transaction");
            return;
        }
        MESSAGE_QUEUE.add(str);
    }

    private static void onMessage(IpcRepresentable representable) {
        if (representable instanceof ChainWrapper cw) {
            Blockchain.getInstance().replaceChain(cw.blocks());
            LOGGER.info("Received new blockchain with {} blocks", cw.blocks().size());
            BlockchainServer.getInstance().clearMemPool();
        } else if (representable instanceof Transaction ct) {
            BlockchainServer.getInstance().addTransaction(ct);
            LOGGER.info("Received transaction");
        }
    }

    public static void newBlock(List<Block> chain) {
        newMessage(new ChainWrapper(chain));
    }

    public static void newTransaction(Transaction t) {
        newMessage(t);
    }

    public static void readMessages(SocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1048576);
        try {
            while (true) {
                buffer.clear();
                int bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) break;
                buffer.flip();
                String receivedMessage = new String(buffer.array(), 0, buffer.limit());
                LOGGER.info("Received message");
                IpcRepresentable representable = OBJECT_MAPPER.readValue(receivedMessage, IpcRepresentable.class);
                onMessage(representable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMessages(SocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1048576);
        try {
            while (true) {
                buffer.clear();

                String message = MESSAGE_QUEUE.poll();

                if (message == null) {
                    Thread.sleep(1000);
                    continue;
                }

                buffer.put(message.getBytes());
                buffer.flip();
                clientChannel.write(buffer);
                LOGGER.info("Sent message");
                Thread.sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
