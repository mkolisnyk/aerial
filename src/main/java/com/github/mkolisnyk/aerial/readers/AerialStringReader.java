/**
 * .
 */
package com.github.mkolisnyk.aerial.readers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.core.AerialTagList;
import com.github.mkolisnyk.aerial.core.params.AerialParams;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialStringReader extends AerialReader {

    /**
     * .
     */
    private List<String> content;
    /**
     * .
     */
    private Iterator<String> iterator;
    /**
     * .
     * @throws Exception .
     */
    public AerialStringReader(AerialParams params, AerialTagList tags) throws Exception {
        super(params, tags);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#open(java.lang.Object[])
     */
    public final void open(AerialParams params) throws Exception {
        content = new ArrayList<String>();
        for (String param : params.getValueParams()) {
            content.add(param);
        }
        iterator = content.iterator();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#close()
     */
    public final void close() throws Exception {
        if (content != null) {
            content.clear();
            content = null;
        }
        iterator = null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#readNext()
     */
    public final String readNext() {
        if (hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#hasNext()
     */
    public final boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

}
