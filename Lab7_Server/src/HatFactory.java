import java.util.Map;

public class HatFactory {
    private HatFactory(){}

    /**
     * <p>Возвращает новый объект типа Hat</p>
     *
     * @param params - параметры для создания объекта
     * @return Новый объект типа Hat
     * @throws Exception при недостаточном количестве параметров
     */
    public static Hat newInstance(Map<String, Object> params) {
        Hat hat = null;

        float diametr = 21.0F;
        int height = 15;
        try {
            try {
                diametr = Float.parseFloat(params.get("diametr").toString());
            } catch (NumberFormatException e) {
                diametr = (float) params.get("diametr");
            }
            try {
                height = Integer.parseInt(params.get("height").toString());
            } catch (NumberFormatException e) {
                height = (int) params.get("height");
            }
            if (params.containsKey("hatType")) {
                hat = new Hat(diametr, height, HatType.valueOf(params.get("hatType").toString()));
            } else {
                hat = new Hat(diametr, height);
            }
        } catch (Exception e) {
            hat = new Hat(diametr, height);
        }

        return hat;
    }
}
