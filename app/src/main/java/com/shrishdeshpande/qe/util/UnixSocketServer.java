package com.shrishdeshpande.qe.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnixSocketServer {
    private static final Logger LOGGER = LogManager.getLogger(UnixSocketServer.class);

    public static void start() {
        Path socketPath = Path
                .of(System.getProperty("user.home"))
                .resolve("bc1.socket");

        SocketAddress address = new InetSocketAddress("127.0.0.1", 9006);

        try {
            Files.deleteIfExists(socketPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UnixDomainSocketAddress socketAddress = UnixDomainSocketAddress.of(socketPath);

        new Thread(() -> {
            try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
                serverSocketChannel.bind(address);
                LOGGER.info("Server is listening on {}", address);

                try (SocketChannel clientChannel = serverSocketChannel.accept()) {
                    LOGGER.info("Client connected");

                    new Thread(() -> SocketMessages.readMessages(clientChannel)).start();
                    new Thread(() -> SocketMessages.writeMessages(clientChannel)).start();

                    Thread.currentThread().join();
                }
            } catch (IOException e) {
                LOGGER.error("Error occurred", e);
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted");
            }
        }).start();
    }
}
