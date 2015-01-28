package com.github.mkolisnyk.aerial.core.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.readers.AerialFileReader;
import com.github.mkolisnyk.aerial.readers.AerialJiraReader;
//import com.github.mkolisnyk.aerial.readers.AerialJiraReader;
import com.github.mkolisnyk.aerial.readers.AerialStringReader;
import com.github.mkolisnyk.aerial.writers.AerialFileWriter;
import com.github.mkolisnyk.aerial.writers.AerialStringWriter;

public class AerialParams {

    private AerialSourceType inputType;
    private String source;
    private AerialSourceType outputType;
    private String destination;
    private Map<String, String> namedParams;
    private List<String> valueParams;
    private AerialOutputFormat format;
    private String configuration;

    public AerialParams() {
        this.inputType = AerialSourceType.NONE;
        this.source = "";
        this.outputType = AerialSourceType.NONE;
        this.destination = "";
        this.namedParams = new HashMap<String, String>();
        this.valueParams = new ArrayList<String>();
        this.format = AerialOutputFormat.CUCUMBER;
        this.configuration = "";
    }

    /**
     * @return the inputType
     */
    public final AerialSourceType getInputType() {
        return inputType;
    }

    /**
     * @return the source
     */
    public final String getSource() {
        return source;
    }

    /**
     * @return the outputType
     */
    public final AerialSourceType getOutputType() {
        return outputType;
    }

    /**
     * @return the destination
     */
    public final String getDestination() {
        return destination;
    }

    /**
     * @return the format
     */
    public final AerialOutputFormat getFormat() {
        return format;
    }

    /**
     * @return the extraParams
     */
    public final Map<String, String> getNamedParams() {
        return namedParams;
    }

    /**
     * @return the valueParams
     */
    public final List<String> getValueParams() {
        return valueParams;
    }

    /**
     * @return the configuration
     */
    public final String getConfiguration() {
        return configuration;
    }

    public void parse(String[] args) {
        int index = 0;
        this.namedParams = new HashMap<String, String>();
        this.valueParams = new ArrayList<String>();
        while (index < args.length) {
            AerialParamKeys key = AerialParamKeys.fromString(args[index]);
            switch (key) {
                case INPUT_TYPE:
                    this.inputType = AerialSourceType.fromString(args[++index]);
                    break;
                case OUTPUT_TYPE:
                    this.outputType = AerialSourceType.fromString(args[++index]);
                    break;
                case SOURCE:
                    this.source = args[++index];
                    break;
                case DESTINATION:
                    this.destination = args[++index];
                    break;
                case FORMAT:
                    this.format = AerialOutputFormat.fromString(args[++index]);
                    break;
                case CONFIGURATION:
                    this.configuration = args[++index];
                    break;
                default:
                    String param = args[index];
                    if (param.matches("([^=]+)[^\\\\]=(.*)")) {
                        this.namedParams.put(param.split("=")[0], param.substring(param.indexOf("=") + 1));
                    } else {
                        this.valueParams.add(param.replaceAll("\\\\=", "="));
                    }
                    break;
            }
            index++;
        }
    }

    public void validate() {
        Assert.assertNotNull("The input type is undefined", this.getInputType());
        Assert.assertNotEquals("Illegal input type", AerialSourceType.NONE, this.getInputType());
        Assert.assertNotNull("The output type is undefined", this.getOutputType());
        Assert.assertNotEquals("Illegal output type", AerialSourceType.NONE, this.getOutputType());
        Assert.assertNotNull("The source value is undefined", this.getSource());
        Assert.assertNotNull("The destination value is undefined", this.getDestination());
    }

    public AerialReader getReader() throws Exception {
        AerialReader reader = null;
        switch (this.getInputType()) {
            case STRING:
                reader = new AerialStringReader(this);
                break;
            case FILE:
                reader = new AerialFileReader(this);
                break;
            case JIRA:
                reader = new AerialJiraReader(this);
                break;
            default:
                break;
        }
        return reader;
    }

    public AerialWriter getWriter() {
        AerialWriter writer = null;
        switch (this.getOutputType()) {
            case STRING:
                writer = new AerialStringWriter();
                break;
            case FILE:
                writer = new AerialFileWriter(getDestination());
                break;
            default:
                break;
        }
        return writer;
    }

    public void usage() {
    }
}
