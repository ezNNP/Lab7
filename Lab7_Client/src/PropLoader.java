import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropLoader {
    private PropLoader(){}

    public enum Language {
        RU, ES, SB, IT
    }

    public static ResourceBundle readProperties(Language language) {
        Properties properties = new Properties();
        String filename = "";
        ResourceBundle r;
        Locale ru = new Locale("RU");
        Locale es = new Locale("ES");
        Locale sb = new Locale("SB");
        Locale it = new Locale("IT");

        switch (language) {
            case RU: r = ResourceBundle.getBundle("GuiLabels_RU", ru); break;
            case ES: r = ResourceBundle.getBundle("GuiLabels_ES", es); break;
            case SB: r = ResourceBundle.getBundle("GuiLabels_SB", sb); break;
            case IT: r = ResourceBundle.getBundle("GuiLabels_IT", it); break;
            default:
                System.err.println("Не найден язык");
                return null;
        }
        return r;
    }
}
