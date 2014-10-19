/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Assert;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.ValueExpressionType;
import com.github.mkolisnyk.aerial.util.Clock;
import com.github.mkolisnyk.aerial.util.SystemClock;

/**
 * @author Myk Kolisnyk
 *
 */
public class DateRangeValueExpression extends ValueExpression {
    private static final String OPEN_RANGE_BRACKET_PATTERN = "(\\[|\\()";
    private static final String CLOSE_RANGE_BRACKET_PATTERN = "(\\]|\\))";
    private static final String DATE_PATTERN = "([0-9\\-/]+)";
    private static final String SPACE_DELIMITER_PATTERN = "(\\s*)";
    private boolean includeLower = false;
    private boolean includeUpper = false;
    private String formatString;
    private Date lower;
    private Date upper;

    private Clock clock;

    /**
     * @param inputValue
     * @throws Exception .
     */
    public DateRangeValueExpression(InputRecord inputValue) throws Exception {
        this(inputValue, new SystemClock());
    }

    public DateRangeValueExpression(InputRecord inputValue, Clock clockValue) throws Exception {
        super(inputValue);
        this.clock = clockValue;
        this.lower = clock.now();
        this.upper = clock.now();
        this.formatString = "dd-MM-yyyy";
    }

    /**
     * @param clockValue the clock to set
     */
    public final void setClock(Clock clockValue) {
        this.clock = clockValue;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        this.validate();

        InputRecord inputValue = (InputRecord) this.getInput().clone();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(this.formatString);
        DateTime start = new DateTime(lower);
        DateTime end = new DateTime(upper);

        // Valid in range value
        int range = Days.daysBetween(start, end).getDays();
        calendar.setTime(lower);
        calendar.add(Calendar.DAY_OF_YEAR, range / 2);
        inputValue.setValue(format.format(calendar.getTime()));
        inputValue.setValidInput(true);
        result.add(inputValue);
        // Lower border value
        inputValue = (InputRecord) this.getInput().clone();
        inputValue.setValue(format.format(lower));
        inputValue.setValidInput(this.includeLower);
        result.add(inputValue);
        // Upper border value
        inputValue = (InputRecord) this.getInput().clone();
        inputValue.setValue(format.format(upper));
        inputValue.setValidInput(this.includeUpper);
        result.add(inputValue);
        // Day before lower
        inputValue = (InputRecord) this.getInput().clone();
        calendar.setTime(lower);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        inputValue.setValue(format.format(calendar.getTime()));
        inputValue.setValidInput(false);
        result.add(inputValue);
        // Day after upper
        inputValue = (InputRecord) this.getInput().clone();
        calendar.setTime(upper);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        inputValue.setValue(format.format(calendar.getTime()));
        inputValue.setValidInput(false);
        result.add(inputValue);

        return result;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getValueType()
     */
    @Override
    public ValueExpressionType getValueType() {
        return ValueExpressionType.DATE;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getMatchPattern()
     */
    @Override
    public String getMatchPattern() {
        String dateMatchPattern = "([mMdDnYyhHsS\\-/:]+)";
        return String.format("%s%s%s%s;%s%s%s%s, Format: %s%s",
                OPEN_RANGE_BRACKET_PATTERN,
                SPACE_DELIMITER_PATTERN,
                DATE_PATTERN,
                SPACE_DELIMITER_PATTERN,
                SPACE_DELIMITER_PATTERN,
                DATE_PATTERN,
                SPACE_DELIMITER_PATTERN,
                CLOSE_RANGE_BRACKET_PATTERN,
                SPACE_DELIMITER_PATTERN,
                dateMatchPattern);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#validate()
     */
    @Override
    public void validate() throws Exception {
        super.validate();
        this.parse();
        Assert.assertTrue("The upper date should be after the initial date",
                this.upper.after(this.lower));
    }

    public void parse() throws Exception {
        String input = this.getInput().getValue();
        String pattern = this.getMatchPattern();
        this.includeLower = input.replaceFirst(pattern, "$1").equals("[");
        this.includeUpper = input.replaceFirst(pattern, "$8").equals("]");
        this.formatString = input.replaceFirst(pattern, "$10");
        SimpleDateFormat format = new SimpleDateFormat(this.formatString);
        String lowerString = input.replaceFirst(pattern, "$3");
        String upperString = input.replaceFirst(pattern, "$6");
        this.lower = format.parse(lowerString);
        this.upper = format.parse(upperString);
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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return String.format("%s%s;%s%s,Format:%s",
                start,
                format.format(lower),
                format.format(upper),
                end,
                format.toPattern());
    }
}
