import java.io.Serializable;

public interface Creatureable extends Serializable {
	void sound(Sounds type, String message);
}