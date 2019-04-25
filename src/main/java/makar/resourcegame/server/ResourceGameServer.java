package makar.resourcegame.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import makar.resourcegame.server.game.Game;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class ResourceGameServer {

    private static final Logger LOGGER = Logger.getLogger(ResourceGameServer.class.getName());
    private final int portNumber;

    public ResourceGameServer(int portNumber) {
        this.portNumber = portNumber;
    }

    public void startGame(Game game) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        HttpContext context = server.createContext("/");
        HttpHandler handler = new GameServerRequestHandler(game);
        context.setHandler(handler);
        LOGGER.info("Starting server on " + server.getAddress());
        server.start();
    }
}
