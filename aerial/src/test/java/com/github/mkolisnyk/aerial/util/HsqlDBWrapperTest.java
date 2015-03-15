package com.github.mkolisnyk.aerial.util;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HsqlDBWrapperTest {

    private HsqlDBWrapper db;

    @Before
    public void setUp() throws Exception {
        db = new HsqlDBWrapper("test");
        db.startServer();
        db.openConnection();
    }

    @After
    public void tearDown() throws Exception {
        db.closeConnection();
        db.stopServer();
    }

    @Test
    public void testExecute() throws SQLException {
        db.execute("DROP TABLE sample IF EXISTS");
        db.execute("CREATE TABLE sample (Field1 varchar(50))");
        db.execute("INSERT INTO sample (Field1) VALUES ('Sample Value')");
    }

}
