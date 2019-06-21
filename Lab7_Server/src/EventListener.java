import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class EventListener {
    private Set<InetSocketAddress> addresses;
    private Set<User> users;
    private DatagramChannel channel;


    public EventListener(Set<User> users, int port) {
        this.users = users;
        addresses = new HashSet<>();
        try {
            channel = DatagramChannel.open().bind(new InetSocketAddress(port));
        } catch (Exception e) {
        }
    }

    public void updateCollection(Set<User> users) {
        this.users = users;
    }

    public void receiver() {

        try {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            buffer.clear();
            InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
            addresses.add(clientAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newEvent(String message) {
        System.out.println("Server: " + message);
        byte[] send = message.getBytes();
        System.out.println(addresses.size());
        for (InetSocketAddress addr : addresses) {
            System.out.println("Addr2: " + addr);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer.clear();
            buffer.put(send);
            buffer.flip();
            try {
                channel.send(buffer, addr);
            } catch (Exception e) {

            }
        }
    }

    public void checkUsersTimeout() {
        for (User user : users) {
            if (user.getLastRequest().compareTo(LocalDateTime.now()) < 0) {
                newEvent(String.format("Пользователь %s вышел с сервера", user.getLogin()));
            }
        }
    }

    public Set<User> getTimeoutUsers() {
        Set<User> timed = new HashSet<>();
        for (User user : users) {
            if (user.getLastRequest().compareTo(LocalDateTime.now()) < 0) {
                timed.add(user);
            }
        }
        users.removeAll(timed);
        return timed;
    }
}
