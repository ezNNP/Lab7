import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Loaders {
    private Loaders() {}

    public static Vector<Human> loadFromDB() {
        ResultSet resultSet = DatabaseBridge.makeRequest("SELECT * FROM collection");
        Vector<Human> humans = new Vector<>();
        try {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                long tall = resultSet.getInt("tall");
                int speed = resultSet.getInt("speed");
                int charism = resultSet.getInt("charism");
                float headDiametr = resultSet.getFloat("headDiametr");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String timeID = resultSet.getString("timeID");
                String owner = resultSet.getString("owner");
                Fear fear = Fear.valueOf(resultSet.getString("fear"));
                Human human = new Human(name, x, y, speed, age, tall, charism, fear, headDiametr);
                human.setOwner(owner);
                human.setTimeID(LocalDateTime.parse(timeID));

                boolean cloth = resultSet.getBoolean("cloth");
                if (cloth) {
                    float diametr = resultSet.getFloat("hat_diametr");
                    int height = resultSet.getInt("hat_height");
                    HatType hatType = HatType.valueOf(resultSet.getString("hat_hatType"));
                    Hat hat = new Hat(diametr, height, hatType);
                    human.setCloth(hat);
                }
                humans.add(human);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return humans;
    }

    public static Set<User> loadUsersFromDB() {
        ResultSet resultSet = DatabaseBridge.makeRequest("SELECT * FROM users");
        Set<User> users = new HashSet<>();
        try {
            while (resultSet.next()) {
                try {
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    User user = new User(email, login, password);

                    users.add(user);
                } catch (SQLException e) {

                }
            }
        } catch (Exception e) {
            // DEBUG

        }
        return users;
    }
}
