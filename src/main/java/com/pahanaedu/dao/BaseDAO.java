package com.pahanaedu.dao;

import com.pahanaedu.helpers.DatabaseHelper;

import java.sql.Connection;
import java.sql.SQLException;


public abstract class BaseDAO {

    protected final DatabaseHelper databaseHelper;

    protected BaseDAO() {
        this.databaseHelper = DatabaseHelper.getInstance();
    }


    protected Connection getConnection() throws SQLException {
        return databaseHelper.getConnection();
    }

    protected void closeConnection(Connection connection) {
        databaseHelper.closeConnection(connection);
    }
}
