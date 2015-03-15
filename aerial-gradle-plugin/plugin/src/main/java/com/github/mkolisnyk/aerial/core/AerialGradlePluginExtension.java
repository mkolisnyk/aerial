package com.github.mkolisnyk.aerial.core;

import java.util.List;
import java.util.Map;

import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

public class AerialGradlePluginExtension {

    private AerialSourceType inputType;
    private String source;
    private String configurationFile;
    private AerialSourceType outputType;
    private String destination;
    private Map<String, String> namedParams;
    private List<String> valueParams;

    
    /**
     * @return the inputType
     */
    public final AerialSourceType getInputType() {
        return inputType;
    }


    /**
     * @param inputTypeValue the inputType to set
     */
    public final void setInputType(AerialSourceType inputTypeValue) {
        this.inputType = inputTypeValue;
    }


    /**
     * @return the source
     */
    public final String getSource() {
        return source;
    }


    /**
     * @param sourceValue the source to set
     */
    public final void setSource(String sourceValue) {
        this.source = sourceValue;
    }


    /**
     * @return the configurationFile
     */
    public final String getConfigurationFile() {
        return configurationFile;
    }


    /**
     * @param configurationFileValue the configurationFile to set
     */
    public final void setConfigurationFile(String configurationFileValue) {
        this.configurationFile = configurationFileValue;
    }


    /**
     * @return the outputType
     */
    public final AerialSourceType getOutputType() {
        return outputType;
    }


    /**
     * @param outputTypeValue the outputType to set
     */
    public final void setOutputType(AerialSourceType outputTypeValue) {
        this.outputType = outputTypeValue;
    }


    /**
     * @return the destination
     */
    public final String getDestination() {
        return destination;
    }


    /**
     * @param destinationValue the destination to set
     */
    public final void setDestination(String destinationValue) {
        this.destination = destinationValue;
    }


    /**
     * @return the namedParams
     */
    public final Map<String, String> getNamedParams() {
        return namedParams;
    }


    /**
     * @param namedParamsValue the namedParams to set
     */
    public final void setNamedParams(Map<String, String> namedParamsValue) {
        this.namedParams = namedParamsValue;
    }


    /**
     * @return the valueParams
     */
    public final List<String> getValueParams() {
        return valueParams;
    }


    /**
     * @param valueParamsValue the valueParams to set
     */
    public final void setValueParams(List<String> valueParamsValue) {
        this.valueParams = valueParamsValue;
    }

}
