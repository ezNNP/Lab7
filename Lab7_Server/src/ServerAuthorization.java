import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.net.SocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class ServerAuthorization {

    private static final byte[] salt = {33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59, 61, 63};

    public static Response registerUser(String data, Map<String, LocalDateTime> authorizationTokens, Set<User> allUsers) {
        String split = "\\uE000";
        String[] dataArr = data.split(split);
        String email        = dataArr[0];
        String login        = dataArr[1];
        String password     = dataArr[2];
        ResponseType type = ResponseType.PLANNED;
        Status status = Status.OK;
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) { status = Status.USER_EXIST; break; }
            if (user.getLogin().equals(login)) { status = Status.USER_EXIST; break; }
        }
        RandomString rnd = new RandomString();
        String rndStr = rnd.nextString();
        while (authorizationTokens.containsKey(rndStr)) {
            rndStr = rnd.nextString();
        }

        if (status != Status.USER_EXIST) {
            status = SendEmail.send(email, rndStr);
        }

        if (status == Status.OK) {
            authorizationTokens.put(rndStr, LocalDateTime.now().plusMinutes(2).plusSeconds(30));
        }

        return new Response(status, type, rndStr);

    }

    public static Response loginUser(String data, Set<User> activeUsers, Set<User> allUsers, SocketAddress saddr) {
        String split = "\\uE000";
        String[] dataArr = data.split(split);
        System.out.println();
        String login =      dataArr[0];
        String password =   generateStrongPasswordHash(dataArr[1]);
        ResponseType type = ResponseType.PLANNED;
        Status status = Status.USER_NOT_FOUND;
        User thisUser = null;

        for (User user : allUsers) {
            if (user.getLogin().equals(login)) { status = Status.OK; thisUser = user; break; }
        }

        for (User user : activeUsers) {
            if (user.getLogin().toLowerCase().equals(login.toLowerCase())) {
                status = Status.USER_IN_SYSTEM; break;
            } else {
                System.out.println(user.getLogin());
            }
        }


        if (status == Status.OK && !password.equals(thisUser.getPassword())) {
            System.out.println(password + ":" + thisUser.getPassword());
            status = Status.WRONG_PASSWORD;
        }
        if (status == Status.OK && password.equals(thisUser.getPassword())) {
            RandomString gen = new RandomString();
            thisUser.setLastRequest(LocalDateTime.now().plusMinutes(2));
            String genToken = gen.nextString();
            thisUser.setToken(genToken);
            thisUser.setSaddr(saddr);
            activeUsers.add(thisUser);
            System.out.println("Token: " + genToken);
            return new Response(status, type, genToken);
        }
        return new Response(status, type, "");
    }

    public static Response completeRegistration(String data, Map<String, LocalDateTime> authorizationTokens, Set<User> allUsers) {
        String split = "\\uE000";
        String[] dataArr = data.split(split);
        String email =      dataArr[0];
        String login =      dataArr[1];
        String password =   generateStrongPasswordHash(dataArr[2]);
        String token =      dataArr[3];
        ResponseType type = ResponseType.PLANNED;
        Status status = Status.OK;

        if (!authorizationTokens.containsKey(token)) { status = Status.WRONG_TOKEN; }
        if (authorizationTokens.get(token).compareTo(LocalDateTime.now()) < 0) { status = Status.EXPIRED_TOKEN; }

        if (status == Status.OK) {
            User user = new User(email, login, password);
            allUsers.add(user);
            String sql = "INSERT INTO users (login, password, email) VALUES (?, ?, ?)";
            try {
                PreparedStatement statement = DatabaseBridge.getConnection().prepareStatement(sql);
                statement.setString(1, login);
                statement.setString(2, password);
                statement.setString(3, email);
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                System.err.println("ERROR: произошла ошибка при исполнении SQL запроса");
            }
        }
        return new Response(status, type, "");
    }


    private static String generateStrongPasswordHash(final String password) {
        try {
            int iterations = 1000;
            char[] chars = password.toCharArray();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 128);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return toHex(hash);
        } catch (Exception e) {

            return null;
        }
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
