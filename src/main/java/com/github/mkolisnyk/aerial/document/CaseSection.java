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

import com.github.mkolisnyk.aerial.core.AerialGlobalProperties;
import com.github.mkolisnyk.aerial.datagenerators.CaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.TestDataGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.MandatoryCaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.NegativeCaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.PositiveCaseScenarioGenerator;
import com.github.mkolisnyk.aerial.datagenerators.cases.UniqueValueCaseScenarioGenerator;

/**
 * @author Myk Kolisnyk
 */
public class CaseSection extends ContainerSection implements AerialGlobalProperties {

    public CaseSection(DocumentSection<?> container) {
        this(container, null);
    }

    public CaseSection(DocumentSection<?> container, String tag) {
        super(container, tag);
    }

    public CaseSection parse(String input) throws Exception {
        super.parse(input);
        return this;
    }

    public String[] getSectionTokens() {
        return new String[] {Tokens.getActionToken(),
                Tokens.getPrerequisitesToken(), Tokens.getInputToken(),
                Tokens.getValidOutputToken(), Tokens.getErrorOutputToken() };
    }

    public String[] getMandatoryTokens() {
        return new String[] {Tokens.getActionToken(),
                Tokens.getInputToken(), Tokens.getValidOutputToken(), Tokens.getErrorOutputToken() };
    }

    public Map<String, Class<?>> getCreationMap() {
        return new HashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;
            {
                put(Tokens.getActionToken(), ActionSection.class);
                put(Tokens.getPrerequisitesToken(),
                        PrerequisitesSection.class);
                put(Tokens.getInputToken(), InputSection.class);
                put(Tokens.getValidOutputToken(), ValidOutput.class);
                put(Tokens.getErrorOutputToken(), ErrorOutput.class);
            }
        };
    }

    public List<Class<? extends CaseScenarioGenerator>> getScenarioGenerators() throws Exception {
        List<Class<? extends CaseScenarioGenerator>> result = new ArrayList<Class<? extends CaseScenarioGenerator>>() {
            /**
             * .
             */
            private static final long serialVersionUID = 1L;

            {
                add(PositiveCaseScenarioGenerator.class);
                add(NegativeCaseScenarioGenerator.class);
                add(UniqueValueCaseScenarioGenerator.class);
                add(MandatoryCaseScenarioGenerator.class);
                String customClassesString = System.getProperty(AERIAL_GEN_CUSTOM_CLASSES, "");
                if (!customClassesString.trim().equals("")) {
                    String[] customClasses = customClassesString.split(";");
                    for (String customClass : customClasses) {
                        Class<? extends CaseScenarioGenerator> clazz =
                                (Class<? extends CaseScenarioGenerator>) Class.forName(customClass);
                        add(clazz);
                    }
                }
            }
        };
        return result;
    }

    public String generate() throws Exception {
        InputSection input = (InputSection) this.getSections().get(Tokens.getInputToken()).get(0);
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
