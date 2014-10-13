/**
 * 
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;

/**
 * @author Myk Kolisnyk
 *
 */
public class SingleDateValueExpression extends ValueExpression {

    /**
     * @param inputValue
     */
    public SingleDateValueExpression(InputRecord inputValue) {
        super(inputValue);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#validate()
     */
    @Override
    public void validate() {
        super.validate();
        new SimpleDateFormat(this.getInput().getValue());
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getMatchPattern()
     */
    @Override
    public String getMatchPattern() {
        return "([mMdDnYyhHsS\\\\\\\\-/:]+)";
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        this.validate();
        DateFormat format = new SimpleDateFormat(this.getInput().getValue());
        DateFormat baseFormat = new SimpleDateFormat("mm-dd-yyyy");

        InputRecord testRecord = (InputRecord) this.getInput().clone();
        // Add current date
        testRecord.setValue(format.format(DateTime.now()));
        testRecord.setValidInput(true);
        result.add(testRecord);
        // Fake leap day
        /*testRecord.setValue(format.format(baseFormat.parse("29-02-2014")));
        testRecord.setValidInput(false);
        result.add(testRecord);*/

        // Before Unix era
        testRecord.setValue(format.format(baseFormat.parse("12-04-1961")));
        testRecord.setValidInput(true);
        result.add(testRecord);

        // Empty value
        testRecord.setValue("");
        testRecord.setValidInput(false);
        result.add(testRecord);

        return result;
    }

}
