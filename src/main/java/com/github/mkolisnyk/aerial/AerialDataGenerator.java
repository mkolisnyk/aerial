/**
 * .
 */
package com.github.mkolisnyk.aerial;

import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;

/**
 * @author Myk Kolisnyk
 *
 */
public interface AerialDataGenerator {

    List<InputRecord> generate();
    void validate();
}
