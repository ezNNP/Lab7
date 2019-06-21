import java.io.Serializable;

public class Hat extends Cloth implements Serializable {

    private boolean isOn;
    private float diametr;
    private int height;
    private HatType hatType;

    public Hat(float diametr, int height, HatType hatType) {
        this.isOn = false;
        this.diametr = diametr;
        this.height = height;
        this.hatType = hatType;
    }

    public Hat(float diametr, int height) {
        this.isOn = false;
        this.diametr = diametr;
        this.height = height;
        this.hatType = HatType.DEFAULT;
    }

    @Override
    public void putOn(Human human) {
        if (isOn) {
            System.out.println("Шляпа уже надета, не стоит одевать вторую");
        } else if ((this.diametr - human.getHeadDiametr() < 2.0) && (this.diametr - human.getHeadDiametr() > 0.5)) {
            System.out.println("Мы надели шляпу на " + human.getName());
            isOn = true;
        } else {
            System.out.println("Шляпа слишком мала для " + human.getName());
        }
    }

    @Override
    public void takeOff(Human human) {
        if (isOn) {
            System.out.println("Шляпа снята");
            isOn = false;
        } else {
            System.out.println("Нечего больше снимать, подумой!");
        }
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public float getDiametr() {
        return diametr;
    }

    public void setDiametr(float diametr) {
        this.diametr = diametr;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public HatType getHatType() {
        return hatType;
    }

    public void setHatType(HatType hatType) {
        this.hatType = hatType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hat hat = (Hat) o;

        if (isOn != hat.isOn) return false;
        if (Float.compare(hat.diametr, diametr) != 0) return false;
        if (height != hat.height) return false;
        return hatType == hat.hatType;
    }

    @Override
    public int hashCode() {
        int result = (isOn ? 1 : 0);
        result = 31 * result + Math.round(diametr);
        result = 31 * result + height;
        result = 31 * result + (hatType != null ? hatType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Hat{" +
                "isOn=" + isOn +
                ", diametr=" + diametr +
                ", height=" + height +
                ", hatType=" + hatType +
                '}';
    }
}