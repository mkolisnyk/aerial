/**
 * 
 */
package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;

/**
 * @author Myk Kolisnyk
 *
 */
public class NumericDataGenerator extends TypedDataGenerator {

    /**
     * @param inputValue
     */
    public NumericDataGenerator(InputRecord inputValue) {
        super(inputValue);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialDataGenerator#generate()
     */
    public List<InputRecord> generate() {
        List<InputRecord> result = new ArrayList<InputRecord>();
        return result;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialDataGenerator#validate()
     */
    public void validate() {
        // TODO Auto-generated method stub

    }

}
