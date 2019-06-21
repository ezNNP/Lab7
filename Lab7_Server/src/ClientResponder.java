import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientResponder {

    private DatagramChannel channel;

    public ClientResponder (DatagramChannel channel) {
        this.channel = channel;
    }

    public void sendResponse(SocketAddress addr, Response response) {
        ByteBuffer out = serializeResponse(response);
        try {
            channel.send(out, addr);
        } catch (IOException e) {
            System.err.println("Возникла ошибка при отправлении пакета");
        }
    }

    private ByteBuffer serializeResponse(Response response) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
            return ByteBuffer.wrap(baos.toByteArray());
        } catch (IOException e) {
            System.err.println("Произошла ошибка при сериализации ответа");
        }
        return null;
    }
}
