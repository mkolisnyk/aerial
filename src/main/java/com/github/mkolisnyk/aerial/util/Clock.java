/**
 * .
 */
package com.github.mkolisnyk.aerial.util;

import java.util.Date;

/**
 * @author Myk Kolisnyk
 *
 */
public interface Clock {
    Date now() throws Exception;
}
