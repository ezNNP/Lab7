import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class EventListener extends Thread {

    private InetAddress addr;
    private int port;
    private DatagramSocket ds;
    private User user;
    private long initTime;

    public EventListener(String host, int port, User user, long initTime) {
        this.user = user;
        this.initTime = initTime;
        user.setEventListenerAddress(new InetSocketAddress(port));
        try {
            this.addr = InetAddress.getByName(host);
            this.port = port;
            this.ds = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFuckingPacket(DatagramPacket dp) throws IOException {
        for (int i = 0; i < 20; i++) {
            ds.send(dp);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                long thisTime = System.currentTimeMillis();
                byte[] d = new byte[256];
                DatagramPacket dp = new DatagramPacket(d, d.length, addr, port);
                sendFuckingPacket(dp);
                DatagramPacket data = new DatagramPacket(d, d.length);
                while (thisTime - initTime < 1000) {
                    ds.receive(data);
                    Thread.sleep(500);
                    thisTime = System.currentTimeMillis();
                    System.out.println("Sleeping");
                }
                ds.receive(data);
                System.out.println(new String(data.getData()).trim());
                System.out.print("> ");
            }
        } catch (Exception e) {

        }
    }
}
