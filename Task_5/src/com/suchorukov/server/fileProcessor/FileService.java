package com.suchorukov.server.fileProcessor;

import com.suchorukov.server.documentGenerator.HTMLGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        return HTMLGen.generateHTML(new File(absolutePath), relativePath);
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
