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
 * Generates Cucumber feature files based on Aerial document descriptions.
 * Input documents can be stored in many accessible types (controlled by
 * {@link AerialMavenGeneratorPlugin#inputType} field.
 * @author Myk Kolisnyk
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class AerialMavenGeneratorPlugin extends AbstractMojo {
    /**
     * Identifies input type to use for source data. Available values are: FILE, JIRA, STRING, CUSTOM.
     * @since 0.0.1
     */
    @Parameter(property = "aerial.input.type", defaultValue = "FILE")
    private AerialSourceType inputType;
    /**
     * Identifies the actual source to get data from. Depending on {@link AerialMavenGeneratorPlugin#inputType}
     * parameter value it can be:
     * <ul>
     * <li> For FILE - main folder where to get document files from
     * <li> For JIRA - base JIRA URL to send queries to
     * <li> For STRING - ignored as not needed
     * <li> For CUSTOM - anything that's defined in appropriate handler
     * </ul>
     * @since 0.0.1
     */
    @Parameter(property = "aerial.input.source",
            defaultValue = "", required = true)
    private String source;
    /**
     * Identifies path to global configuration file (if specified)
     * @since 0.0.4
     */
    @Parameter(property = "aerial.global.config", defaultValue = "")
    private String configurationFile;
    /**
     * Identifies output type to use for generated data. Available values are: FILE, CUSTOM.
     * @since 0.0.1
     */
    @Parameter(property = "aerial.output.type", defaultValue = "FILE")
    private AerialSourceType outputType;
    /**
     * Identifies output location depending on {@link AerialMavenGeneratorPlugin#outputType}.
     * For FILE it indicates output directory.
     * @since 0.0.1
     */
    @Parameter(property = "aerial.output.destination",
            defaultValue = "", required = true)
    private String destination;
    /**
     * Some sources require additional information to be passed in addition to input and output locations.
     * E.g. JIRA source additionally requires user name, password and field name. Some other sources may
     * require something else. Main feature is that this field contain parameters with known name.
     * @since 0.0.1
     */
    @Parameter
    private Map<String, String> namedParams;
    /**
     * Contains the list of additional values passed with the input sources.
     * Mainly they contain some search filters or input strings which can be passed
     * in big quantity and processed uniformly. E.g. :
     * <ul>
     * <li> For STRING source type this is the actual container of input text.
     * <li> For FILE source it is the list of file name filters.
     * <li> For JIRA source it is the list of JQL queries to use for data retrieval.
     * </ul>
     * @since 0.0.1
     */
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
        params.add(AerialParamKeys.CONFIGURATION.toString());
        params.add(configurationFile);
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
     * @param configurationValue the configuration to set
     */
    public final void setConfigurationFile(String configurationValue) {
        this.configurationFile = configurationValue;
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

