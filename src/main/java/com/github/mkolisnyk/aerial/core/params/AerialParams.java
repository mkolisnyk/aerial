package com.github.mkolisnyk.aerial.core.params;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.core.AerialGlobalProperties;
import com.github.mkolisnyk.aerial.document.Tokens;
import com.github.mkolisnyk.aerial.readers.AerialFileReader;
import com.github.mkolisnyk.aerial.readers.AerialJiraReader;
import com.github.mkolisnyk.aerial.readers.AerialStringReader;
import com.github.mkolisnyk.aerial.writers.AerialFileWriter;
import com.github.mkolisnyk.aerial.writers.AerialStringWriter;

public class AerialParams implements AerialGlobalProperties {

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
        this.format = null;
        this.configuration = "main/resources/aerial.properties";
    }

    private void loadGlobalProperties(String path) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        Properties props = new Properties();
        props.load(in);
        in.close();
        for (Entry<Object, Object> entry : System.getProperties().entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }
        System.setProperties(props);
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

    /**
     * @return the readersMap
     */
    public final Map<AerialSourceType, Class<? extends AerialReader>> getReadersMap() {
        return new HashMap<AerialSourceType, Class<? extends AerialReader>>() {
            private static final long serialVersionUID = 1L;

            {
                put(AerialSourceType.STRING, AerialStringReader.class);
                put(AerialSourceType.FILE, AerialFileReader.class);
                put(AerialSourceType.JIRA, AerialJiraReader.class);
            }
        };
    }

    /**
     * @return the readersMap
     */
    public final Map<AerialSourceType, Class<? extends AerialWriter>> getWritersMap() {
        return new HashMap<AerialSourceType, Class<? extends AerialWriter>>() {
            private static final long serialVersionUID = 1L;

            {
                put(AerialSourceType.STRING, AerialStringWriter.class);
                put(AerialSourceType.FILE, AerialFileWriter.class);
            }
        };
    }

    public void parse(String[] args) throws Exception {
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

    public void apply() throws Exception {
        loadGlobalProperties(this.getConfiguration());
        if (this.format == null) {
            this.format = AerialOutputFormat.fromString(System.getProperty(AERIAL_OUTPUT_FORMAT));
        }
        Tokens.refresh();
    }

    public void validate() throws Exception {
        Assert.assertNotNull("The input type is undefined", this.getInputType());
        Assert.assertNotEquals("Illegal input type", AerialSourceType.NONE, this.getInputType());
        Assert.assertNotNull("The output type is undefined", this.getOutputType());
        Assert.assertNotEquals("Illegal output type", AerialSourceType.NONE, this.getOutputType());
        Assert.assertNotNull("The source value is undefined", this.getSource());
        Assert.assertNotNull("The destination value is undefined", this.getDestination());
        if (this.getInputType().equals(AerialSourceType.CUSTOM)) {
            Assert.assertTrue("If custom class is defined you must pass the 'readerClass' named parameter",
                    this.getNamedParams().containsKey("readerClass"));
            Class<?> clazz = Class.forName(this.getNamedParams().get("readerClass"));
            this.getReadersMap().put(
                    AerialSourceType.CUSTOM,
                    clazz.asSubclass(AerialReader.class));
        }
        if (this.getOutputType().equals(AerialSourceType.CUSTOM)) {
            Assert.assertTrue("If custom class is defined you must pass the 'writerClass' named parameter",
                    this.getNamedParams().containsKey("writerClass"));
            Class<?> clazz = Class.forName(this.getNamedParams().get("writerClass"));
            this.getWritersMap().put(
                    AerialSourceType.CUSTOM,
                    clazz.asSubclass(AerialWriter.class));
        }
    }

    public AerialReader getReader() throws Exception {
        AerialReader reader = null;
        reader = this.getReadersMap().get(this.getInputType()).getConstructor(AerialParams.class).newInstance(this);
        return reader;
    }

    public AerialWriter getWriter() throws Exception {
        AerialWriter writer = null;
        writer = this.getWritersMap().get(this.getOutputType()).getConstructor(AerialParams.class).newInstance(this);
        return writer;
    }

    public void usage() {
    }
}
