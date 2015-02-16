package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;

public class CustomExternalTypeExpression extends ValueExpression {

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

    public CustomExternalTypeExpression(InputRecord inputValue) {
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
