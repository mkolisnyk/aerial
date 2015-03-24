package com.github.mkolisnyk.aerial.document;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InputSectionValidateConditionTest {

    private String fieldName;
    private List<String> conditions;
    private Set<String> availableFields;
    private boolean shouldFail;

    private InputSection section;
    
    public InputSectionValidateConditionTest(String description,
            String fieldName, List<String> conditions,
            Set<String> availableFields, boolean shouldFail) {
        this.fieldName = fieldName;
        this.conditions = conditions;
        this.availableFields = availableFields;
        this.shouldFail = shouldFail;
    }

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        Set<String> sampleSet = new HashSet<String>(){
            {
                add("test");
                add("test1");
                add("test2");
            }
        };
        return Arrays.asList(new Object[][] {
                {"Empty condition list", "test", new ArrayList<String>(), sampleSet, false
                },
                {
                    "Dependency on other fields is acceptable",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("test1 > test2");
                            add("test1 <= test2");
                        }
                    },
                    sampleSet,
                    false
                },
                {
                    "Field should not depend on itself",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("test = 'Hello'");
                            add("test = 'Bye'");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Empty condition",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Unknown dependency condition should fail",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("sample");
                            add("sample2");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Inconsistent quotes should fail",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("test1 < '3");
                            add("test2 > 4");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Inconsistent double-quotes should fail",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("test1 < \"3");
                            add("test2 > 4");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Consistent double-quotes should pass",
                    "test1",
                    new ArrayList<String>() {
                        {
                            add("test2 < \"3\"");
                            add("test > 4");
                        }
                    },
                    sampleSet,
                    false
                },
                {
                    "Inconsistent brackets should fail",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("test1 < (3");
                            add("test2 > (4)");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Inconsistent square brackets should fail",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("[test1] < 3");
                            add("test2] > (4)");
                        }
                    },
                    sampleSet,
                    true
                },
                {
                    "Single condition should fail",
                    "test",
                    new ArrayList<String>() {
                        {
                            add("[test1] < 3");
                        }
                    },
                    sampleSet,
                    true
                }
        });
    }

    @Before
    public void setUp() throws Exception {
        section = new InputSection(null);
    }

    @Test
    public void testValidateConditions() {
        try {
            section.validateConditions(fieldName, conditions, availableFields);
        } catch (AssertionError e) {
            Assert.assertTrue("Unexpected error: " + e.getMessage(), shouldFail);
        }
    }

}
