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
        String absolutePath = defaultPath + relativePath;
        this.relativePath = relativePath;
        setAbsolutePath(absolutePath);
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
        this.fileSystem = new FileSystem(absolutePath);
    }

    public String getContentByPath() throws IOException {
        String result = "";
        try {
            if (fileSystem.checkDir()) {
                result = getDirectoryContent();
            }
        } catch (IllegalArgumentException e) {
            result = fileSystem.getFileContent();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
        return result;
    }

    private String getDirectoryContent() {
        String[] directoryList = fileSystem.getDirectories();
        String[] fileList = fileSystem.getFiles();
        return HTMLGen.generateHTML(new File(absolutePath), relativePath);
    }

    public String getMimeType() {
        String result = "text/html";
        try {
            fileSystem.checkDir();
        } catch (FileNotFoundException e){
        } catch (Exception e) {
            result = fileSystem.getMimeType();
        }
        return result;
    }


}
