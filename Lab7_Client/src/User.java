import java.io.Serializable;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class User implements Serializable {
    private static final long serialVersionUID = 1;
    private String email;
    private String login;
    private String password;
    private String clientSalt;
    private String token;
    private String status;
    private LocalDateTime lastRequest;
    private boolean loggedIn;
    private boolean activated;
    private InetSocketAddress address;
    private InetSocketAddress eventListenerAddress;
    private int port;
    private String registerToken;

    public User(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
        loggedIn = false;
        activated = false;
    }

    public User(String login) {
        this.login = login;
        loggedIn = false;
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

    public String getClientSalt() {
        return clientSalt;
    }

    public void setClientSalt(String clientSalt) {
        this.clientSalt = clientSalt;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(LocalDateTime lastRequest) {
        this.lastRequest = lastRequest;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    public InetSocketAddress getEventListenerAddress() {
        return eventListenerAddress;
    }

    public void setEventListenerAddress(InetSocketAddress eventListenerAddress) {
        this.eventListenerAddress = eventListenerAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (!login.equals(user.login)) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (clientSalt != null ? !clientSalt.equals(user.clientSalt) : user.clientSalt != null) return false;
        return token != null ? token.equals(user.token) : user.token == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + login.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (clientSalt != null ? clientSalt.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", clientSalt='" + clientSalt + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}