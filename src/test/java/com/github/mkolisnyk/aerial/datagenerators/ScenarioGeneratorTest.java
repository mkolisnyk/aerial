package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.document.InputRecord;

public class ScenarioGeneratorTest {

    private ScenarioGenerator generator;

    @Before
    public void setUp() throws Exception {
        List<InputRecord> initialData = new ArrayList<InputRecord>()
        {
            {
                add(new InputRecord("Name", "string", "(\\d)-(\\S{1,3})", "", true));
                add(new InputRecord("Date", "date", "MM/dd/yyyy", "", true));
                add(new InputRecord("Count", "int", "[0;100)", "", true));
            }
        };
        generator = new ScenarioGenerator(initialData);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUniqueNamesForValidInputShouldReturnOnlyUniqueNames() {
        String[] expectedNames = {"Name", "Date", "Count"};
        String[] actualNames = generator.getUniqueNames();
        Assert.assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetUniqueNamesForEmptyInputShouldReturnEmptyList() throws Exception {
        generator = new ScenarioGenerator(new ArrayList<InputRecord>());
        String[] expectedNames = {};
        String[] actualNames = generator.getUniqueNames();
        Assert.assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void testGetUniqueNamesForNullInputShouldReturnEmptyList() throws Exception {
        generator = new ScenarioGenerator(null);
        String[] expectedNames = {};
        String[] actualNames = generator.getUniqueNames();
        Assert.assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void testGenerateQueryStringShouldReturnConsistentString() {
        String expectedQuery = "SELECT S0.Value AS \"Name\","
                                    + " S1.Value AS \"Date\","
                                    + " S2.Value AS \"Count\","
                                    + "  CASE WHEN ("
                                            + "S0.ValidInput = 'true' AND "
                                            + "S1.ValidInput = 'true' AND "
                                            + "S2.ValidInput = 'true' AND  1 = 1 )"
                                    + " THEN 'true' ELSE 'false' END AS \"ValidInput\""
                                    + " FROM ("
                                        + "SELECT Name,Value,Condition,ValidInput FROM input"
                                            + " WHERE Name = 'Name') AS S0"
                                        + "  CROSS JOIN (SELECT Name,Value,Condition,ValidInput"
                                            + " FROM input WHERE Name = 'Date') AS S1"
                                        + "  CROSS JOIN (SELECT Name,Value,Condition,ValidInput"
                                            + " FROM input WHERE Name = 'Count') AS S2"
                                        + " ORDER BY \"ValidInput\" DESC";
        String query = generator.generateQueryString(generator.getUniqueNames());
        Assert.assertEquals(expectedQuery, query);
    }

    @Test
    public void testGenerateValidSetOfData() throws Exception {
        Map<String, List<String>> actualResult = generator.generateTestData();
        Assert.assertEquals(60, actualResult.get("Name").size());
        Assert.assertEquals(60, actualResult.get("Date").size());
        Assert.assertEquals(60, actualResult.get("Count").size());
        Assert.assertEquals(60, actualResult.get("ValidInput").size());
    }
}
