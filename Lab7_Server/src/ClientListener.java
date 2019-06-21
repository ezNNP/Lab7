import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientListener extends Thread {

    private SocketAddress saddr;
    private ByteBuffer channelBuffer;
    private Command command;
    private boolean ready;
    private DatagramChannel channel;

    public ClientListener(DatagramChannel channel) {
        this.channel = channel;
        this.ready = false;
        this.channelBuffer = ByteBuffer.allocate(8192);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.channelBuffer.clear();
                this.saddr = channel.receive(channelBuffer);
                System.out.println("Получен запрос");
                this.command = readSerializedCommand();
                this.ready = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getDataFromLastCommand() {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(command.getData());
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Возникла ошибка при десеарелизации данных от последней команды");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Не найден класс");
        }
        return null;
    }

    public boolean isReady() {
        return ready;
    }

    public Command getCommand() {
        this.ready = false;
        return command;
    }

    /**
     * Читает сериализованную команду
     * @return объект типа Command
     */
    private Command readSerializedCommand() {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(channelBuffer.array());
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Command) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SocketAddress getSaddr() {
        return saddr;
    }
}
