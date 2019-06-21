import java.io.Serializable;

public enum ResponseType implements Serializable {
    PLANNED,
    INFO,
    CONNECTION,
    CLIENT_ERROR,
    SERVER_ERROR
}

