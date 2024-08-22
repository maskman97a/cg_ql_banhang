package vn.codegym.qlbanhang.config;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.InputStream;
import java.util.Properties;

@WebServlet
@Getter
@Setter
public class PropertiesConfig extends HttpServlet {
    public static final PropertiesConfig inst = new PropertiesConfig();
    private static final String ENV = System.getenv("ENVIRONMENT");

    private Properties properties;

    private PropertiesConfig() {
        this.properties = new Properties();
        try {
            String propertiesPath = "/application.properties";
            if (ENV != null && !ENV.isEmpty()) {
                propertiesPath = "/application-" + ENV + ".properties";
            }
            InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesPath);
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception ex) {
            log(ex.getMessage());
        }
    }

    public static PropertiesConfig getInstance() {
        return inst;
    }

    public static String getProperty(String key) {
        return getInstance().getProperties().getProperty(key);
    }
}
