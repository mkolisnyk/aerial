/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.ValueExpressionType;

/**
 * @author Myk Kolisnyk
 *
 */
public class NumericRangeValueExpression extends ValueExpression {

    private static final String NUMBER_PATTERN = "(([-0-9](\\d*)))";
    private static final String OPEN_RANGE_BRACKET_PATTERN = "(\\[|\\()";
    private static final String CLOSE_RANGE_BRACKET_PATTERN = "(\\]|\\))";

    private boolean includeLower = false;
    private boolean includeUpper = false;
    private int lower = 0;
    private int upper = 0;

    /**
     * @param inputValue
     */
    public NumericRangeValueExpression(InputRecord inputValue) {
        super(inputValue);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        InputRecord testRecord = (InputRecord) this.getInput().clone();
        this.validate();
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        "" + (lower + upper) / 2,
                        testRecord.getCondition(),
                        true));
        if (Math.abs(upper - lower) > 1) {
            result.add(
                    new InputRecord(
                            testRecord.getName(),
                            testRecord.getType(),
                            "" + ((lower + upper) / 2 + 1),
                            testRecord.getCondition(),
                            true));
        }
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        "" + lower,
                        testRecord.getCondition(),
                        includeLower));
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        "" + upper,
                        testRecord.getCondition(),
                        includeUpper));
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        "" + (lower - 1),
                        testRecord.getCondition(),
                        false));
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        "" + (upper + 1),
                        testRecord.getCondition(),
                        false));
        return result;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getValueType()
     */
    @Override
    public ValueExpressionType getValueType() {
        return ValueExpressionType.INTEGER;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#validate()
     */
    @Override
    public void validate() throws Exception {
        super.validate();
        this.parse();
        Assert.assertTrue(
                String.format(
                        "Lower bound %d should be less than upper bound %d",
                        lower,
                        upper),
                lower <= upper);
        Assert.assertFalse(
                "The range is empty",
                !this.includeLower
                    && !this.includeUpper
                    && (this.upper - this.lower) <= 1);
        Assert.assertFalse(
                "The range is empty",
                !(this.includeLower && this.includeUpper)
                    && (this.upper - this.lower) < 1);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getMatchPattern()
     */
    @Override
    public String getMatchPattern() {
        return String.format("%s%s;%s%s",
                OPEN_RANGE_BRACKET_PATTERN,
                NUMBER_PATTERN,
                NUMBER_PATTERN,
                CLOSE_RANGE_BRACKET_PATTERN);
    }

    public void parse() {
        String input = this.getInput().getValue();
        String pattern = this.getMatchPattern();
        this.includeLower = input.replaceFirst(pattern, "$1").equals("[");
        this.includeUpper = input.replaceFirst(pattern, "$8").equals("]");
        this.lower = new Integer(input.replaceFirst(pattern, "$2"));
        this.upper = new Integer(input.replaceFirst(pattern, "$5"));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String start = "(";
        String end = ")";
        if (includeLower) {
            start = "[";
        }
        if (includeUpper) {
            end = "]";
        }
        return String.format("%s%d;%d%s",
                start,
                lower,
                upper,
                end);
    }
}
