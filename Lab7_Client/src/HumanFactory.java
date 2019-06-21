import java.util.Map;

public class HumanFactory {

    private HumanFactory() {}

    /**
     * <p>Возвращает новый объект типа Human</p>
     *
     * @param params - параметры для создания Human
     * @return Новый объект типа Human
     * @throws Exception при недостаточном количестве параметров
     */
    public static Human newInstance(Map<String, Object> params) throws Exception {
        if (params.containsKey("name")) {
            Human human = new Human(params.get("name").toString());
            if (params.containsKey("age"))
                try {
                    human.setAge(Integer.parseInt(params.get("age").toString()));
                } catch (NumberFormatException e) {
                    human.setAge((int) Math.round((double) params.get("age")));
                }
            if (params.containsKey("tall"))
                try {
                    human.setTall(Long.parseLong(params.get("tall").toString()));
                } catch (NumberFormatException e) {
                    human.setTall((long) Math.round((double) params.get("tall")));
                }
            if (params.containsKey("charism"))
                try {
                    human.setCharism(Integer.parseInt(params.get("charism").toString()));
                } catch (NumberFormatException e) {
                    human.setCharism((int) Math.round((double) params.get("charism")));
                }
            if (params.containsKey("headDiametr"))
                try {
                    human.setHeadDiametr(Float.parseFloat(params.get("headDiametr").toString()));
                } catch (NumberFormatException e) {
                    human.setHeadDiametr((float) params.get("headDiametr"));
                }
            if (params.containsKey("x"))
                try {
                    human.setX(Integer.parseInt(params.get("x").toString()));
                } catch (NumberFormatException e) {
                    human.setX((int) Math.round((double) params.get("x")));
                }
            if (params.containsKey("y"))
                try {
                    human.setY(Integer.parseInt(params.get("y").toString()));
                } catch (NumberFormatException e) {
                    human.setY((int) Math.round((double) params.get("y")));
                }
            if (params.containsKey("cloth"))
                human.setCloths((Cloth) params.get("cloth"));
            return human;
        } else {
            throw new Exception();
        }
    }
}
