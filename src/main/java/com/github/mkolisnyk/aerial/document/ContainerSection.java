package com.github.mkolisnyk.aerial.document;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialGenerator;

public abstract class ContainerSection
                extends DocumentSection<ContainerSection>
                implements AerialGenerator {

    private Map<String, DocumentSection<?>> sections;

    private String description;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the sections
     */
    public Map<String, DocumentSection<?>> getSections() {
        return sections;
    }

    public ContainerSection(DocumentSection<?> container) {
        super(container);
        this.description = "";
        this.sections = new HashMap<String, DocumentSection<?>>();
    }

    public abstract String[] getSectionTokens();
    public abstract String[] getMandatoryTokens();
    public abstract Map<String, Class<?>> getCreationMap();

    private DocumentSection<?> createSection(String token) throws Exception {
        Map<String, Class<?>> creationMap = getCreationMap();
        if (creationMap.containsKey(token)) {
            Class<?> clazz = creationMap.get(token);
            return (DocumentSection<?>) clazz
                        .getConstructor(DocumentSection.class)
                        .newInstance(this);
        }
        return null;
    }

    private void setCurrentSection(
            String currentSectionName,
            DocumentSection<?> currentSection,
            String content) throws Exception {
        if (currentSection == null) {
            this.description = content;
            content = "";
        } else {
            this.sections.put(
                    currentSectionName,
                    currentSection.parse(content));
        }
    }
    public ContainerSection parse(String input) throws Exception {
        String currentSectionName = "";
        DocumentSection<?> currentSection = null;
        String content = "";
        String separator = System.lineSeparator();

        for (String line:input.split(separator)) {
            boolean isToken = false;
            String tokenFound = "";
            for (String token : this.getSectionTokens()) {
                if (line.trim().equals(token)) {
                    isToken = true;
                    tokenFound = line.trim();
                    break;
                }
            }
            if (isToken) {
                setCurrentSection(currentSectionName, currentSection, content);
                currentSectionName = tokenFound;
                currentSection = createSection(tokenFound);
                content = "";
            } else {
                if (content.equals("")) {
                    content = content.concat(line);
                } else {
                    content = content.concat(separator + line);
                }
            }
        }
        setCurrentSection(currentSectionName, currentSection, content);
        return this;
    }

    public void validate() {
        for (String token:this.getMandatoryTokens()) {
            Assert.assertTrue(this.sections.containsKey(token));
            this.sections.get(token).validate();
        }
        for (String token:this.getSectionTokens()) {
            if (!ArrayUtils.contains(this.getMandatoryTokens(), token)
                    && this.sections.containsKey(token)) {
                this.sections.get(token).validate();
            }
        }
    }
}
