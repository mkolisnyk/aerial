/**
 * 
 */
package com.github.mkolisnyk.aerial.core;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.document.Document;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialProcessor {

    /**
     * .
     */
    public AerialProcessor() {
    }

    public void process(AerialReader input, AerialWriter output)
            throws Exception {
        Document document = new Document();
        while (input.hasNext()) {
            document = (Document) document.parse(input.readNext());
            output.open(document);
            while (output.hasNext()) {
                output.writeNext();
            }
            output.close();
        }
    }
}
