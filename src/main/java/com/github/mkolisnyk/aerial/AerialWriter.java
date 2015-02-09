/**
 * .
 */
package com.github.mkolisnyk.aerial;

import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.document.Document;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class AerialWriter {

    private AerialTagList tags;

    public AerialWriter(AerialParams params, AerialTagList tagsValue) {
        params.toString();
        tags = tagsValue;
    }

    /**
     * @return the tags
     */
    public final AerialTagList getTags() {
        return tags;
    }
    /**
     * .
     * @param params .
     * @throws Exception .
     */
    public abstract void open(Document document, Object... params) throws Exception;
    /**
     * .
     * @throws Exception .
     */
    public abstract void close() throws Exception;
    /**
     * .
     * @return .
     * @throws Exception .
     */
    public abstract String writeNext() throws Exception;
    /**
     * .
     * @return .
     */
    public abstract boolean hasNext();
}
