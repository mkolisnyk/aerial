/**
 * .
 */
package com.github.mkolisnyk.aerial;

import com.github.mkolisnyk.aerial.document.Document;

/**
 * @author Myk Kolisnyk
 *
 */
public interface AerialWriter {
    /**
     * .
     * @param params .
     * @throws Exception .
     */
    void open(Document document, Object... params) throws Exception;
    /**
     * .
     * @throws Exception .
     */
    void close() throws Exception;
    /**
     * .
     * @return .
     */
    void writeNext();
    /**
     * .
     * @return .
     */
    boolean hasNext();
}
