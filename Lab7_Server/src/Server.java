import java.io.*;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private Vector<Human> storage;

    private Map<String, LocalDateTime> authorizationTokens = new ConcurrentHashMap<>(); // токены авторизации, ключ - токен, значение - время его деактивации
    private Set<User> activeUsers = new HashSet<>(); // клиенты, которые находятся на сервере в данный момент
    private Set<User> allUsers = new HashSet<>(); // клиенты. которые когда-либо были зарегистрированы на сервере
    private Set<SocketAddress> allAddresses = new HashSet<>();

    private ClientListener listener;
    private ClientResponder responder;


    public Server(int port) throws IOException {
        System.out.println("Server is running on");
        System.out.println("address: " + InetAddress.getLocalHost());
        System.out.println("port: " + port);
        DatabaseBridge.createConnection();
        DatagramChannel channel = DatagramChannel.open().bind(new InetSocketAddress(port));
        responder = new ClientResponder(channel);
        listener = new ClientListener(channel);
    }

    public void loadCollection() {
        System.out.println("Initializing collection...");
        storage = Loaders.loadFromDB();
    }

    public void loadUsers() {
        System.out.println("Initializing users...");
        allUsers = Loaders.loadUsersFromDB();
    }

    private User getUserByToken(String token) {
        System.out.println(token);
        for (User client: activeUsers) {
            try {
                if (client.getToken().equals(token)) {
                    return client;
                } else {
                    System.out.println("===");
                    System.out.println(client.getToken());
                    System.out.println("===");
                }
            } catch (NullPointerException e) {

            }
        }
        System.err.println("Пользователь не был найден");
        return null;
    }

    private void notifyAllActiveUsersExcept(User user) {
        Response response = new Response(Status.OK, ResponseType.INFO, String.format("Пользователь %s вышел с сервера", user.getLogin()));
        for (User active : activeUsers) {
            responder.sendResponse(active.getInfoSocket(), response);
        }
    }

    private void notifySpecificUser(User user) {
        Response response = new Response(Status.OK, ResponseType.INFO, "Время ожидания истекло");
        responder.sendResponse(user.getInfoSocket(), response);
    }

    private void finish() {
        Response response = new Response(Status.OK, ResponseType.INFO, "Сервер выключен, заходите позже");
        for (SocketAddress address : allAddresses)
            responder.sendResponse(address, response);
    }

    private void listen() throws Exception {
        System.out.println("-- Running Server at " + InetAddress.getLocalHost() + " --");

        CommandHandler handler;

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            for (User user : activeUsers) {
                if (user.getLastRequest().compareTo(LocalDateTime.now()) < 0) {
                    System.out.println("Пользователь " + user.getLogin() + " вышел с сервера");
                    notifySpecificUser(user);
                    activeUsers.remove(user);
                    notifyAllActiveUsersExcept(user);
                } else {
                }
            }
        };
        service.scheduleWithFixedDelay(task, 0, 500, TimeUnit.MILLISECONDS);
        Command command;
        while (true) {
            while (!listener.isReady()) {
                Thread.sleep(10);
            }

            SocketAddress saddr = listener.getSaddr();
            command = listener.getCommand();
            Object data = listener.getDataFromLastCommand();
            handler = new CommandHandler();
            try {
                System.out.println(1);
                User user = getUserByToken(command.getToken());
                Response response = null;
                System.out.println("Client input: " + command.getCommand());
                if (!command.getCommand().equals("unlogin") && !command.getCommand().equals("infosocket")) {
                    response = handler.handleCommand(command, storage, user);
                    responder.sendResponse(user.getSaddr(), response);
                    System.out.println("Отправлен ответ");
                } else if (command.getCommand().equals("infosocket")) {
                    user.setInfoSocket(saddr);
                    System.out.println(user.getInfoSocket());
                    allAddresses.add(saddr);
                } else {
                    System.out.println("Removing: " + activeUsers.remove(user));
                    notifyAllActiveUsersExcept(user);
                }
            } catch (NullPointerException e) {
                System.out.println(2);
                String strData = (String) data;
                Response response = null;
                switch (command.getCommand().toLowerCase().trim()) {
                    case "register":
                        response = ServerAuthorization.registerUser(strData, authorizationTokens, allUsers);
                        break;
                    case "login":
                        response = ServerAuthorization.loginUser(strData, activeUsers, allUsers, saddr);
                        System.out.println("login");
                        break;
                    case "acceptregister":
                        response = ServerAuthorization.completeRegistration(strData, authorizationTokens, allUsers);
                        break;
                    case "connecting":
                        response = new Response(Status.OK, ResponseType.CONNECTION, "");
                        break;
                }
                responder.sendResponse(saddr, response);
                System.out.println("Отправлен ответ");
            }
        }
    }

    public static void showUsage() {
        System.out.println("To run server properly follow this syntax");
        System.out.println("java -jar Server.jar <port>");
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {


        int input_port = -1;
        if (args.length != 1) {
            showUsage();
        }

        /*
        1 - загрузка коллекции из базы данных
        2 - загрузка пользователей из базы данных
        3 - инициализация приёмника
        4 - инициализация отправителя
        5 - старт приёма команд
        6 - оповещение пользователей об остановке сервера
        7 - сохранение allUser
        8 - сохранение коллекции
         */

        try {
            input_port = Integer.parseInt(args[0]);
        } catch (IllegalArgumentException e) {
            showUsage();
        }

        Server server = new Server(input_port);

        server.loadCollection();
        server.loadUsers();
        server.listen();
    }
}