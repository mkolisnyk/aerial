/**
 * .
 */
package com.github.mkolisnyk.aerial;

/**
 * @author Myk Kolisnyk
 *
 */
public interface AerialReader {
    /**
     * .
     * @param params .
     * @throws Exception .
     */
    void open(Object... params) throws Exception;
    /**
     * .
     * @throws Exception .
     */
    void close() throws Exception;
    /**
     * .
     * @return .
     * @throws Exception .
     */
    String readNext() throws Exception;
    /**
     * .
     * @return .
     */
    boolean hasNext();
}
