/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.util.Clock;
import com.github.mkolisnyk.aerial.util.SystemClock;

/**
 * @author Myk Kolisnyk
 *
 */
public class SingleDateValueExpression extends ValueExpression {

    private Clock clock;

    /**
     * @param inputValue
     */
    public SingleDateValueExpression(InputRecord inputValue) {
        super(inputValue);
        this.clock = new SystemClock();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#validate()
     */
    @Override
    public void validate() throws Exception {
        super.validate();
        new SimpleDateFormat(this.getInput().getValue());
    }

    /**
     * @param clockValue the clock to set
     */
    public final void setClock(Clock clockValue) {
        this.clock = clockValue;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getMatchPattern()
     */
    @Override
    public String getMatchPattern() {
        return "([mMdDnYyhHsS\\-/:]+)";
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        this.validate();
        DateFormat format = new SimpleDateFormat(this.getInput().getValue());
        DateFormat baseFormat = new SimpleDateFormat("dd-mm-yyyy");

        InputRecord testRecord = (InputRecord) this.getInput().clone();
        // Add current date
        testRecord.setValue(format.format(this.clock.now()));
        testRecord.setValidInput(true);
        result.add(testRecord);
        // Fake leap day
        testRecord = (InputRecord) this.getInput().clone();
        testRecord.setValue(format.format(baseFormat.parse("29-02-2014")));
        testRecord.setValidInput(false);
        result.add(testRecord);

        // Before Unix era
        testRecord = (InputRecord) this.getInput().clone();
        testRecord.setValue(format.format(baseFormat.parse("12-04-1961")));
        testRecord.setValidInput(true);
        result.add(testRecord);

        // Empty value
        testRecord = (InputRecord) this.getInput().clone();
        testRecord.setValue("");
        testRecord.setValidInput(false);
        result.add(testRecord);

        return result;
    }

}
