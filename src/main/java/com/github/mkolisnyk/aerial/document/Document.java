/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Myk Kolisnyk
 *
 */
public class Document extends ContainerSection {

    private List<FeatureSection> features;

    /**
     * .
     */
    public Document() {
        super(null);
        features = new ArrayList<FeatureSection>();
    }

    /**
     * @return the cases
     */
    public final List<FeatureSection> getFeatures() {
        return features;
    }

    @Override
    public String[] getSectionTokens() {
        return new String[] {
                Tokens.FEATURE_TOKEN
        };
    }

    @Override
    public String[] getMandatoryTokens() {
        return new String[] {
                Tokens.FEATURE_TOKEN
        };
    }

    @Override
    public Map<String, Class<?>> getCreationMap() {
        return new HashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;
            {
                put(Tokens.FEATURE_TOKEN, FeatureSection.class);
            }
        };
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.document.ContainerSection#parse(java.lang.String)
     */
    @Override
    public ContainerSection parse(String input) throws Exception {
        super.parse(input);
        features = new ArrayList<FeatureSection>();
        ArrayList<DocumentSection<?>> section = this.getSections().get(Tokens.FEATURE_TOKEN);
        if (section != null) {
            for (DocumentSection<?> item : section) {
                features.add((FeatureSection) item);
            }
        }
        return this;
    }

    public String generate() {
        return null;
    }
}
