package makar.resourcegame.server;

import java.io.InputStream;
import java.util.Scanner;

public class PostRequestBodyParser {

    public int parsePoints(InputStream inputStream) {
        String body = toString(inputStream);
        String[] keyValue = body.trim().split("=");
        return Integer.parseInt(keyValue[1]);
    }

    public String toString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
