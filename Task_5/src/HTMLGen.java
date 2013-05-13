import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HTMLGen {
    File inputFile;

    MessageFormat trTemplate = new MessageFormat("<tr>" +
            "<td>" +
                "{0}" +
            "</td>" +
            "<td>" +
                "{1}" +
            "</td>" +
            "<td>" +
                "{2}" +
            "</td>" +
            "</tr>");

    public HTMLGen(String path) {
        try {
            inputFile = new File(path);
            if (inputFile.exists()) {
                if (inputFile.isDirectory()) {
                    String[] elemArr = inputFile.list();
                    String indexText = getHTML(inputFile.getPath(), elemArr);
                    System.out.println(indexText);
                } else {

                }
            } else {
                throw new FileNotFoundException();
            }

        } catch (NullPointerException e){
            System.out.println("Путь к файлу не задан");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }

    }

    String getHTML(String path, String[] arrName) {
        StringBuilder html = new StringBuilder();
        html.append("<HTML>" + "<HEAD><TITLE>" + path + "</TITLE><HEAD><BODY>");
        html.append("<table>");
        List<File> files = new ArrayList<File>();
        for (String name : arrName){
            File file = new File(path + "\\" + name);
            files.add(file);
        }
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if ((o1.isDirectory() && o2.isDirectory()) || (o1.isFile() && o2.isFile())) {
                    return o1.getName().compareTo(o2.getName());
                }
                return o1.isDirectory() ? -1 : 1;
            }
        } );
        for (File file : files){
            Object[] fileArg = new Object[3];
            fileArg[0] = file.getName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            fileArg[1] = dateFormat.format(file.lastModified());
            fileArg[2] = (file.isDirectory() ? "<dir>" : file.length()/1024 + "KБ");
            html.append(trTemplate.format(fileArg).toString());
        }

        html.append("</table>");
        html.append("</BODY></HTML>");
        return html.toString();
    }


}
