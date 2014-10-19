/**
 * 
 */
package com.github.mkolisnyk.aerial.datagenerators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;

/**
 * @author Myk Kolisnyk
 *
 */
public class TypedDataGeneratorTest {

    private class MyTypedDataGeneratorNullList extends TypedDataGenerator {

        public MyTypedDataGeneratorNullList(InputRecord inputValue) {
            super(inputValue);
            // TODO Auto-generated constructor stub
        }

        @Override
        public ValueExpression[] getApplicableExpressions()
                throws Exception {
            return null;
        }
    }

    private class MyTypedDataGeneratorEmptyList extends TypedDataGenerator {

        public MyTypedDataGeneratorEmptyList(InputRecord inputValue) {
            super(inputValue);
            // TODO Auto-generated constructor stub
        }

        @Override
        public ValueExpression[] getApplicableExpressions()
                throws Exception {
            return new ValueExpression[] {};
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.datagenerators.TypedDataGenerator#validate()}.
     * @throws Exception .
     */
    @Test
    public void testValidateForEmptyListOfExpressions() throws Exception {
        TypedDataGenerator generator = new MyTypedDataGeneratorNullList(new InputRecord());
        generator.validate();
        generator = new MyTypedDataGeneratorEmptyList(new InputRecord());
        generator.validate();
    }

}
