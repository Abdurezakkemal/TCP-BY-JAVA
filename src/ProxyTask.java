import java.io.*;
import java.net.*;

public class ProxyTask implements Runnable {
    private static final String TARGET_SERVER = "example.com"; // Change to target server's address
    private static final int TARGET_PORT = 80; // Change to target server's port

    private Socket clientSocket;

    public ProxyTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                InputStream clientInput = clientSocket.getInputStream();
                OutputStream clientOutput = clientSocket.getOutputStream();
                Socket targetSocket = new Socket(TARGET_SERVER, TARGET_PORT);
                InputStream targetInput = targetSocket.getInputStream();
                OutputStream targetOutput = targetSocket.getOutputStream()
        ) {
            // Forward request from client to target server
            forwardData(clientInput, targetOutput);

            // Forward response from target server to client
            forwardData(targetInput, clientOutput);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void forwardData(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
            output.flush();
        }
    }
}
