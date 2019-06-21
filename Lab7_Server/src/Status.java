import java.io.Serializable;

public enum Status implements Serializable {
    OK,
    USER_EXIST,
    ERROR,
    UNKNOWN_COMMAND,
    NO_MAIL,
    USER_IN_SYSTEM,
    USER_NOT_FOUND,
    WRONG_PASSWORD,
    WRONG_TOKEN,
    EXPIRED_TOKEN
}
