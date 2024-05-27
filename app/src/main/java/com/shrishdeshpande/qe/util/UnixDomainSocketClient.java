package com.shrishdeshpande.qe.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

import static com.shrishdeshpande.qe.util.SocketMessages.readMessages;
import static com.shrishdeshpande.qe.util.SocketMessages.writeMessages;

public class UnixDomainSocketClient {
    private static final Logger LOGGER = LogManager.getLogger(UnixDomainSocketClient.class);

    public static void start() {
        Path socketPath = Path.of("~/a_socket");
        UnixDomainSocketAddress socketAddress = UnixDomainSocketAddress.of(socketPath);

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(socketAddress);
            LOGGER.info("Connected to server");

            // Start read and write threads
            new Thread(() -> readMessages(socketChannel)).start();
            new Thread(() -> writeMessages(socketChannel)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
