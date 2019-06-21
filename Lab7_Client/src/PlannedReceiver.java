import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class PlannedReceiver {

    private DatagramSocket socket;
    private MessageSender sender;

    public PlannedReceiver(DatagramSocket socket, MessageSender sender) {
        this.socket = socket;
        try {
            this.socket.setSoTimeout(1000);
        } catch (SocketException e) {

        }
        this.sender = sender;
    }

    public Response listenServer() {
        byte[] data = new byte[8192];
        DatagramPacket dp = new DatagramPacket(data, data.length);
        try {
            socket.receive(dp);
        } catch (SocketTimeoutException e) {
            boolean connected = false;

            for (int i = 1; i < 11; i++) {
                System.out.println("Попытка восстановить соединение №" + i);
                sender.sendCommand("connecting", "");
                try {
                    socket.receive(dp);
                    connected = true;
                    break;
                } catch (IOException ex) {
                    continue;
                }
            }
            if (connected) {
                System.out.println("Соединение восстановлено");
                return null;
            } else {
                System.out.println("Соединение восстановить не удалось");
                System.exit(0);
            }
        } catch (IOException e) {
            System.err.println("Гавно");
        }
        return MessageReceiver.decodeResponseObject(dp.getData());
    }
}
