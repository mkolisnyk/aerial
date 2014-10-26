/**
 * 
 */
package com.github.mkolisnyk.aerial.writers;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.AerialWriter;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialStringWriterTest {

    private AerialWriter writer;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.writer = new AerialStringWriter();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.writer.close();
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.writers.AerialStringWriter#writeNext()}.
     * @throws Exception .
     */
    @Test
    public void testWriteNextShouldHandleNullIterator() throws Exception {
        Assert.assertNull(this.writer.writeNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.writers.AerialStringWriter#hasNext()}.
     */
    @Test
    public void testHasNextShouldReturnNullOnNullIterator() {
        Assert.assertFalse(this.writer.hasNext());
    }

}
