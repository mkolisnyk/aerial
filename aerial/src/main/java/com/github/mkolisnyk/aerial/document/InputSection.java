/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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

    public void validateConditions(String fieldName, List<String> conditions, Set<String> availableFields) {
        for (String condition : conditions) {
            // Rule 1: field should not depend on itself. So, field doesn't contain itself in the condition
            Assert.assertFalse("Apparently field " + fieldName + " has dependency on itself",
                    condition.matches("(^|([^A-Za-z0-9]+))" + fieldName  + "($|([^A-Za-z]+))"));
            // Rule 2: if condition exists it shouldn't be empty
            Assert.assertFalse("The condition for the '" + fieldName + "' field shouldn't be empty",
                    condition.trim().equals(""));
            // Rule 3: condition should depend on at least 1 field
            int dependenciesCount = 0;
            for (String availableField : availableFields) {
                if (condition.matches("(.*)" + availableField + "(.*)")) {
                    dependenciesCount++;
                    break;
                }
            }
            Assert.assertTrue("The condition for the '" + fieldName
                    + "' should depend at least on one available fields: ["
                    + StringUtils.join(availableFields.iterator(), ",") + "]",
                    dependenciesCount > 0);
            // Rule 4: condition should have matching number of brackets and quotes
            Assert.assertTrue("Quote doesn't have closing pair", StringUtils.countMatches(condition, "'") % 2 == 0);
            Assert.assertTrue("Double quote doesn't have closing pair",
                                StringUtils.countMatches(condition, "\"") % 2 == 0);
            Assert.assertEquals(StringUtils.countMatches(condition, "("), StringUtils.countMatches(condition, ")"));
            Assert.assertEquals(StringUtils.countMatches(condition, "["), StringUtils.countMatches(condition, "]"));
        }
        // Rule 5: the number of conditions for each field must not be equal to 1
        // (each condition should have at least one counter-condition)
        Assert.assertNotEquals(1, conditions.size());
    }

    public void validate(String input) throws Exception {
        if (inputs.size() <= 0) {
            this.parse(input);
        }
        Set<String> allFields = new HashSet<String>();
        for (InputRecord inputRecord : inputs) {
            allFields.add(inputRecord.getName().trim());
        }
        Map<String, List<String>> nameConditionMap = getNameConditionMap(inputs);
        for (Entry<String, List<String>> entry : nameConditionMap.entrySet()) {
            validateConditions(entry.getKey(), entry.getValue(), allFields);
        }
    }

    public String generate() throws Exception {
        return null;
    }

    public static Map<String, List<String>> getNameConditionMap(List<InputRecord> inputs) throws Exception {
        Map<String, List<String>> nameConditionMap = new HashMap<String, List<String>>();
        for (InputRecord record : inputs) {
            Assert.assertFalse("The name must be non-empty", record.getName().trim().equals(""));
            TypedDataGenerator generator = new TypedDataGenerator(record);
            generator.validate();
            if (!nameConditionMap.containsKey(record.getName())) {
                nameConditionMap.put(record.getName(), new ArrayList<String>());
            }
            if (!nameConditionMap.get(record.getName()).contains(record.getCondition().trim())
                    && StringUtils.isNotBlank(record.getCondition().trim())) {
                nameConditionMap.get(record.getName()).add(record.getCondition().trim());
            }
        }
        for (InputRecord record : inputs) {
            if (nameConditionMap.get(record.getName()).size() == 0) {
                nameConditionMap.remove(record.getName());
            }
        }
        return nameConditionMap;
    }
}
