import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CommandHandler extends Thread {

    public User lastUser;

    public CommandHandler() {

    }

    @Override
    public void run() {
    }

    private boolean isTokenValid(LocalDateTime expirationTime) {
        return expirationTime.compareTo(LocalDateTime.now()) > 0;
    }

    /**
     * <p>Ищет исполняемую команду и исполняет её</p>
     * @param com - команда
     * @param storage - ссылка на коллекцию с объектами
     */
    //public DatagramPacket handleCommand(String command, Vector<Human> storage, String data) {
    public Response handleCommand(Command com, Vector<Human> storage, User user) {
        synchronized (CommandHandler.class) {
            String command = com.getCommand();
            byte[] data = com.getData();
            String token = com.getToken();
            if (isTokenValid(user.getLastRequest())) {
                user.setLastRequest(LocalDateTime.now().plusMinutes(2));
                String buffer = null;
                switch (command.toLowerCase()) {
                    case "show":
                        buffer = show(storage);
                        break;
                    case "add":
                        buffer = add(storage, getHumanFromCommand(data), user);
                        break;
                    case "add_if_min":
                        buffer = add_if_min(storage, getHumanFromCommand(data), user);
                        break;
                    case "import":
                        buffer = _import(storage, getHumanVectorFromCommand(data), user);
                        break;
                    case "info":
                        buffer = info(storage);
                        break;
                    case "remove":
                        buffer = remove(storage, user, getStringFromCommand(data));
                        break;
                    case "help":
                        buffer = help();
                        break;
                    case "save":
                        buffer = "Collection saved";
                        break;
                }

                return new Response(Status.OK, ResponseType.PLANNED, buffer);
            } else {
                return new Response(Status.EXPIRED_TOKEN, ResponseType.PLANNED, "");
            }
        }
    }

    private Human getHumanFromCommand(byte[] data) {
        try(ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Human) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Произошла ошибки при получении объекта типа Human");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Vector<Human> getHumanVectorFromCommand(byte[] data) {
        try(ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Vector<Human>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Произошла ошибки при получении объекта типа Human");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStringFromCommand(byte[] data) {
        try(ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (String) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Произошла ошибки при получении объекта типа Human");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Показывает все данные, содержащиеся в коллекции</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     */
    public String show(Vector<Human> storage) {
        String out = "";
        for (Human human : storage) {
            out += human.toString() + "\n";
        }
        if (storage.size() == 0) {
            return "В коллекции нет элементов";
        }
        return out;
    }

    /**
     * <p>Добавляет элемент в коллекцию</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param human - объект типа Human
     */
    public String add(Vector<Human> storage, Human human, User user) {
        boolean exist = false;

        for (Human current: storage) {
            if (current.getName().toLowerCase().equals(human.getName().toLowerCase())) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            try {
                human.setOwner(user.getLogin());
                storage.add(human);
                sortCollection(storage);
                String sql;
                try {
                    Hat hat = (Hat) human.getCloth();
                    sql = "INSERT INTO collection (name, age, tall, charism, headdiametr, x, y, timeid, owner, hat_diametr, hat_height, hat_hattype, cloth, speed, fear) VALUES (?, ?, ?, ? ,? ,? ,? ,? ,? ,? , ?, ?, ?, ?, ?)";
                    PreparedStatement statement = DatabaseBridge.getConnection().prepareStatement(sql);
                    statement.setString(1, human.getName());
                    statement.setInt(2, human.getAge());
                    statement.setLong(3, human.getTall());
                    statement.setInt(4, human.getCharism());
                    statement.setFloat(5, human.getHeadDiametr());
                    statement.setInt(6, human.getX());
                    statement.setInt(7, human.getY());
                    statement.setString(8, human.getTimeID().toString());
                    statement.setString(9, human.getOwner());
                    statement.setFloat(10, hat.getDiametr());
                    statement.setInt(11, hat.getHeight());
                    statement.setString(12, hat.getHatType().name());
                    statement.setBoolean(13, true);
                    statement.setInt(14, human.getSpeed());
                    statement.setString(15, human.getFear().name());
                    statement.execute();
                    statement.close();
                } catch (NullPointerException e) {
                    sql = "INSERT INTO collection (name, age, tall, charism, headdiametr, x, y, timeid, owner, cloth, speed, fear) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = DatabaseBridge.getConnection().prepareStatement(sql);
                    statement.setString(1, human.getName());
                    statement.setInt(2, human.getAge());
                    statement.setLong(3, human.getTall());
                    statement.setInt(4, human.getCharism());
                    statement.setFloat(5, human.getHeadDiametr());
                    statement.setInt(6, human.getX());
                    statement.setInt(7, human.getY());
                    statement.setString(8, human.getTimeID().toString());
                    statement.setString(9, human.getOwner());
                    statement.setBoolean(10, false);
                    statement.setInt(11, human.getSpeed());
                    statement.setString(12, human.getFear().name());
                    statement.execute();
                    statement.close();
                }
                sortCollection(storage);
                return "Element successfully added";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Server error, database mistake";
            }
        } else {
            sortCollection(storage);
            return "Duplicate element";
        }
    }

    /**
     * <p>Добавляет элемент в коллекцию если он является уникальным</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param human - объект типа Human
     */
    public String add_if_min(Vector<Human> storage, Human human, User user) {
        if (storage.size() > 0) {
            Human min = storage.stream().min(Human::compareTo).get();
            if (human.compareTo(min) < 0) {

                return add(storage, human, user);
            } else {
                System.out.println(min);
                return "This element is not minimal";
            }
        } else {
            return add(storage, human, user);
        }
    }

    /**
     * <p>Импортирует все объекты из заданного json файла</p>
     *
     * @param storage - ссылка на коллекцию с объектом
     * @param importing - коллекция для импорта
     */
    public String _import(Vector<Human> storage, Vector<Human> importing, User user) {
        for (Human human: importing) {
            add(storage, human, user);
        }

        return "Команда import выполнена";
    }

    /**
     * <p>Выводит информацию о коллекции</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     */
    public String info(Vector<Human> storage) {
        return ("Информация о коллекции\n" +
                "Тип коллекции: " + storage.getClass() + "\n" +
                "Количество элементов в коллекции: " + storage.size());
    }

    /**
     * <p>Удаляет элемент из коллекции</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     * @param name - уникальное имя объекта
     */
    public String remove(Vector<Human> storage, User user, String name) {
        for (Human human: storage) {
            if (human.getName().toLowerCase().equals(name.toLowerCase())) {
                if (user.getLogin().equals(human.getOwner())) {
                    String sql = "DELETE FROM collection WHERE name = ?";
                    try {
                        PreparedStatement statement = DatabaseBridge.getConnection().prepareStatement(sql);
                        statement.setString(1, human.getName());
                        statement.execute();
                        statement.close();
                    } catch (SQLException e) {
                        return "Не удалось удалить объект, произошла ошибка с базой данных";
                    }
                    storage.remove(human);
                    return ("Удален человек по имени \"" + human.getName() + "\"");
                } else {
                    return "Вы не можете удалить этот объект, так как не вы его владелец";
                }
            }
        }

        return"Такого объекта не было найдено";
    }

    /**
     * <p>Выводит информацию о всех доступных командах</p>
     */
    public String help() {

        return ("Доступные команды:" +
                "\nadd {element} - добавляет элемент в коллекцию, element - строка в формате json" +
                "\nshow - выводит список всех элементов коллекции" +
                "\nimport {path} - добавляет в коллекцию все элементы из файла в формате json, path - путь до .json файла" +
                "\ninfo - выводит информацию о коллекции" +
                "\nremove {name} - удаляет элемент из коллекции, name - уникальное имя" +
                "\nadd_if_min {element} - добавляет элемент в коллекцию если он минимальный, element - строка в формате json" +
                "\nhelp - выводит список доступных команд" +
                "\nsave - сохраняет элементы в базу данных");
    }


    /**
     * <p>Сортирует коллекцию</p>
     *
     * @param storage - ссылка на коллекцию с объектами
     */
    private static void sortCollection(Vector<Human> storage) {
        Collections.sort(storage);
    }
}