package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialGenerator;

public abstract class ContainerSection
                extends DocumentSection<ContainerSection>
                implements AerialGenerator {

    private Map<String, ArrayList<DocumentSection<?>>> sections;

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
    public Map<String, ArrayList<DocumentSection<?>>> getSections() {
        return sections;
    }

    public ContainerSection(ContainerSection container) {
        this(container, null);
    }

    public ContainerSection(ContainerSection container, String tag) {
        super(container, tag);
        this.description = "";
        this.sections = new HashMap<String, ArrayList<DocumentSection<?>>>();
    }

    public abstract String[] getSectionTokens();
    public abstract String[] getMandatoryTokens();
    public abstract Map<String, Class<?>> getCreationMap();

    private DocumentSection<?> createSection(String token) throws Exception {
        Map<String, Class<?>> creationMap = getCreationMap();
        if (creationMap.containsKey(token)) {
            Class<?> clazz = creationMap.get(token);
            return (DocumentSection<?>) clazz
                        .getConstructor(ContainerSection.class, String.class)
                        .newInstance(this, this.getTag());
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
            ArrayList<DocumentSection<?>> sectionItem = new ArrayList<DocumentSection<?>>();
            if (this.sections.containsKey(currentSectionName)) {
                sectionItem = this.sections.get(currentSectionName);
            }
            sectionItem.add(currentSection.parse(content));
            this.sections.put(
                    currentSectionName,
                    sectionItem);
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
                if (line.trim().startsWith(token)) {
                    isToken = true;
                    tokenFound = token;
                    break;
                }
            }
            if (isToken) {
                setCurrentSection(currentSectionName, currentSection, content);
                currentSectionName = tokenFound;
                currentSection = createSection(tokenFound);
                String trimLine = line.trim();
                if (trimLine.equals(tokenFound)) {
                    currentSection.setName("");
                } else {
                    currentSection.setName(trimLine.substring(tokenFound.length()).trim());
                }
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

    public void validate() throws Exception {
        for (String token:this.getMandatoryTokens()) {
            Assert.assertTrue(this.sections.containsKey(token));
            for (DocumentSection<?> item : this.sections.get(token)) {
                item.validate();
            }
        }
        for (String token:this.getSectionTokens()) {
            if (!ArrayUtils.contains(this.getMandatoryTokens(), token)
                    && this.sections.containsKey(token)) {
                for (DocumentSection<?> item : this.sections.get(token)) {
                    item.validate();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends DocumentSection<?>> T findNamedSectionInParent(String name, String token, Class<T> clazz) {
        ContainerSection container = (ContainerSection) this.getParent();
        while (container != null) {
            Map<String, ArrayList<DocumentSection<?>>> sectionMap = container.getSections();
            if (sectionMap.containsKey(token)) {
                ArrayList<DocumentSection<?>> sectionsList = sectionMap.get(token);
                for (DocumentSection<?> section : sectionsList) {
                    if (section.getName().equals(name) && section != (DocumentSection<T>) this) {
                        return (T) section;
                    }
                }
            }
            container = container.getParent();
        }
        return null;
    }
}
