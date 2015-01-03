/**
 * .
 */
package com.github.mkolisnyk.aerial.readers;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.AerialReader;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialParams;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

/**
 * @author Myk Kolisnyk
 *
 */
public class AerialStringReaderTest {

    private AerialReader reader;
    private AerialParams params;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        params = new AerialParams();
        params.parse(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.STRING.toString(),
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.STRING.toString(),
                "Text String 1",
                "Text String 2",
                "Text String 3",
                "Text String 4"
        });
        reader = new AerialStringReader(params);
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
     * @throws Exception .
     */
    @Test
    public void testConstructorShouldCreateObjectWithEmptyContent() throws Exception {
        reader.close();
        params.parse(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(), AerialSourceType.STRING.toString(),
                AerialParamKeys.OUTPUT_TYPE.toString(), AerialSourceType.STRING.toString()
        });
        reader = new AerialStringReader(params);
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#open(java.lang.Object[])}.
     * @throws Exception .
     */
    @Test
    public void testOpenWithParametersShouldStoreAllStringsPassed() throws Exception {
        String[] expected = {
                "Text String 1",
                "Text String 2",
                "Text String 3",
                "Text String 4"
        };
        reader.open(params);
        for (String expectedItem : expected) {
            Assert.assertTrue(reader.hasNext());
            Assert.assertEquals(expectedItem, reader.readNext());
        }
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#open(java.lang.Object[])}.
     * @throws Exception .
     */
    @Test
    public void testOpenWithoutParametersShouldStoreNoValues() throws Exception {
        params.getValueParams().clear();
        reader.open(params);
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    /**
     * Test method for {@link com.github.mkolisnyk.aerial.readers.AerialStringReader#close()}.
     * @throws Exception .
     */
    @Test
    public void testCloseOpenedReaderRemovesAllValues() throws Exception {
        reader.open(params);
        reader.close();
        Assert.assertFalse(reader.hasNext());
        Assert.assertNull(reader.readNext());
    }

    @Test
    public void testCloseOfClosedReaderShouldntThrowErrors() throws Exception {
        params.getValueParams().clear();
        reader.open(params);
        reader.close();
        try {
            reader.close();
        } catch (Throwable e) {
            Assert.fail("Repetitive close unexpectly failed");
        }
    }
}
