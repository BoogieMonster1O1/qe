package com.shrishdeshpande.qe.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

import static com.shrishdeshpande.qe.util.SocketMessages.readMessages;
import static com.shrishdeshpande.qe.util.SocketMessages.writeMessages;

public class UnixSocketClient {
    private static final Logger LOGGER = LogManager.getLogger(UnixSocketClient.class);

    public static void start() {
        Path socketPath = Path
                .of(System.getProperty("user.home"))
                .resolve("bc1.socket");
        UnixDomainSocketAddress socketAddress = UnixDomainSocketAddress.of(socketPath);
        SocketAddress address = new InetSocketAddress("127.0.0.1", 9006);

        new Thread(() -> {
            try (SocketChannel socketChannel = SocketChannel.open()) {
                socketChannel.connect(address);
                LOGGER.info("Connected to server");

                // Start read and write threads
                new Thread(() -> readMessages(socketChannel)).start();
                new Thread(() -> writeMessages(socketChannel)).start();

                Thread.currentThread().join();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                LOGGER.info("Interrupted");
                e.printStackTrace();
            }
        }).start();
    }
}
