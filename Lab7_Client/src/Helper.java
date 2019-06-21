import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

public class Helper {

    private Helper() {}

    /**
     * <p>Добавляет элемент в коллекцию</p>
     *
     * @param json - строка формата json
     */
    public static Human getFromJson(String json) {
        Gson gson = new Gson();
        Map<String, Object> map;
        try {
            Type type = new TypeToken<LinkedTreeMap<String, Object>>(){}.getType();
            map = (LinkedTreeMap<String, Object>) gson.fromJson(json, type);


            Hat innerHat;

            if (map.containsKey("cloth")) {
                LinkedTreeMap<String, Object> inner = (LinkedTreeMap<String, Object>) map.get("cloth");
                if (inner.containsKey("hat")) {
                    LinkedTreeMap<String, Object> hatMap = (LinkedTreeMap<String, Object>) inner.get("hat");
                    innerHat = HatFactory.newInstance(hatMap);
                    map.remove("cloth");
                    map.put("cloth", innerHat);
                    return HumanFactory.newInstance(map);
                }
            } else {
                return HumanFactory.newInstance(map);
            }
        } catch (Exception e) {
            System.err.println("Неверная строка json");
        }

        return null;
    }

    /**
     * <p>Импортирует все объекты из заданного json файла</p>
     *
     * @param pathToFile - путь до файла json
     */
    public static Vector<Human> _import(String pathToFile) {
        Vector<Human> vector = new Vector<>();
        try {
            String input = readStrings(pathToFile);

            String formattedInput = "";
            for (char c : input.toCharArray()) {
                if ((c != '\n') && (c != ' ') && (c != '\r'))
                    formattedInput += c;
            }
            Gson gson = new Gson();
            Map<String, Object> outerMap;
            try {
                Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
                }.getType();
                outerMap = (LinkedTreeMap<String, Object>) gson.fromJson(formattedInput, type);


                Hat innerHat;
                if (outerMap.containsKey("world")) {
                    ArrayList<LinkedTreeMap<String, Object>> humans = (ArrayList<LinkedTreeMap<String, Object>>) outerMap.get("world");

                    for (int i = 0; i < humans.size(); i++) {
                        LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) humans.get(i).get("human");

                        if (map.containsKey("cloth")) {
                            LinkedTreeMap<String, Object> inner = (LinkedTreeMap<String, Object>) map.get("cloth");
                            if (inner.containsKey("hat")) {
                                LinkedTreeMap<String, Object> hatMap = (LinkedTreeMap<String, Object>) inner.get("hat");
                                innerHat = HatFactory.newInstance(hatMap);
                                map.remove("cloth");
                                map.put("cloth", innerHat);
                                vector.add(HumanFactory.newInstance(map));
                            }
                        } else {
                            vector.add(HumanFactory.newInstance(map));
                        }
                    }

                } else {
                    System.err.println("Возникла проблема при добавлении объекта, проверьте вашу json строку");
                    return null;
                }

            } catch (Exception e) {
                System.err.println("Возникла проблема при добавлении объекта, проверьте вашу json строку");
                return null;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            return null;
        }
        return vector;
    }

    /**
     * <p>Читает файл</p>
     *
     * @param path - Путь до файла
     * @return массив строк, который содержит все символы файла
     *
     */
    public static String readStrings(String path) throws FileNotFoundException {
        String in = "";

        File file = new File(path);
        if (file.exists()) {
            try (BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(file))) {
                int i;
                while ((i = fileInput.read()) != -1) {
                    char c = (char) i;
                    in += c;
                }
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException();
            } catch (IOException e) {
                System.err.println("Ой-ой-ой");
            }
        } else {
            throw new FileNotFoundException();
        }

        return in.trim();
    }
}
