import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        File index = new File("c:\\DiscD\\Video\\index.html");
        index.createNewFile();
        OutputStream indexWriter = new FileOutputStream(index);
        HTMLGen.getHTML("c:\\DiscD\\Video", indexWriter);

    }
}
