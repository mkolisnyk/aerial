/**
 * 
 */
package com.github.mkolisnyk.aerial.readers;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.AerialReader;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialStringReaderTest {

    private AerialReader reader;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        reader = new AerialStringReader();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        reader.close();
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#AerialStringReader()}.
     */
    @Test
    public void testConstructorShouldCreateObjectWithEmptyContent() {
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#open(java.lang.Object[])}.
     * @throws Exception 
     */
    @Test
    public void testOpenWithParametersShouldStoreAllStringsPassed() throws Exception {
        String expected[] = {
                "Text String 1",
                "Text String 2",
                "Text String 3",
                "Text String 4"
        };
        reader.open(expected[0],expected[1],expected[2],expected[3]);
        for (String expectedItem : expected) {
            Assert.assertTrue(reader.hasNext());
            Assert.assertEquals(expectedItem, reader.readNext());
        }
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#open(java.lang.Object[])}.
     * @throws Exception 
     */
    @Test
    public void testOpenWithoutParametersShouldStoreNoValues() throws Exception {
        reader.open();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#close()}.
     * @throws Exception 
     */
    @Test
    public void testCloseOpenedReaderRemovesAllValues() throws Exception {
        String expected[] = {
                "Text String 1",
                "Text String 2",
                "Text String 3",
                "Text String 4"
        };
        reader.open(expected[0],expected[1],expected[2],expected[3]);
        reader.close();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testCloseOfClosedReaderShouldntThrowErrors() throws Exception {
        reader.open();
        reader.close();
        try {
            reader.close();
        } catch (Throwable e) {
            Assert.fail("Repetitive close unexpectly failed");
        }
    }
}
