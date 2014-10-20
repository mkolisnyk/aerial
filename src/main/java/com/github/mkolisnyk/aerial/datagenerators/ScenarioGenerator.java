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
            String validInput = "false";
            if (record.isValidInput()) {
                validInput = "true";
            }
            db.execute(String.format("INSERT INTO input (Name, Type, Value, Condition, ValidInput)"
                    + " VALUES ('%s','%s','%s','%s','%s')",
                    record.getName(),
                    record.getType(),
                    record.getValue(),
                    record.getCondition(),
                    validInput));
        }

        /*
         * SELECT 
    S1.Value AS 'Number',
    S2.Value AS 'Text',
    S3.Value AS 'Date',
    CASE WHEN (S1.ValidInput = 'true' AND S2.ValidInput = 'true' AND S3.ValidInput = 'true') THEN 'true' ELSE 'false' END AS Valid
FROM
(
SELECT [Value]
      ,[Condition]
      ,[ValidInput]
  FROM [Test].[dbo].[InputTable] WHERE Name = 'Number') AS S1
  CROSS JOIN
(
SELECT [Value]
      ,[Condition]
      ,[ValidInput]
  FROM [Test].[dbo].[InputTable] WHERE Name = 'Text') AS S2
  CROSS JOIN
(
SELECT [Value]
      ,[Condition]
      ,[ValidInput]
  FROM [Test].[dbo].[InputTable] WHERE Name = 'Date') AS S3
  
ORDER BY Valid DESC

         */
        
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
