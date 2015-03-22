package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.document.InputSection;
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

    private List<Map<String, String>> getConditionCombinations() throws Exception {
        Map<String, List<String>> nameConditionMap = InputSection.getNameConditionMap(this.records);
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        int total = 0;
        for (Entry<String, List<String>> entry : nameConditionMap.entrySet()) {
            if (total == 0) {
                total = 1;
            }
            total *= entry.getValue().size();
        }
        for (int i = 0; i < total; i++) {
            Map<String, String> row = new HashMap<String, String>();
            int multiplier = 1;
            for (Entry<String, List<String>> entry : nameConditionMap.entrySet()) {
                int index = i / multiplier % entry.getValue().size();
                row.put(entry.getKey(), entry.getValue().get(index));
                multiplier *= entry.getValue().size();
            }
            results.add(row);
        }
        return results;
    }

    private String singleQueryString(String[] names, Map<String, String> conditionRow) {
        String query = "SELECT ";
        for (int i = 0; i < names.length; i++) {
            query = query.concat(String.format("S%d.Value AS \"%s\", ", i, names[i]));
        }
        query = query.concat(" CASE WHEN (");
        for (int i = 0; i < names.length; i++) {
            query = query.concat(String.format("S%d.ValidInput = 'true' AND ", i));
        }
        query = query.concat(" 1 = 1 ) THEN 'true' ELSE 'false' END AS \"ValidInput\" FROM ");
        String[] queryParts = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            if (conditionRow.containsKey(names[i])) {
                queryParts[i] = String.format(
                    "(SELECT Name,Value,Condition,ValidInput "
                    + "FROM input WHERE Name = '%s' AND Condition='%s') AS S%d",
                    names[i], conditionRow.get(names[i]), i);
            } else {
                queryParts[i] = String.format(
                    "(SELECT Name,Value,Condition,ValidInput "
                    + "FROM input WHERE Name = '%s') AS S%d",
                    names[i], i);
            }
        }
        query = query.concat(StringUtils.join(queryParts, " CROSS JOIN "));
        return query;
    }

    private List<String> quoteNames(Collection<String> conditions, String[] names) {
        List<String> result = new ArrayList<String>();
        for (String condition : conditions) {
            for (String name : names) {
                condition = condition.replaceAll(name, "\"" + name + "\"");
            }
            result.add(condition);
        }
        return result;
    }

    String generateQueryString(String[] names, List<Map<String, String>> conditions) {
        String queryResult = "";
        if (conditions.size() < 1) {
            return singleQueryString(names, new HashMap<String, String>()) + " ORDER BY ValidInput DESC";
        }
        String[] queries = new String[conditions.size()];
        for (int i = 0; i < conditions.size(); i++) {
            queries[i] = "(SELECT * FROM (" + singleQueryString(names, conditions.get(i)) + ") AS T" + i + " WHERE "
                    + StringUtils.join(quoteNames(conditions.get(i).values(), names).iterator(), " AND ") + ")";
        }
        queryResult = StringUtils.join(queries, " UNION ") + " ORDER BY \"ValidInput\" DESC";
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
        List<Map<String, String>> conditions = this.getConditionCombinations();
        String query = this.generateQueryString(names, conditions);

        Map<String, List<String>> output = db.executeQuery(query);
        System.out.println(StringUtils.join(output.keySet().iterator(), ", "));
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
