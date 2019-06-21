import java.io.*;
import java.util.Arrays;

public class Response implements Serializable {
    private static long serialVersionUID = 1;
    private Status status;
    private ResponseType type;
    private byte[] response; // SERIALIZED VALUE возможно переделать в String

    public Response(Status status, ResponseType type, byte[] response) {
        this.status = status;
        this.type = type;
        this.response = response;
    }

    public Response(Status status, ResponseType type, String response) {
        this.status = status;
        this.type = type;
        this.response = writeResponse(response);
    }

    public Response(ResponseType type) {
        this.type = type;
    }


    private byte[] writeResponse(String response) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Произошла ошибка при сериализации строки ответа");
        }
        return null;
    }

    public static String getStringFromResponse(byte[] response) {
        return (String) readResponse(response);
    }

    public static User getUserFromResponse(byte[] response) {
        return (User) readResponse(response);
    }

    private static Object readResponse(byte[] response) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(response);
             ObjectInputStream ois = new ObjectInputStream(bais)){
            return ois.readObject();
        } catch (IOException e) {
            System.err.println("Невозможно прочитать ответ сервера (Class Response, method readResponse)");
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден класс");
        }
        return null;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", type=" + type +
                ", response=" + Arrays.toString(response) +
                '}';
    }
}