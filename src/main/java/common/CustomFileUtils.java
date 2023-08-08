package common;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomFileUtils {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject getJSONObjectFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } catch (Exception e) {
            System.out.println("Exception occurred when fetching URL: " + e);
            return null;
        }
    }

    public static String fileToString(String filePath) {
        try {
            Path file = Path.of(filePath);
            return Files.readString(file);
        } catch (Exception e) {
            System.out.println("Exception occurred when reading file: " + e);
            return null;
        }
    }

    public static void stringToFile(String filePath, String fileContents) {
        try {
            File file = new File(filePath);
            FileUtils.writeStringToFile(file, fileContents, Charset.forName("UTF-8"));
        } catch (Exception e) {
            System.out.println("Exception occurred when creating file: " + e);
        }

    }

    public static File findFileInDirectory(String directory, String fileName) {
        try {
            File dir = new File(directory);
            FileFilter fileFilter = new WildcardFileFilter(fileName);
            File[] files = dir.listFiles(fileFilter);
            if(files.length == 1)
                return files[0];

        } catch (Exception e) {
            System.out.println("Exception occurred when creating file: " + e);
        }

        return null;
    }


}
