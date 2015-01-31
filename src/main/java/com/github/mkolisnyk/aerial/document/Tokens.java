/**
 * .
 */
package com.github.mkolisnyk.aerial.document;

import com.github.mkolisnyk.aerial.core.params.AerialInputFormat;
import com.github.mkolisnyk.aerial.core.templates.AerialInputTemplateMap;

/**
 * @author Myk Kolisnyk
 *
 */
public final class Tokens {

    private static String actionToken = "Action:";
    private static String prerequisitesToken = "Pre-requisites:";
    private static String inputToken = "Input:";
    private static String validOutputToken = "On Success:";
    private static String errorOutputToken = "On Failure:";

    private static String featureToken = "Feature:";
    private static String caseToken = "Case:";
    private static String additionalScenariosToken
                                    = "Additional Scenarios:";

    /**
     * @return the actionToken
     */
    public static String getActionToken() {
        return actionToken;
    }
    /**
     * @return the prerequisitesToken
     */
    public static String getPrerequisitesToken() {
        return prerequisitesToken;
    }
    /**
     * @return the inputToken
     */
    public static String getInputToken() {
        return inputToken;
    }
    /**
     * @return the validOutputToken
     */
    public static String getValidOutputToken() {
        return validOutputToken;
    }
    /**
     * @return the errorOutputToken
     */
    public static String getErrorOutputToken() {
        return errorOutputToken;
    }
    /**
     * @return the featureToken
     */
    public static String getFeatureToken() {
        return featureToken;
    }
    /**
     * @return the caseToken
     */
    public static String getCaseToken() {
        return caseToken;
    }
    /**
     * @return the additionalScenariosToken
     */
    public static String getAdditionalScenariosToken() {
        return additionalScenariosToken;
    }
    public static void refresh() throws Exception {
        String format = AerialInputFormat.getCurrent().toString();
        actionToken = AerialInputTemplateMap.get(format, "token.action");
        prerequisitesToken = AerialInputTemplateMap.get(format, "token.prerequisites");
        inputToken = AerialInputTemplateMap.get(format, "token.input");
        validOutputToken = AerialInputTemplateMap.get(format, "token.valid_output");
        errorOutputToken = AerialInputTemplateMap.get(format, "token.error_output");

        featureToken = AerialInputTemplateMap.get(format, "token.feature");
        caseToken = AerialInputTemplateMap.get(format, "token.case");
        additionalScenariosToken = AerialInputTemplateMap.get(format, "token.additional_scenarios");
    }
    /**
     * .
     */
    private Tokens() {
    }
}
