import java.net.*;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

public class Client {
    private static InetAddress clientAddr;
    private static int port;

    private static Scanner scanner = new Scanner(System.in);
    private String token;
    MessageSender sender;
    private MessageReceiver receiver;
    PlannedReceiver plannedReceiver;
    private DatagramSocket socket;
    private DatagramSocket plannedSocket;
    private static String input;
    String login;

    private Client(InetAddress serverAddress, int port) {
        try {
            socket = new DatagramSocket();
            plannedSocket = new DatagramSocket();
        } catch (SocketException e) {

        }
        this.sender = new MessageSender(serverAddress, port, plannedSocket);
        this.receiver = new MessageReceiver(socket);
        this.plannedReceiver = new PlannedReceiver(plannedSocket, sender);
    }

    public static void showUsage() {
        System.out.println("To run client properly you need to follow this syntax");
        System.out.println("java Client <host> <port>");
        System.exit(1);
    }

    public void connectToServer() {
        sender.sendCommand("connecting", "");
        plannedReceiver.listenServer();
    }

    public boolean authorize() {
        System.out.println();
        System.out.println("Для авторизации на сервере нужно использовать следующие команды:");
        System.out.println("register <email> <login> <password> | Для регистрации на сервере | Все поля без пробела");
        System.out.println("login <login> <password> | Для авторизации на сервере | Все поля без пробела");
        System.out.print("Авторизация > ");
        input = scanner.nextLine();
        String command = input.split(" ")[0];
        String data = input.substring(command.length()).trim();
        String token;
        switch (command) {
            case "register":
                token = register(data);
                if (token != null) {
                    completeRegistration(data + " " + token);
                    return false;
                } else {
                    return false;
                }
            case "login":
                token = login(data);
                if (token != null) {
                    this.token = token;
                    return true;
                } else {
                    return false;
                }
            default:
                System.err.println("Вы ввели неверную команду, попробуйте еще раз");
                System.out.println("=====");
                return false;
        }
    }

    private String register(String data) {
        String[] dataArr = data.split(" ");
        if (dataArr.length != 3) {
            System.err.println("Вы использовали неверный синтаксис или пробелы, попробуйте снова");
            return null;
        }

        sender.sendCommand("register", data);
        Response response = plannedReceiver.listenServer();
        if (response == null) {
            startAgain();
        }
        if (response.getStatus() == Status.OK) {
            String token = Response.getStringFromResponse(response.getResponse());
            System.out.println("Вам на почту пришел 32-х значный токен, скопируйте его в строку ниже, чтобы подтвердить регистрацию\nБудьте внимательны, у вас всего 3 попытки");
            int attemptCounter = 0;
            while (attemptCounter < 3) {
                System.out.print("Токен > ");
                if (scanner.nextLine().trim().equals(token)) {
                    return token;
                }
                attemptCounter++;
            }
        } else if (response.getStatus() == Status.USER_EXIST) {
            System.err.println("Пользователь с таким email или login уже существует\nПопробуйте еще раз\n");
        } else if (response.getStatus() == Status.NO_MAIL) {
            System.err.println("Некорректная почта, письмо для завершения регистрации не было доставлено\nПопробуйте еще раз\n");
        }
        return null;
    }

    private boolean completeRegistration(String data) {
        sender.sendCommand("AcceptRegister", data);
        Response response = plannedReceiver.listenServer();
        if (response == null) {
            startAgain();
        }
        if (response.getStatus() == Status.OK) {
            System.out.println("Вы зарегистрированы на сервере");
            return true;
        } else if (response.getStatus() == Status.WRONG_TOKEN) {
            System.err.println("Неверный токен\n\n");
            return false;
        } else if (response.getStatus() == Status.EXPIRED_TOKEN) {
            System.err.println("Токен просрочен, поторопитесь в следующий раз, он действует всего 2.5 минуты\n\n");
            return false;
        }
        return false;
    }

    private String login(String data) {
        String[] dataArr = data.split(" ");
        if (dataArr.length != 2) {
            System.err.println("Вы использовали неверный синтаксис или пробелы, попробуйте снова");
            return null;
        }
        // Получаем и обрабатываем запрос к серверу
        sender.sendCommand("login", data);
        Response response = plannedReceiver.listenServer();
        if (response == null) {
            startAgain();
        }
        if (response.getStatus() == Status.OK) {
            return Response.getStringFromResponse(response.getResponse());
        } else if (response.getStatus() == Status.USER_IN_SYSTEM) {
            System.err.println("Пользователь с таким login уже в системе\n\n");
        } else if (response.getStatus() == Status.WRONG_PASSWORD) {
            System.err.println("Пароль неверен (Как и все, кто не верит в Аллаха)\n\n");
        } else if (response.getStatus() == Status.USER_NOT_FOUND) {
            System.err.println("Пользователь с таким login не найден\n\n");
        }

        return null;
    }

    private void start() {
        sender.sendCommandFromSpecificSocket(socket, token, "infosocket", "");
    }

    public void finish() {
        sender.sendCommand(token,"unlogin", "");
    }

    /**
     * <p>Считает количество символов в строке</p>
     *
     * @param in - Исходная строка
     * @param c - Символ, который мы ищем
     * @return Количество символов в строке
     */
    private int charCounter(String in, char c) {
        int count = 0;
        int k = 0; // считает количество "
        for (char current: in.toCharArray()) {
            if (current == c && k % 2 == 0) {
                count++;
            }
            if (current == '"') {
                k++;
            }
        }
        return count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            showUsage();
        }
        Date date = new Date();
        System.out.println(date);

        try {
            clientAddr = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
            InetAddress addr = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            Client client = new Client(addr, port);
            Thread f = new Thread(() -> {
                client.finish();
                System.out.println("Пока-пока");
            });
            Runtime.getRuntime().addShutdownHook(f);
            Authorization authorization = new Authorization(client);
            authorization.createWindow();

            System.out.println("-- Running UDP Client at " + InetAddress.getLocalHost() + " --");
            System.out.println("-- UDP client settings --");
            System.out.println("-- UDP connection to " + addr + " host --");
            System.out.println("-- UDP port " + port + " --");
            System.out.println("Client started");
            /*
            0 - подключение к серверу
            1 - авторизация
            2 - команды
            3 - конец программы (отправляем на сервер то, что мы отключаемся)
             */
            // TODO: 2019-05-20 Uncomment
            //client.connectToServer();
            client.setToken(null);
            boolean authorized = client.authorize();
            while (!authorized && client.getToken() == null) {
                authorized = client.authorize();
            }
            client.start();

        } catch (Exception e) {
            e.printStackTrace();
            showUsage();
        }
    }

    public static void startAgain() {
        Client client = new Client(clientAddr, port);
        client.setToken(null);
        client.connectToServer();
        boolean authorized = client.authorize();
        while (!authorized && client.getToken() == null) {
            authorized = client.authorize();
        }
        Thread f = new Thread(() -> {
            client.finish();
        });
        Runtime.getRuntime().addShutdownHook(f);
        client.start();
    }



    public Scanner getScanner() {
        return scanner;
    }
}