import java.io.Serializable;

public class Command implements Serializable {

    private static final long serialVersionUID = 1;

    private String token;
    private String command;
    private byte[] data; // TODO: 2019-04-13

    public Command(String command, byte[] data) {
        this.command = command;
        this.data = data;
    }

    public Command(String token, String command, byte[] data) {
        this.token = token;
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", data=" + data +
                '}';
    }
}
