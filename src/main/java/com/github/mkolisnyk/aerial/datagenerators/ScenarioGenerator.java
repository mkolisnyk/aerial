package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.util.HsqlDBWrapper;

public class ScenarioGenerator {

    private List<InputRecord> records;

    public ScenarioGenerator(List<InputRecord> recordsList) throws Exception {
        this.records = recordsList;
        this.validate();
    }

    public Map<String, List<String>> generate() throws Exception {
        List<InputRecord> expanded = new ArrayList<InputRecord>();
        for (InputRecord record : this.records) {
            TypedDataGenerator generator = new TypedDataGenerator(record);
            expanded.addAll(generator.generate());
        }
        HsqlDBWrapper db = new HsqlDBWrapper("test");
        db.startServer();
        db.openConnection();
        db.execute("DROP TABLE input IF EXISTS");
        db.execute("CREATE TABLE input ("
                + "Name varchar(50),"
                + " Type varchar(10),"
                + " Value varchar(50),"
                + " Condition varchar(50),"
                + " ValidInput varchar(5))");

        for (InputRecord record : expanded) {
            db.execute(String.format("INSERT INTO input (Name, Type, Value, Condition, ValidInput)"
                    + " VALUES ('%s','%s','%s','%s','%s')",
                    record.getName(),
                    record.getType(),
                    record.getValue(),
                    record.getCondition(),
                    record.isValidInput() ? "true" : "false"));
        }

        db.closeConnection();
        db.stopServer();
        return null;
    }

    public void validate() throws Exception {
        for (InputRecord record : this.records) {
            TypedDataGenerator generator = new TypedDataGenerator(record);
            generator.validate();
        }
    }
}
