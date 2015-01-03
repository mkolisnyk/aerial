/**
 * .
 */
package com.github.mkolisnyk.aerial;

import com.github.mkolisnyk.aerial.core.params.AerialParams;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class AerialReader {
    /**
     * .
     * @param params .
     */
    public  AerialReader(AerialParams params) {
        params.toString();
    }
    /**
     * .
     * @param params .
     * @throws Exception .
     */
    public abstract void open(AerialParams params) throws Exception;
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
    public abstract String readNext() throws Exception;
    /**
     * .
     * @return .
     */
    public abstract boolean hasNext();
}
