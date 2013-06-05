package com.suchorukov.server.fileProcessor;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

public class FileSystem {
    private File absolutePathFile;
    private File[] listFiles = new File[]{};

    public FileSystem(String absolutePath) {
        setAbsolutePathFile(absolutePath);
    }

    public File getFile(){
        return absolutePathFile;
    }

    public void setAbsolutePathFile(String absolutePath) {
        absolutePathFile = new File(absolutePath);
        if (absolutePathFile.exists())
            listFiles = absolutePathFile.listFiles();
    }

    public boolean checkExist() throws FileNotFoundException {
        return absolutePathFile.exists();
    }

    public boolean checkDir() {
        return absolutePathFile.isDirectory();
    }

    public String[] getDirectories() {
        String[] listStrDirs = new String[listFiles.length];
        int k = 0;
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory()) {
                listStrDirs[k++] = listFiles[i].getName();
            }
        }
        return listStrDirs;
    }

    public String[] getFiles() {
        String[] listStrFiles = new String[listFiles.length];
        int k = 0;
        for (int i = 0; i < listFiles.length; i++) {
            if (!listFiles[i].isDirectory()) {
                listStrFiles[k++] = listFiles[i].getName();
            }
        }
        return listStrFiles;
    }

    public String getFileContent() throws IOException {
       return getFileString(absolutePathFile);
    }

    private String getFileString(File file) throws IOException {
        if (!file.canRead()) {
            throw new FileNotFoundException();
        }
        StringBuilder result = new StringBuilder();
        InputStream fInput = null;
        try {
            fInput = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int read;
            while ((read = fInput.read(buf)) != -1) {
                result.append(new String(buf));
            }
        } finally {
            try {
                fInput.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    public String getIndex() throws IOException {
        return getFileString(new File(absolutePathFile.getPath() + File.separator + "index.html"));
    }


    public String getMimeType() {
        //Работает если положить файл .mime.types в дирректорию юзера
        //Файл в корне репозитория
        return new MimetypesFileTypeMap().getContentType(absolutePathFile);
    }


}
