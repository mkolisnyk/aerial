/**
 * .
 */
package com.github.mkolisnyk.aerial.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

/**
 * @author Myk Kolisnyk
 *
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class AerialMavenGeneratorPlugin extends AbstractMojo {
    /**
     * .
     */
    @Parameter(property = "aerial.input.type", defaultValue = "FILE")
    private AerialSourceType inputType;
    /**
     * .
     */
    @Parameter(property = "aerial.input.source",
            defaultValue = "", required = true)
    private String source;
    /**
     * .
     */
    @Parameter(property = "aerial.output.type", defaultValue = "FILE")
    private AerialSourceType outputType;
    /**
     * .
     */
    @Parameter(property = "aerial.output.destination",
            defaultValue = "", required = true)
    private String destination;
    @Parameter
    private Map<String, String> namedParams;
    @Parameter
    private List<String> valueParams;

    /**
     * .
     */
    public void execute() throws MojoExecutionException,
            MojoFailureException {
        ArrayList<String> params = new ArrayList<String>();
        params.add(AerialParamKeys.INPUT_TYPE.toString());
        params.add(inputType.toString());
        params.add(AerialParamKeys.SOURCE.toString());
        params.add(source);
        params.add(AerialParamKeys.OUTPUT_TYPE.toString());
        params.add(outputType.toString());
        params.add(AerialParamKeys.DESTINATION.toString());
        params.add(destination);
        if (namedParams != null) {
            for (Entry<String, String> entry : namedParams.entrySet()) {
                params.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        if (valueParams != null) {
            for (String param : valueParams) {
                param = param.replaceAll("=", "\\=");
                params.add(param);
            }
        }
        String[] paramsArray = new String[params.size()];
        paramsArray = params.toArray(paramsArray);
        try {
            AerialMain.main(paramsArray);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoFailureException("Failed to execute generation");
        }
    }

    /**
     * @param inputTypeValue the inputType to set
     */
    public final void setInputType(AerialSourceType inputTypeValue) {
        this.inputType = inputTypeValue;
    }

    /**
     * @param sourceValue the source to set
     */
    public final void setSource(String sourceValue) {
        this.source = sourceValue;
    }

    /**
     * @param outputTypeValue the outputType to set
     */
    public final void setOutputType(AerialSourceType outputTypeValue) {
        this.outputType = outputTypeValue;
    }

    /**
     * @param destinationValue the destination to set
     */
    public final void setDestination(String destinationValue) {
        this.destination = destinationValue;
    }

    /**
     * @param extraParamsValue the extraParams to set
     */
    public final void setNamedParams(Map<String, String> extraParamsValue) {
        this.namedParams = extraParamsValue;
    }

    /**
     * @param valueParamsValue the valueParams to set
     */
    public final void setValueParams(List<String> valueParamsValue) {
        this.valueParams = valueParamsValue;
    }
}

