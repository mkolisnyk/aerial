/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.datagenerators.TypedDataGenerator;

/**
 * @author Myk Kolisnyk
 *
 */
public class InputSection extends DocumentSection<InputSection> {

    private List<InputRecord> inputs;
    private String lineSeparator = System.lineSeparator();

    /**
     * @param container
     */
    public InputSection(DocumentSection<CaseSection> container) {
        super(container);
        inputs = new ArrayList<InputRecord>();
    }

    /**
     * @return the inputs
     */
    public final List<InputRecord> getInputs() {
        return inputs;
    }


    public InputSection parse(String input) throws Exception {
        this.setContent(input);
        String[] lines = input.split(lineSeparator);
        Assert.assertTrue(
                "At least header and data row should be defined for input",
                lines.length > 1);
        for (int i = 1; i < lines.length; i++) {
            InputRecord record = new InputRecord();
            record.read(lines[i], lines[0]);
            inputs.add(record);
        }
        return this;
    }

    public void validate(String input) throws Exception {
        for (InputRecord record : inputs) {
            TypedDataGenerator generator = new TypedDataGenerator(record);
            generator.validate();
        }
    }
}
