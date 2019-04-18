package makar.resourcegame.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseBodyCreator {
    //language=html
    public String getResponseBody() {
        System.out.println("Reading response from file");
        Path path = null;
        try {
            path = Paths.get(getClass().getClassLoader().getResource("game-page.html").toURI());
        } catch (URISyntaxException e) {
            System.out.println("Failed to find file in classpath");
            e.printStackTrace();
        }
        System.out.println("Trying to read file " + path);
        try {
            return Files.readString(path, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
            return null;
        }
    }
}
