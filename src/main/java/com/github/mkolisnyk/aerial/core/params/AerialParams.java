package com.github.mkolisnyk.aerial.core.params;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;

public class AerialParams {

    private AerialSourceType inputType;
    private String source;
    private AerialSourceType outputType;
    private String destination;
    private Map<String, String> extraParams;

    public AerialParams() {
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
     * @return the extraParams
     */
    public final Map<String, String> getExtraParams() {
        return extraParams;
    }

    public void parse(String[] args) {
        int index = 0;
        this.extraParams = new HashMap<String, String>();
        while (index < args.length) {
            AerialParamKeys key = AerialParamKeys.fromString(args[index]);
            switch (key) {
                case INPUT_TYPE:
                    this.inputType = AerialSourceType.fromString(args[index++]);
                    break;
                case OUTPUT_TYPE:
                    this.outputType = AerialSourceType.fromString(args[index++]);
                    break;
                case SOURCE:
                    this.source = args[index++];
                    break;
                case DESTINATION:
                    this.destination = args[index++];
                    break;
                default:
                    String param = args[index];
                    if (param.matches("([^=]+)=(.*)")) {
                        this.extraParams.put(param.split("=")[0], param.substring(param.indexOf("=")));
                    } else {
                        throw new IllegalArgumentException();
                    }
                    break;
            }
            index++;
        }
    }

    public void validate() {
        Assert.assertNotNull("The input type is undefined", this.getInputType());
        Assert.assertNotNull("The output type is undefined", this.getOutputType());
        Assert.assertNotNull("The source value is undefined", this.getSource());
        Assert.assertNotNull("The destination value is undefined", this.getDestination());
    }

    public AerialReader getReader() {
        return null;
    }

    public AerialWriter getWriter() {
        return null;
    }

    public void usage() {
    }
}
