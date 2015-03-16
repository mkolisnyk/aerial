package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.util.HsqlDBWrapper;

public class TestDataGenerator {

    private List<InputRecord> records;

    public TestDataGenerator(List<InputRecord> recordsList) throws Exception {
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

    private String[] getUniqueConditions() {
        List<String> conditions = new ArrayList<String>();
        for (InputRecord record : this.records) {
            if (!conditions.contains(record.getCondition()) && !StringUtils.isBlank(record.getCondition())) {
                conditions.add(record.getCondition());
            }
        }
        String[] result = new String[conditions.size()];
        result = conditions.toArray(result);
        return result;
    }
    private String singleQueryString(String[] names) {
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
    String generateQueryString(String[] names, String[] conditions) {
        String queryResult = "";
        if (conditions.length < 1) {
            return singleQueryString(names);
        }
        String[] queries = new String[conditions.length];
        for (int i = 0; i < conditions.length; i++) {
            queries[i] = singleQueryString(names) + " WHERE " + conditions[i];
        }
        queryResult = StringUtils.join(queries, " UNION ");
        return queryResult;
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
        String[] conditions = this.getUniqueConditions();
        String query = this.generateQueryString(names, conditions);

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
