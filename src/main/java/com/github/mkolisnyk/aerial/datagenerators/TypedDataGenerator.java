/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

import com.github.mkolisnyk.aerial.AerialDataGenerator;
import com.github.mkolisnyk.aerial.document.InputRecord;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class TypedDataGenerator implements
        AerialDataGenerator {

    private InputRecord input;

    /**
     * .
     */
    public TypedDataGenerator(InputRecord inputValue) {
        this.input = inputValue;
    }

    /**
     * @return the input
     */
    public final InputRecord getInput() {
        return input;
    }
}
