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
    public InputSection(ContainerSection container) {
        super(container);
        inputs = new ArrayList<InputRecord>();
    }

    public InputSection(ContainerSection container, String tagValue) {
        super(container, tagValue);
    }

    /**
     * @return the inputs
     */
    public final List<InputRecord> getInputs() {
        return inputs;
    }

    public InputSection parse(String input) throws Exception {
        inputs = new ArrayList<InputRecord>();
        this.setContent(input);
        String[] lines = input.split(lineSeparator);
        if (this.getName().trim().equals("")) {
            Assert.assertTrue(
                    "At least header and data row should be defined for input",
                    lines.length > 1);
        } else {
            if (lines.length < 2) {
                InputSection section = this.getParent()
                        .findNamedSectionInParent(
                        getName(),
                        Tokens.getInputRefToken(),
                        InputSection.class);
                Assert.assertNotNull("Section with the '" + getName() + "' was not found", section);
                section.parse(section.getContent());
                this.setContent(section.getContent());
                this.inputs = section.getInputs();
                return this;
            }
        }
        for (int i = 1; i < lines.length; i++) {
            InputRecord record = new InputRecord();
            record.read(lines[i], lines[0]);
            inputs.add(record);
        }
        return this;
    }

    public void validate(String input) throws Exception {
        if (inputs == null) {
            this.parse(input);
        }
        for (InputRecord record : inputs) {
            Assert.assertFalse("The name must be non-empty", record.getName().trim().equals(""));
            TypedDataGenerator generator = new TypedDataGenerator(record);
            generator.validate();
        }
    }

    public String generate() throws Exception {
        return null;
    }
}
