package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.util.HsqlDBWrapper;

public class ScenarioGenerator {

    private List<InputRecord> records;

    public ScenarioGenerator(List<InputRecord> recordsList) throws Exception {
        if (recordsList == null) {
            recordsList = new ArrayList<InputRecord>();
        }
        this.records = recordsList;
    }

    String[] getUniqueNames() {
        List<String> names = new ArrayList<String>();
        for (InputRecord record : this.records) {
            if (!names.contains(record.getName())) {
                names.add(record.getName());
            }
        }
        String[] result = new String[names.size()];
        result = names.toArray(result);
        return result;
    }

    String generateQueryString(String[] names) {
        String query = "SELECT ";
        for (int i = 0; i < names.length; i++) {
            query = query.concat(String.format("S%d.Value AS \"%s\", ", i, names[i]));
        }
        query = query.concat(" CASE WHEN (");
        for (int i = 0; i < names.length; i++) {
            query = query.concat(String.format("S%d.ValidInput = 'true' AND ", i));
        }
        query = query.concat(" 1 = 1 ) THEN 'true' ELSE 'false' END AS \"ValidInput\" FROM ");
        for (int i = 0; i < names.length - 1; i++) {
            query = query.concat(
                    String.format(
                            "(SELECT Name,Value,Condition,ValidInput "
                            + "FROM input WHERE Name = '%s') AS S%d  CROSS JOIN ",
                            names[i], i));
        }
        query = query.concat(
                String.format(
                        "(SELECT Name,Value,Condition,ValidInput "
                        + "FROM input WHERE Name = '%s') AS S%d ORDER BY \"ValidInput\" DESC",
                        names[names.length - 1], names.length - 1));
        return query;
    }

    public Map<String, List<String>> generateTestData() throws Exception {
        List<InputRecord> expanded = new ArrayList<InputRecord>();
        this.validate();
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
                    "" + record.isValidInput()));
        }

        String[] names = this.getUniqueNames();
        String query = this.generateQueryString(names);

        Map<String, List<String>> output = db.executeQuery(query);
        for (String name: names) {
            Assert.assertTrue(
                    String.format("The '%s' column wasn't found in the result set", name),
                    output.containsKey(name));
        }

        db.closeConnection();
        db.stopServer();
        return output;
    }

    public void validate() throws Exception {
        for (InputRecord record : this.records) {
            TypedDataGenerator generator = new TypedDataGenerator(record);
            generator.validate();
        }
    }
}
