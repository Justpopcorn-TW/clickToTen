package util;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {
    private HttpServer server;
    private String rootDir;

    public SimpleHttpServer(int port, String rootDir) throws IOException {
        this.rootDir = rootDir;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(null);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String uri = t.getRequestURI().getPath();
            if (uri.equals("/")) uri = "/index.html";
            File file = new File(rootDir, uri);
            if (file.exists() && !file.isDirectory()) {
                byte[] bytes = new byte[(int) file.length()];
                try (FileInputStream fis = new FileInputStream(file)) {
                    fis.read(bytes);
                }
                t.sendResponseHeaders(200, bytes.length);
                OutputStream os = t.getResponseBody();
                os.write(bytes);
                os.close();
                return;
            }
            // Try to load from resources
            String resourcePath = uri.startsWith("html") ? uri.substring(1) : uri;
            try (java.io.InputStream in = SimpleHttpServer.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (in != null) {
                    byte[] bytes = in.readAllBytes();
                    t.sendResponseHeaders(200, bytes.length);
                    OutputStream os = t.getResponseBody();
                    os.write(bytes);
                    os.close();
                    return;
                }
            }
            String notFound = "404 Not Found";
            t.sendResponseHeaders(404, notFound.length());
            OutputStream os = t.getResponseBody();
            os.write(notFound.getBytes());
            os.close();
        }
    }
}
