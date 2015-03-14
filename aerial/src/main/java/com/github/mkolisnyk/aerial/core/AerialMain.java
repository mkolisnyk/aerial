/**
 * .
 */
package com.github.mkolisnyk.aerial.core;

import org.apache.commons.lang.ArrayUtils;

import com.github.mkolisnyk.aerial.annotations.Aerial;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

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

    public static String[] toArgs(Class<?> clazz) {
        Aerial annotation = clazz.getAnnotation(Aerial.class);
        String[] args = (String[]) ArrayUtils.addAll(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(),
                annotation.inputType().toString(),
                AerialParamKeys.SOURCE.toString(),
                annotation.source(),
                AerialParamKeys.OUTPUT_TYPE.toString(),
                AerialSourceType.FILE.toString(),
                AerialParamKeys.DESTINATION.toString(),
                annotation.destination()}, annotation.additionalParams());
        return args;
    }

    /**
     * @param args .
     * @throws Exception .
     */
    public static void main(final String[] args) throws Exception {
        AerialParams params = new AerialParams();
        try {
            params.parse(args);
            params.apply();
            params.validate();
        } catch (Throwable e) {
            params.usage();
            return;
        }
        params.getFormat().setCurrent();
        AerialProcessor processor = new AerialProcessor();
        processor.process(params.getReader(), params.getWriter());
    }

}
