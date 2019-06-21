import java.io.Serializable;

public abstract class Cloth implements Serializable {
    abstract void putOn(Human human);
    abstract void takeOff(Human human);
}