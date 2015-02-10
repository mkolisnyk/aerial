/**
 * .
 */
package com.github.mkolisnyk.aerial.writers;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.document.Document;
import com.github.mkolisnyk.aerial.document.FeatureSection;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialFileWriter extends AerialWriter {
    private Iterator<FeatureSection> iterator;
    private String outputFolder = "";
    /**
     * .
     */
    public AerialFileWriter(AerialParams params) {
        super(params);
        this.iterator = null;
        this.outputFolder = params.getDestination();
    }

    public void open(Document document, Object... params)
            throws Exception {
        if (document == null) {
            return;
        }
        this.iterator = document.getFeatures().iterator();
    }

    public void close() throws Exception {
        if (this.iterator != null) {
            this.iterator = null;
        }
    }

    public String writeNext() throws Exception {
        if (this.iterator != null && this.hasNext()) {
            FeatureSection section = this.iterator.next();
            String text = section.generate();
            File output = new File(this.outputFolder + File.separator
                    + section.getName().replaceAll(" ", "") + ".feature");
            //output.getParentFile().mkdirs();
            FileUtils.writeStringToFile(output, text);
            return text;
        }
        return null;
    }

    public boolean hasNext() {
        if (this.iterator != null) {
            return iterator.hasNext();
        }
        return false;
    }
}
