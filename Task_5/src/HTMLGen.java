import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HTMLGen {
    static MessageFormat trTemplate = new MessageFormat("<tr>" +
            "<td><a href=\"{0}\">" +
            "{0}" +
            "</a></td>" +
            "<td>" +
            "{1}" +
            "</td>" +
            "<td>" +
            "{2}" +
            "</td>" +
            "</tr>");

    private HTMLGen(){}

    public static void getHTML(String path, OutputStream out) {
        File inputFile;
        try {
            inputFile = new File(path);
            if (inputFile.exists()) {
                if (inputFile.isDirectory()) {
                    String[] elemArr = inputFile.list();
                    String indexText = generateHTML(inputFile.getPath(), elemArr);
                    out.write(indexText.getBytes("UTF-8"));
                    /*File index = new File(path + File.separator + "index.html");
                    OutputStream indexWriter = new FileOutputStream(index);
                    indexWriter.write(indexText.getBytes("UTF-8")); */
                } else {

                }
            } else {
                throw new FileNotFoundException();
            }

        } catch (NullPointerException e) {
            System.out.println("Путь к файлу не задан");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public static String generateHTML(String path, String[] arrName) {
        StringBuilder html = new StringBuilder();
        html.append("<HTML>" + "<HEAD><TITLE>" + path + "</TITLE><HEAD><BODY>");
        html.append("<META charset=\"utf-8\">");
        html.append("<h1>" + path + "</h1>");
        html.append("<table>");
        List<File> files = new ArrayList<File>();
        for (String name : arrName) {
            File file = new File(path + File.separator + name);
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
            Object[] fileArg = new Object[3];
            fileArg[0] = file.getName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            fileArg[1] = dateFormat.format(file.lastModified());
            fileArg[2] = (file.isDirectory() ? "dir" : file.length() / 1024 + "KБ");
            html.append(trTemplate.format(fileArg).toString());
        }

        html.append("</table>");
        html.append("</BODY></HTML>");
        return html.toString();
    }


}
