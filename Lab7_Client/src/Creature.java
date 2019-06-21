public abstract class Creature implements Creatureable {

    private int x, y;
    private int speed;
    private String name;
    private Fear fear;

    public Creature(String name, int x, int y, int speed, Fear fear) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.fear = fear;
    }




    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void sound(Sounds type, String message) {
        switch (type) {
            case NORMAL:
                break;
            case SHOUT:
                message = message.toUpperCase();
                break;
            case WHISPER:
                message = "*шепотом*" + message.toLowerCase() + "...";
                break;
            case LOUD:
                message = firstUpperCase(message) + "!";
        }

        System.out.println(this.name + ": " + message);
    }

    private String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public Fear getFear() {
        return fear;
    }

    public void setFear(Fear fear) {
        this.fear = fear;
    }
}