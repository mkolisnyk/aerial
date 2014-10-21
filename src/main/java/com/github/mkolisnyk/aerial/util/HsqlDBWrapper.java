/**
 * .
 */
package com.github.mkolisnyk.aerial.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<String> getColumnNames(ResultSet result) throws SQLException {
        List<String> names = new ArrayList<String>();
        int count = result.getMetaData().getColumnCount();
        for (int i = 1; i <= count; i++) {
            names.add(result.getMetaData().getColumnLabel(i));
        }
        return names;
    }

    public Map<String, List<String>> executeQuery(String query) throws SQLException {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        LOG.info("Running query: " + query);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = null;
        try {
            result = statement.executeQuery();
            List<String> names = this.getColumnNames(result);
            result.next();
            while (!result.isAfterLast()) {
                for (String name: names) {
                    List<String> column = new ArrayList<String>();
                    if (output.containsKey(name)) {
                        column = output.get(name);
                    }
                    column.add(result.getString(name));
                    output.put(name, column);
                }
                result.next();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        statement.close();
        return output;
    }
}
