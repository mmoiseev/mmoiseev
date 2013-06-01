package com.suchorukov.server;

import com.suchorukov.server.exceptions.NotImplementedCommand;
import com.suchorukov.server.fileProcessor.FileService;


import java.io.*;
import java.net.Socket;

public class SocketProcessor implements Runnable {

    private Socket s;
    private InputStream is;
    private OutputStream os;
    private String defaultPath;

    private final static String CODE_200="200 OK";

    private final static String CODE_404="404 Not Found";
    private final static String BODY_404="<h1>Error 404 File not found</h1>";

    private final static String CODE_501= "501 Not Implemented";
    private final static String BODY_501="<h1>Error 501 Not implemented</h1>";

    private final static String CODE_500="500 Internal Server Error";
    private final static String BODY_500="<h1>Error 500 Internal Server Error</h1>";


    public SocketProcessor(Socket s, String defaultPath) throws IOException {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
        this.defaultPath = defaultPath;
    }

    public void run() {
        System.out.println("Starting processor for client " + s.getInetAddress() + ":" + s.getPort());
        try {
            String header = readInputHeader();
            String method = RequestParser.getRequestMethod(header);
            if (!checkRequestMethod(method)) {
                throw new NotImplementedCommand("Не поддерживаемая команда " + method);
            }
            String relativePath = RequestParser.getRelativePath(header);
            System.out.println(relativePath);
            FileService fileService = new FileService(defaultPath, relativePath);
            String result = fileService.getContentByPath();
            writeResponse(CODE_200, result, fileService.getMimeType());

        } catch (FileNotFoundException e) {
            try {
                writeResponse(CODE_404, BODY_404, "text/html");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (NotImplementedCommand e) {
            try {
                writeResponse(CODE_501, BODY_501, "text/html");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            try {
                writeResponse(CODE_500, BODY_500 + "<p>" + e.getMessage() + "</p>", "text/html");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e) {
            }
        }
        System.out.println("Client processing finished for client " + s.getInetAddress() + ":" + s.getPort());
    }

    private boolean checkRequestMethod(String method){
        if ("GET".equals(method) || "HEAD".equals(method))
            return true;
        else
            return false;
    }

    private void writeResponse(String code, String result, String type) throws IOException {
        String response = "HTTP/1.1 " + code + "\r\n" +
                "Server: LocalServer\r\n" +
                "Content-Type: " + type + "\r\n" +
                "Content-Length: " + result.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        os.write(response.getBytes());
        os.write(result.getBytes());
        os.flush();
    }

    private String readInputHeader() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = br.readLine();
        return s;
    }
}
