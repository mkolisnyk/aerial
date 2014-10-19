/**
 * 
 */
package com.github.mkolisnyk.aerial.datagenerators;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.util.Clock;

/**
 * @author Myk Kolisnyk
 *
 */
@RunWith(Parameterized.class)
public class DateDataGeneratorTest {
    private class TestClock implements Clock {

        public Date now() throws Exception {
            DateFormat baseFormat = new SimpleDateFormat("dd-MM-yyyy");
            return  baseFormat.parse("18-10-2014");
        }
    }

    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private DateDataGenerator generator;

    public DateDataGeneratorTest(String description,
            InputRecord recordValue, List<InputRecord> expectedRecordsValue) {
        this.record = recordValue;
        this.expectedRecords = expectedRecordsValue;
    }

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Simple date format: ",
                    new InputRecord("Name", "date", "dd/MM/yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "18/10/2014", "", true));
                            add(new InputRecord("Name", "date", "12/04/1961", "", true));
                            add(new InputRecord("Name", "date", "", "", false));
                        }
                    }
                },
                {"All inclusive range",
                    new InputRecord("Name", "date", "[01-01-2000;02-10-2010], Format: dd-MM-yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "17-05-2005", "", true));
                            add(new InputRecord("Name", "date", "01-01-2000", "", true));
                            add(new InputRecord("Name", "date", "02-10-2010", "", true));
                            add(new InputRecord("Name", "date", "31-12-1999", "", false));
                            add(new InputRecord("Name", "date", "03-10-2010", "", false));
                        }
                    }
                },
                {"Upper exclusive range",
                    new InputRecord("Name", "date", "[01-01-2000;02-10-2010), Format: dd-MM-yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "17-05-2005", "", true));
                            add(new InputRecord("Name", "date", "01-01-2000", "", true));
                            add(new InputRecord("Name", "date", "02-10-2010", "", false));
                            add(new InputRecord("Name", "date", "31-12-1999", "", false));
                            add(new InputRecord("Name", "date", "03-10-2010", "", false));
                        }
                    }
                },
                {"All exclusive range",
                    new InputRecord("Name", "date", "(01-01-2000;02-10-2010), Format: dd-MM-yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "17-05-2005", "", true));
                            add(new InputRecord("Name", "date", "01-01-2000", "", false));
                            add(new InputRecord("Name", "date", "02-10-2010", "", false));
                            add(new InputRecord("Name", "date", "31-12-1999", "", false));
                            add(new InputRecord("Name", "date", "03-10-2010", "", false));
                        }
                    }
                },
                {"Lower exclusive range",
                    new InputRecord("Name", "date", "(01-01-2000;02-10-2010], Format: dd-MM-yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "17-05-2005", "", true));
                            add(new InputRecord("Name", "date", "01-01-2000", "", false));
                            add(new InputRecord("Name", "date", "02-10-2010", "", true));
                            add(new InputRecord("Name", "date", "31-12-1999", "", false));
                            add(new InputRecord("Name", "date", "03-10-2010", "", false));
                        }
                    }
                },
                {"Spaces should be handled",
                    new InputRecord("Name", "date", "( 01-01-2000 ; 02-10-2010 ], Format:   dd-MM-yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "17-05-2005", "", true));
                            add(new InputRecord("Name", "date", "01-01-2000", "", false));
                            add(new InputRecord("Name", "date", "02-10-2010", "", true));
                            add(new InputRecord("Name", "date", "31-12-1999", "", false));
                            add(new InputRecord("Name", "date", "03-10-2010", "", false));
                        }
                    }
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        generator = new DateDataGenerator(record);
        generator.setClock(new TestClock());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerate() throws Exception {
        List<InputRecord> actualList = generator.generate();

        for (InputRecord actual : actualList) {
            Assert.assertTrue("Unexpected record found: " + actual,
                    this.expectedRecords.contains(actual));
        }
        for (InputRecord expected : this.expectedRecords) {
            Assert.assertTrue("Expected record wasn't found: " + expected,
                    actualList.contains(expected));
        }
    }

}
