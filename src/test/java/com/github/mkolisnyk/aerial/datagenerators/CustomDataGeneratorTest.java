package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;

public class CustomDataGeneratorTest {

    public class CustomInnerTypeExpression extends ValueExpression {

        /* (non-Javadoc)
         * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getMatchPattern()
         */
        @Override
        public String getMatchPattern() {
            return "(.*)";
        }

        /* (non-Javadoc)
         * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getValueTypeName()
         */
        @Override
        public String getValueTypeName() {
            return "Season";
        }

        public CustomInnerTypeExpression(InputRecord inputValue) {
            super(inputValue);
        }

        @Override
        public List<InputRecord> generate() throws Exception {
            List<InputRecord> result = new ArrayList<InputRecord>() {
                {
                    new InputRecord(
                            getInput().getName(),
                            getInput().getType(),
                            "Winter",
                            getInput().getCondition(),
                            true);
                    new InputRecord(
                            getInput().getName(),
                            getInput().getType(),
                            "Spring",
                            getInput().getCondition(),
                            true);
                    new InputRecord(
                            getInput().getName(),
                            getInput().getType(),
                            "Summer",
                            getInput().getCondition(),
                            true);
                    new InputRecord(
                            getInput().getName(),
                            getInput().getType(),
                            "Autumn",
                            getInput().getCondition(),
                            true);
                    new InputRecord(
                            getInput().getName(),
                            getInput().getType(),
                            "Unknown",
                            getInput().getCondition(),
                            false);
                }
            };
            return result;
        }
    }

    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private TypedDataGenerator generator;

    @Before
    public void setUp() throws Exception {
        this.record = new InputRecord("Name", "Season", "", "");
        CustomInnerTypeExpression expression = new CustomInnerTypeExpression(this.record);
        this.expectedRecords = expression.generate();
        generator = new TypedDataGenerator(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerateCustomClassWithPropertySet() throws Exception {
        System.setProperty("aerial.types.custom.classes",
                "com.github.mkolisnyk.aerial.datagenerators.CustomExternalTypeExpression");
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

    @Test
    public void testGenerateCustomClassForInnerTypeWithPropertySet() throws Exception {
        System.setProperty("aerial.types.custom.classes",
                "com.github.mkolisnyk.aerial.datagenerators.CustomDataGeneratorTest$CustomInnerTypeExpression");
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

    @Test(expected = AssertionError.class)
    public void testGenerateCustomClassForInnerTypeNoPropertySetShouldFail() throws Exception {
        System.setProperty("aerial.types.custom.classes", "");
        generator.generate();
    }
}
