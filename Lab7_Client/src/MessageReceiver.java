import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class MessageReceiver extends Thread {
    private DatagramSocket socket;
    private Response plannedResponse;
    private boolean ready;
    private boolean err;

    public MessageReceiver(DatagramSocket socket) {
        this.socket = socket;
        this.ready = false;
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] data = new byte[8192];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                this.socket.receive(packet);
                Response response = decodeResponseObject(packet.getData());
                switch (response.getType()) {
                    case PLANNED:
                        plannedResponse = response;
                        this.ready = true;
                        break;
                    case INFO:
                        showInfo(response);
                        break;
                    case CLIENT_ERROR:
                        System.err.println("Произошла ошибка на стороне клиента");
                        break;
                    case SERVER_ERROR:
                        System.err.println("Произошла ошибка на стороне сервера");
                        break;
                    case CONNECTION:
                        this.socket.setSoTimeout(0);
                        break;
                }
            } catch (SocketTimeoutException e) {
                System.err.println("Потеряно соединение с сервером");
                System.exit(0);
            } catch (IOException e) {
                System.err.println("Произошла ошибка при получении пакета");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Декодирует Object response в Response
     * @param serialized массив byte serializable
     * @return объект типа Response
     */

    public static Response decodeResponseObject(byte[] serialized) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            return (Response) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Возникла ошибка при декодировании ответа сервера");
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден класс");
        }
        return new Response(ResponseType.CLIENT_ERROR);
    }

    /**
     * Выводит ответ, если его тип - ResponseType.INFO
     * @param response ответ сервера
     */
    public static void showInfo(Response response) {
        String responseString = Response.getStringFromResponse(response.getResponse());
        System.out.println();
        System.out.println("Server: " + responseString);

        if (responseString.trim().equals("Время ожидания истекло")) {
            System.exit(0);
        }
        System.out.print("> ");
    }


    /**
     * Возвращает последний response на command
     * @return Response
     */
    public Response getLastSendedResponse() {
        this.ready = false;
        return plannedResponse;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }
}
