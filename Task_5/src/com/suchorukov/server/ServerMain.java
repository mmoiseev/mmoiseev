package com.suchorukov.server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        int port = 8080;
        String defaultPath = "\\";

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
            }
            try {
                defaultPath = args[1];
            } catch (Exception e) {
            }
        }

        RequestHandler server = new RequestHandler(port, defaultPath);
        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
