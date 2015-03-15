/**
 * .
 */
package com.github.mkolisnyk.aerial.util;

import java.util.Date;

import org.joda.time.DateTime;

/**
 * @author Myk Kolisnyk
 *
 */
public class SystemClock implements Clock {

    public SystemClock() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.util.Clock#now()
     */
    public Date now() {
        return DateTime.now().toDate();
    }

}
