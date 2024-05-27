package com.shrishdeshpande.qe.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnixDomainSocketServer {
    private static final Logger LOGGER = LogManager.getLogger(UnixDomainSocketServer.class);

    public static void start() {
        Path socketPath = Path.of("~/a_socket");

        try {
            Files.deleteIfExists(socketPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UnixDomainSocketAddress socketAddress = UnixDomainSocketAddress.of(socketPath);

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(socketAddress);
            LOGGER.info("Server is listening on {}", socketAddress);

            try (SocketChannel clientChannel = serverSocketChannel.accept()) {
                LOGGER.info("Client connected");

                new Thread(() -> SocketMessages.readMessages(clientChannel)).start();
                new Thread(() -> SocketMessages.writeMessages(clientChannel)).start();
            }
        } catch (IOException e) {
            LOGGER.error("Error occurred", e);
        }
    }
}
