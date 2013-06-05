package com.suchorukov.server.fileProcessor;

import com.suchorukov.server.documentGenerator.HTMLGen;

import java.io.*;

public class FileService {
    private String relativePath;
    private String absolutePath;
    private FileSystem fileSystem;

    public FileService(String defaultPath, String relativePath) {
        this.relativePath = relativePath;
        setAbsolutePath(defaultPath + relativePath);
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
        this.fileSystem = new FileSystem(absolutePath);
    }

    public String getContentByPath() throws IOException {
        String result = "";
        if (!fileSystem.checkExist()){
            throw new FileNotFoundException(absolutePath);
        }
        if (fileSystem.checkDir()){
            result = getDirectoryContent();
        } else {
            result = fileSystem.getFileContent();
        }
        return result;
    }

    private String getDirectoryContent() {
        String result = "";
        try {
            result = fileSystem.getIndex();
        } catch (IOException e) {
            result = HTMLGen.generateHTML( fileSystem.getFile(), relativePath);
        }
        return result;

    }

    public String getMimeType() throws FileNotFoundException {
        String result = "text/html";
        if (!fileSystem.checkExist()){
            throw new FileNotFoundException(absolutePath);
        }
        if (!fileSystem.checkDir()){
            result = fileSystem.getMimeType();
        }
        return result;
    }


}
