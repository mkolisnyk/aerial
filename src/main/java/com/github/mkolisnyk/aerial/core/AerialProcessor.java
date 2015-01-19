/**
 * .
 */
package com.github.mkolisnyk.aerial.core;

import org.apache.log4j.Logger;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;
import com.github.mkolisnyk.aerial.document.Document;
import com.github.mkolisnyk.aerial.util.LoggerFactory;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialProcessor {

    private static final Logger LOG = LoggerFactory.create(AerialProcessor.class);
    private String content;
    /**
     * .
     */
    public AerialProcessor() {
        this.content = "";
    }

    public void process(AerialReader input, AerialWriter output)
            throws Exception {
        LOG.info("Start processing input data");
        LOG.debug("Generation format is: " + AerialOutputFormat.getCurrent());
        while (input.hasNext()) {
            Document document = new Document();
            document = (Document) document.parse(input.readNext());
            output.open(document);
            while (output.hasNext()) {
                content = content.concat(output.writeNext());
            }
            output.close();
        }
        LOG.info("Processing was ended");
    }

    /**
     * @return the content
     */
    public final String getContent() {
        return content;
    }
}
