package com.suchorukov.server;

import java.net.*;
import java.io.*;

public class RequestHandler {
    private int port;
    private String defaultPath;

    public RequestHandler(int port, String defaultPath) {
        this.port = port;
        this.defaultPath = defaultPath;
    }

    public void listen() throws IOException {
        ServerSocket s = new ServerSocket(port);

        while (true) {
            Socket clientSocket = s.accept();
            Thread t = new Thread(new SocketProcessor(clientSocket, defaultPath));
            t.start();
        }
    }
}