package com.github.mkolisnyk.aerial;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.Aerial;

public class AerialTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMainForNullArgumentListShouldReturnNoErrors() throws Exception {
        Aerial.main(null);
    }
}

