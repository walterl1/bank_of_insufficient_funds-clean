package org.boif.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String url = "";

    public static Connection getAutoCommitConnect(){

        try{

            Connection connection = DriverManager.getConnection(url);
            return connection;
        }catch (SQLException exception){

            exception.printStackTrace();

        }
        System.out.println("Unable to connect to database at this time.");
        return null;
    }


}
