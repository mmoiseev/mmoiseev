package com.suchorukov.server;

import com.suchorukov.server.exceptions.NotImplementedCommand;
import com.suchorukov.server.fileProcessor.FileService;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;

public class SocketProcessor implements Runnable {

    private Socket s;
    private InputStream is;
    private OutputStream os;
    private String defaultPath;

    public SocketProcessor(Socket s, String defaultPath) throws IOException {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
        this.defaultPath = defaultPath;
    }

    public void run() {
        try {
            String header = readInputHeader();
            String method = RequestParser.getRequestMethod(header);
            if (!checkRequestMethod(method)) {
                throw new NotImplementedCommand("Не поддерживаемая команда");
            }
            String relativePath = RequestParser.getRelativePath(header);
            System.out.println(relativePath);
            String absolutePath = defaultPath + relativePath;

            FileService fileService = new FileService(defaultPath, relativePath);
            String result = fileService.getContentByPath();
            writeResponse(result, fileService.getMimeType());
        } catch (FileNotFoundException e) {
            //todo посылать 404
            e.printStackTrace();
        } catch (NotImplementedCommand e) {
           //todo посылать 501
           e.printStackTrace();
        } catch (Exception e) {
            //todo посылать 500
            e.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e) {
            }
        }
        System.out.println("Client processing finished");
    }

    private boolean checkRequestMethod(String method){
        if ("GET".equals(method) || "HEAD".equals(method))
            return true;
        else
            return false;
    }

    private void writeResponse(String result, String type) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: LocalServer\r\n" +
                "Content-Type: " + type + "\r\n" + //
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
