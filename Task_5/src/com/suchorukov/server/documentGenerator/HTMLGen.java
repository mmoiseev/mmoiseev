package com.suchorukov.server.documentGenerator;

import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HTMLGen {
    static MessageFormat trTemplate = new MessageFormat("<tr>" +
            "<td width=\"300px\"><a href=\"{0}\">" +
            "{1}" +
            "</a></td>" +
            "<td>" +
            "{2}" +
            "</td>" +
            "<td>" +
            "{3}" +
            "</td>" +
            "</tr>");

    private HTMLGen(){}

    public static void writeHTML(String path, OutputStream out) throws IOException {
        File inputFile;
        try {
            inputFile = new File(path);
            if (inputFile.exists()) {
                if (inputFile.isDirectory()) {
                    File indexFile = new File(path + File.separator + "index.html");
                    String indexText = "";
                    boolean fileRead = false;
                    if (indexFile.exists()) {
                        BufferedReader indexReader = null;
                        try {
                            indexReader = new BufferedReader(new InputStreamReader(new FileInputStream(indexFile)));
                            StringBuilder indexTextBuild = new StringBuilder();
                            while ( true ) {
                                String str = indexReader.readLine();
                                if (str == null) break;
                                indexTextBuild.append(str);
                            }
                            indexText = indexTextBuild.toString();
                            fileRead = true;
                        } catch (Exception e){
                        } finally {
                            try {indexReader.close();} catch (Exception e){}
                        }
                    } else if (!fileRead) {
                        indexText = generateHTML(inputFile, "");
                    }
                    out.write(indexText.getBytes("UTF-8"));
                }
            } else {
                throw new FileNotFoundException();
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("Путь к файлу не задан");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Файл не найден");
        } catch (IOException e) {
            throw new IOException("Ошибка ввода/вывода");
        }

    }


    public static String generateHTML(File inputFile, String relativePath) {
        StringBuilder html = new StringBuilder();
        html.append("<HTML>" + "<HEAD><TITLE>" + inputFile.getPath() + "</TITLE><HEAD><BODY>");
        html.append("<META charset=\"utf-8\">");
        html.append("<h1>" + inputFile.getPath() + "</h1>");
        html.append("<table>");
        if (inputFile.getParent() != null){
            Object[] parentStr = new Object[4];
            parentStr[0] = relativePath + "/..";
            parentStr[1] = "..";
            parentStr[2] = "";
            parentStr[3] = "";
            html.append(trTemplate.format(parentStr).toString());
        }
        String[] arrName = inputFile.list();
        List<File> files = new ArrayList<File>();
        for (String name : arrName) {
            File file = new File(inputFile + File.separator + name);
            files.add(file);
        }
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if ((o1.isDirectory() && o2.isDirectory()) || (!o1.isDirectory() && !o2.isDirectory())) {
                    return o1.getName().compareTo(o2.getName());
                }
                return o1.isDirectory() ? -1 : 1;
            }
        });
        for (File file : files) {
            Object[] fileArg = new Object[4];
            fileArg[0] = relativePath + "/" + file.getName();
            fileArg[1] = file.getName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            fileArg[2] = (file.isDirectory() ? "&lt;dir&gt;" : file.length() / 1024 + "KБ");
            fileArg[3] = dateFormat.format(file.lastModified());
            html.append(trTemplate.format(fileArg).toString());
        }

        html.append("</table>");
        html.append("</BODY></HTML>");
        return html.toString();
    }


}
