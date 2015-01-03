/**
 * .
 */
package com.github.mkolisnyk.aerial.readers;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.util.LoggerFactory;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialFileReader extends AerialReader {
    private static final Logger LOG = LoggerFactory.create(AerialFileReader.class);
    private List<File> files;
    private Iterator<File> iterator;
    private String rootDirectory;

    /**
     * .
     * @throws Exception .
     */
    /*public AerialFileReader(String rootDirectoryValue) {
        LOG.info("Initializing File Reader");
        this.files = new ArrayList<File>();
        this.iterator = this.files.iterator();
        this.rootDirectory = rootDirectoryValue;
    }*/
    public AerialFileReader(AerialParams params) throws Exception {
        super(params);
        this.files = new ArrayList<File>();
        this.iterator = this.files.iterator();
        this.rootDirectory = params.getSource();
        if (params.getValueParams().size() == 0) {
            params.getValueParams().add("(.*)");
        }
        this.open(params);
    }

    /**
     * @return the files
     */
    public final List<File> getFiles() {
        return files;
    }

    private boolean compareFileName(String path, String pattern) {
        LOG.debug(String.format("Check if \"%s\" path contains the \"%s\" pattern", path, pattern));
        if (path.contains(pattern)) {
            return true;
        }
        LOG.debug(String.format("Compare \"%s\" path against the \"%s\" pattern", path, pattern));
        if (path.matches(pattern)) {
            return true;
        }
        path = path.replaceAll("[\\\\]", "/");
        LOG.debug(String.format("Compare \"%s\" path against the \"%s\" pattern", path, pattern));
        if (path.matches(pattern)) {
            return true;
        }
        LOG.debug(String.format("Check if \"%s\" path contains the \"%s\" pattern", path, pattern));
        if (path.contains(pattern)) {
            return true;
        }
        return false;
    }

    private List<File> listFiles(File rootFolder, List<String> matchPatterns) {
        LOG.debug(String.format("Processing files in the \"%s\" folder", rootFolder.getAbsolutePath()));
        List<File> outList = new ArrayList<File>();
        for (File child : rootFolder.getAbsoluteFile().listFiles()) {
            LOG.debug(String.format("Found \"%s\" file", child.getAbsolutePath()));
            if (child.isDirectory()) {
                LOG.debug("This is a directory");
                outList.addAll(listFiles(child.getAbsoluteFile(), matchPatterns));
                LOG.debug("Directory processing is done");
            } else {
                for (String pattern : matchPatterns) {
                    if (compareFileName(child.getAbsolutePath(), pattern)) {
                        LOG.debug("It matches!!! Adding to list");
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
    public void open(AerialParams params) throws Exception {
        File root = new File(this.rootDirectory).getAbsoluteFile();
        List<String> matchPatterns = new ArrayList<String>();
        /*for (int i = 0; i < params.length; i++) {
            matchPatterns.add((String) params[i]);
            LOG.debug("Adding parameter: " + params[i]);
        }*/
        for (String param : params.getValueParams()) {
            matchPatterns.add(param);
            LOG.debug("Adding parameter: " + param);
        }
        this.files = listFiles(root, matchPatterns);
        this.iterator = this.files.iterator();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#close()
     */
    public void close() throws Exception {
        this.files.clear();
        this.files = new ArrayList<File>();
        this.iterator = this.files.iterator();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#readNext()
     */
    public String readNext() throws Exception {
        if (this.hasNext()) {
            return FileUtils.readFileToString(this.iterator.next());
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialReader#hasNext()
     */
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

}
