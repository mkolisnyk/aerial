/**
 * .
 */
package com.github.mkolisnyk.aerial.readers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.mkolisnyk.aerial.AerialReader;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialFileReader implements AerialReader {

    private List<File> files;
    private Iterator<File> iterator;

    /**
     * .
     */
    public AerialFileReader() {
        this.files = null;
        this.iterator = null;
    }

    /**
     * @return the files
     */
    public final List<File> getFiles() {
        return files;
    }

    private List<File> listFiles(File rootFolder, List<String> matchPatterns) {
        List<File> outList = new ArrayList<File>();
        for (String name : rootFolder.list()) {
            File child = new File(name);
            if (child.isDirectory()) {
                outList.addAll(listFiles(child, matchPatterns));
            } else {
                for (String pattern : matchPatterns) {
                    if (child.getAbsolutePath().matches(pattern)) {
                        outList.add(child);
                        break;
                    }
                }
            }
        }
        return outList;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#open(java.lang.Object[])
     */
    public void open(Object... params) throws Exception {
        File root = new File("./");
        List<String> matchPatterns = new ArrayList<String>();
        for (int i = 0; i < params.length; i++) {
            matchPatterns.add((String) params[i]);
        }
        this.files = listFiles(root, matchPatterns);
        this.iterator = this.files.iterator();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#close()
     */
    public void close() throws Exception {
        if (this.files != null) {
            this.files.clear();
        }
        this.files = null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#readNext()
     */
    public String readNext() throws Exception {
        if (this.iterator != null && this.hasNext()) {
            return FileUtils.readFileToString(this.iterator.next());
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#hasNext()
     */
    public boolean hasNext() {
        if (this.iterator != null) {
            return this.iterator.hasNext();
        }
        return false;
    }

}
