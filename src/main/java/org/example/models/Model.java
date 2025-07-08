package org.example.models;

import javax.sql.DataSource;

public abstract class Model {
    protected DataSource dataSource;

    public Model(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
