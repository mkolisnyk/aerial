package com.github.mkolisnyk.aerial.writers;

import java.util.Iterator;

import com.github.mkolisnyk.aerial.AerialTagList;
import com.github.mkolisnyk.aerial.AerialWriter;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.document.Document;
import com.github.mkolisnyk.aerial.document.FeatureSection;

public class AerialStringWriter extends AerialWriter {

    private Iterator<FeatureSection> iterator;
    public AerialStringWriter(AerialParams params, AerialTagList tags) {
        super(params, tags);
        this.iterator = null;
    }

    public void open(Document document, Object... params)
            throws Exception {
        this.iterator = document.getFeatures().iterator();
    }

    public void close() throws Exception {
        if (this.iterator != null) {
            this.iterator = null;
        }
    }

    public String writeNext() throws Exception {
        if (this.iterator != null) {
            return this.iterator.next().generate();
        }
        return null;
    }

    public boolean hasNext() {
        if (this.iterator != null) {
            return iterator.hasNext();
        }
        return false;
    }
}
