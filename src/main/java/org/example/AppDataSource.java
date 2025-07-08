package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class AppDataSource extends HikariDataSource {
    private static AppDataSource instance;

    private AppDataSource(HikariConfig config) {
        super(config);
    }

    /**
     * Returns a singleton instance of a DataSource.
     *
     * @return a DataSource instance
     */
    public static HikariDataSource getInstance() {
        if (instance == null) {
            instance = new AppDataSource(new HikariConfig("hikari.properties"));
        }

        return instance;
    }
}
