package project.pbo.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final Properties PROPERTIES = new Properties();
    // Membaca file konfigurasi kredensial (application.properties) dari direktori resource
    static {
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input != null) {
                PROPERTIES.load(input);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Config() {}

    public static String get(String key, String defaultValue) {
        //Mengecek Environment Variable pada sistem OS terlebih dahulu sebagai prioritas utama keamanan sebelum membaca teks di file konfigurasi
        String envValue = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return PROPERTIES.getProperty(key, defaultValue);
    }
}