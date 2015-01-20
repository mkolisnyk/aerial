/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.TestDataGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.NegativeCaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.PositiveCaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.UniqueValueCaseScenarioGenerator;

/**
 * @author Myk Kolisnyk
 */
public class CaseSection extends ContainerSection {

    public CaseSection(DocumentSection<?> container) {
        super(container);
    }

    public CaseSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String[] getSectionTokens() {
        return new String[] {Tokens.ACTION_TOKEN,
                Tokens.PREREQUISITES_TOKEN, Tokens.INPUT_TOKEN,
                Tokens.VALID_OUTPUT_TOKEN, Tokens.ERROR_OUTPUT_TOKEN };
    }

    public String[] getMandatoryTokens() {
        return new String[] {Tokens.ACTION_TOKEN,
                Tokens.INPUT_TOKEN, Tokens.VALID_OUTPUT_TOKEN,
                Tokens.ERROR_OUTPUT_TOKEN };
    }

    public Map<String, Class<?>> getCreationMap() {
        return new HashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;
            {
                put(Tokens.ACTION_TOKEN, ActionSection.class);
                put(Tokens.PREREQUISITES_TOKEN,
                        PrerequisitesSection.class);
                put(Tokens.INPUT_TOKEN, InputSection.class);
                put(Tokens.VALID_OUTPUT_TOKEN, ValidOutput.class);
                put(Tokens.ERROR_OUTPUT_TOKEN, ErrorOutput.class);
            }
        };
    }

    public List<Class<? extends CaseScenarioGenerator>> getScenarioGenerators() {
        List<Class<? extends CaseScenarioGenerator>> result = new ArrayList<Class<? extends CaseScenarioGenerator>>() {
            /**
             * .
             */
            private static final long serialVersionUID = 1L;

            {
                add(PositiveCaseScenarioGenerator.class);
                add(NegativeCaseScenarioGenerator.class);
                add(UniqueValueCaseScenarioGenerator.class);
            }
        };
        return result;
    }

    public String generate() throws Exception {
        InputSection input = (InputSection) this.getSections().get(Tokens.INPUT_TOKEN).get(0);
        TestDataGenerator dataGenerator = new TestDataGenerator(input.getInputs());
        Map<String, List<String>> testData = dataGenerator.generateTestData();
        String content = "";
        for (Class<? extends CaseScenarioGenerator> generator : this.getScenarioGenerators()) {
            Constructor<? extends CaseScenarioGenerator> constructor
                = generator.getConstructor(CaseSection.class, List.class, Map.class);
            CaseScenarioGenerator instance
                = (CaseScenarioGenerator) constructor.newInstance(this, input.getInputs(), testData);
            if (instance.isApplicable()) {
                content = content.concat(instance.generate());
            }
        }
        content = content.replaceAll("\t", "    ");
        return content;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.document.ContainerSection#validate()
     */
    @Override
    public void validate() throws Exception {
        super.validate();
        Assert.assertTrue(this.getName().trim().length() > 0);
    }
}
