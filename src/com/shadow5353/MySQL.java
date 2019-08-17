package com.shadow5353;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Jacob on 16-03-2018.
 */
public class MySQL {
    private String host, username, password, database;
    private int port;
    private Statement statement;
    private Connection connection;

    private MySQL() {
        this.host = StaffNotes.getPlugin().getConfig().get("mysql.host").toString();
        this.username = StaffNotes.getPlugin().getConfig().get("mysql.username").toString();
        this.password = StaffNotes.getPlugin().getConfig().get("mysql.password").toString();
        this.port = Integer.parseInt(StaffNotes.getPlugin().getConfig().get("mysql.port").toString());
        this.database = StaffNotes.getPlugin().getConfig().get("mysql.database").toString();

        try {
            openConnection();
            statement = connection.createStatement();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static MySQL instance = new MySQL();

    public static MySQL getInstance() {
        return instance;
    }

    public void startUp() {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `players` (" +
                    "`fldID` int(255) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`fldUUID` varchar(255) NOT NULL," +
                    "`fldNote` text CHARACTER SET utf16 COLLATE utf16_danish_ci NOT NULL," +
                    "`fldAdmin` varchar(255) NOT NULL," +
                    "`fldTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1\n;");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }
}
