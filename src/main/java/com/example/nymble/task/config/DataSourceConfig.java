package com.example.nymble.task.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Configuration
public class DataSourceConfig {


    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/Nymble";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    @Bean
    public DataSource getDataSource() throws SQLException {
        var dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(DRIVER);
        dataSourceBuilder.url(URL);
        dataSourceBuilder.username(USER_NAME);
        dataSourceBuilder.password(PASSWORD);

        return dataSourceBuilder.build();

    }


    public PreparedStatement getPreparedStatement(String query, Connection con) throws SQLException {
        return con.prepareStatement(query);
    }


}

