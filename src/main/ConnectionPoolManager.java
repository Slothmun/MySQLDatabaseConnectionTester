/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author Slothmun
 */
public class ConnectionPoolManager {

    private final String databaseName;
    private final String databaseUrl;
    private final String userName;
    private final String password;
    private final String port;
    private final int poolSize;
    private final Log log;
    private final Dialog dialog;
    private final Main main;
    private int connCount;
    private boolean connError;

    Vector connectionPool;

    public ConnectionPoolManager(String databaseName, String databaseUrl, String port, String userName, String password, String poolSize, Log log, Main main) {
        this.connectionPool = new Vector();
        this.databaseName = databaseName;
        this.databaseUrl = databaseUrl;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.poolSize = Integer.parseInt(poolSize);
        this.log = log;
        this.main = main;

        connError = false;

        dialog = new Dialog();

        initialize();
    }

    private void initialize() {
        //Here we can initialize all the information that we need
        main.setProgressMax(poolSize);
        initializeConnectionPool();
    }

    private void initializeConnectionPool() {
        while (!checkIfConnectionPoolIsFull()) {
            System.out.println("Connection Pool is NOT full. Proceeding with adding new connections");
            //Adding new connection instance until the pool is full
            connectionPool.addElement(createNewConnectionForPool());
            barProgress();
        }
        if (!connError) {
            System.out.println("Connection Pool is full. " + connCount + " successfull connection(s).");
            dialog.showInfo("Connection Pool is full. " + connCount + " successfull connection(s).", main);
        } else {
            dialog.showError("Error: There was an error during the creation of 1 or more of the connections. Please see the log for details.", main);
        }
    }

    private synchronized boolean checkIfConnectionPoolIsFull() {
        return connectionPool.size() >= poolSize;
    }

    //Creating a connection
    private Connection createNewConnectionForPool() {
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + databaseUrl + ":" + port + "/" + databaseName, userName, password);
            System.out.println("New connection: " + connection + " successful.");
            log.append("New connection: \"" + connection + "\" successful.");
            connCount++;
        } catch (SQLException sqle) {
            System.err.println("SQLException: " + sqle);
            log.append(sqle.getMessage());
            connError = true;
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException: " + cnfe);
            log.append(cnfe.getMessage());
            connError = true;
            return null;
        }

        return connection;
    }

    public synchronized Connection getConnectionFromPool() {
        Connection connection = null;

        //Check if there is a connection available. There are times when all the connections in the pool may be used up
        if (connectionPool.size() > 0) {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        return connection;
    }

    public synchronized void returnConnectionToPool(Connection connection) {
        //Adding the connection from the client back to the connection pool
        connectionPool.addElement(connection);
    }

    private void barProgress() {
        main.barProgress();
    }

}
