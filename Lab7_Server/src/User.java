import java.net.InetAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;

public class User {
    private String email;
    private String login;
    private String password;
    private String token;
    private LocalDateTime lastRequest;
    private int port;
    private SocketAddress saddr;
    private SocketAddress infoSocket;

    public User(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public User(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(LocalDateTime lastRequest) {
        this.lastRequest = lastRequest;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SocketAddress getSaddr() {
        return saddr;
    }

    public void setSaddr(SocketAddress saddr) {
        this.saddr = saddr;
    }

    public SocketAddress getInfoSocket() {
        return infoSocket;
    }

    public void setInfoSocket(SocketAddress infoSocket) {
        this.infoSocket = infoSocket;
    }
}