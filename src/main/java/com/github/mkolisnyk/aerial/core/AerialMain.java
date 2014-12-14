/**
 * .
 */
package com.github.mkolisnyk.aerial.core;

import com.github.mkolisnyk.aerial.core.params.AerialParams;

/**
 * @author Myk Kolisnyk
 *
 */
public final class AerialMain {

    /**
     * .
     */
    private AerialMain() {
    }

    /**
     * @param args .
     * @throws Exception .
     */
    public static void main(final String[] args) throws Exception {
        AerialParams params = new AerialParams();
        try {
            params.parse(args);
            params.validate();
        } catch (Throwable e) {
            params.usage();
            return;
        }
        AerialProcessor processor = new AerialProcessor();
        processor.process(params.getReader(), params.getWriter());
    }

}
