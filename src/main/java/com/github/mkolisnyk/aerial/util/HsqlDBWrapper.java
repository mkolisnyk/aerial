/**
 * .
 */
package com.github.mkolisnyk.aerial.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hsqldb.Server;

/**
 * @author Myk Kolisnyk
 *
 */
public class HsqlDBWrapper {

    private static final Logger LOG = LoogerFactory.create(HsqlDBWrapper.class);

    private String dbName;
    private Server hsqlServer = null;
    private Connection connection;
    /**
     * .
     */
    public HsqlDBWrapper(String dbNameValue) {
        LOG.info("Initializing DB: " + dbNameValue);
        this.dbName = dbNameValue;
    }

    public void startServer() throws Exception {
        LOG.info("Starting local DB server");
        connection = null;
        hsqlServer = new Server();
        hsqlServer.setLogWriter(null);
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, this.dbName);
        hsqlServer.setDatabasePath(0, "file:" + this.dbName);
        hsqlServer.start();
        LOG.info("Started");
    }

    public void openConnection() throws Exception {
        LOG.info("Creating new DB connection");
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/" + this.dbName, "sa", "");
        LOG.info("Created");
    }

    public void closeConnection() throws Exception {
        LOG.info("Closing DB connection");
        connection.close();
        connection = null;
        LOG.info("Closed");
    }

    public void stopServer() throws Exception {
        LOG.info("Stopping local DB server");
        hsqlServer.stop();
        hsqlServer = null;
        LOG.info("Stopped");
    }

    public void execute(String query) throws SQLException {
        LOG.info("Running query: " + query);
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        statement.close();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        LOG.info("Running query: " + query);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = null;
        try {
            result = statement.executeQuery();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        statement.close();
        return result;
    }
}
