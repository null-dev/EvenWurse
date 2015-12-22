package tk.wurst_client.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

/**
 * Project: EvenWurse
 * Created: 21/12/15
 * Author: nulldev
 */
public class IOUtils {
    public static void writeToFile(String string, File file) throws IOException {
        PrintWriter save = new PrintWriter(new FileWriter(file));
        save.println(string);
        save.close();
    }

    public static String readFile(File file) throws IOException {
        return String.join("\n", Files.readAllLines(file.toPath()));
    }
}
