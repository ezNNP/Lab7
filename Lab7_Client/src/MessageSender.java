import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MessageSender {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int port;
    private Command lastCommand;
    private boolean sent;

    public MessageSender(InetAddress serverAddress, int port, DatagramSocket socket) {
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void sendCommandFromSpecificSocket(DatagramSocket socket, String token, String command, Object data) {
        byte[] serializedData = writeDataToByteArray(data);
        Command c = new Command(token, command, serializedData);
        byte[] sending = writeCommandToByteArray(c);
        DatagramPacket dp = new DatagramPacket(sending, sending.length, serverAddress, port);
        this.lastCommand = c;
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Посылает запрос к серверу без токена
     * @param command команда
     * @param data данные
     */
    public void sendCommand(String command, Object data) {
        byte[] serializedData = writeDataToByteArray(data);
        Command c = new Command(command, serializedData);
        this.lastCommand = c;
        byte[] sending = writeCommandToByteArray(c);
        DatagramPacket dp = new DatagramPacket(sending, sending.length, serverAddress, port);
        sendDatagramPacket(dp);
        this.sent = true;
    }

    /**
     * Посылает запрос серверу с токеном
     * @param token токен
     * @param command команда
     * @param data данные
     */
    public void sendCommand(String token, String command, Object data) {
        byte[] serializedData = writeDataToByteArray(data);
        Command c = new Command(token, command, serializedData);
        this.lastCommand = c;
        byte[] sending = writeCommandToByteArray(c);
        DatagramPacket dp = new DatagramPacket(sending, sending.length, serverAddress, port);
        sendDatagramPacket(dp);
        this.sent = true;
    }

    /**
     * Сериализует объект типа Command
     * @param command объект типа Command
     * @return сериализованный массив byte
     */
    private byte[] writeCommandToByteArray(Command command) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(command);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Не удалось записать объект команды");
        }
        return new byte[]{1, 2, 3};
    }

    /**
     * Сериилизует данные, передаваемые клиентом
     * @param data данные
     * @return сериализованный массив byte
     */
    private byte[] writeDataToByteArray(Object data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(data);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Не удалось записать передоваемые командой данные");
        }
        return new byte[]{1, 2, 3};
    }

    /**
     * Посылает пакет по udp соединению
     * @param packet пакет
     */
    private void sendDatagramPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("Не удалось послать пакет по UDP соединению");
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Command getLastCommand() {
        return lastCommand;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
