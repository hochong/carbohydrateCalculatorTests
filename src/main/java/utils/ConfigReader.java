package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found on classpath");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /** System property (-D) takes precedence over config.properties value. */
    public static boolean getBoolean(String key) {
        String sysProp = System.getProperty(key);
        return sysProp != null ? Boolean.parseBoolean(sysProp) : Boolean.parseBoolean(get(key));
    }
}
