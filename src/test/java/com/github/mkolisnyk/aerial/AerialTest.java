package com.github.mkolisnyk.aerial;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AerialTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMain() {
        Aerial.main(null);
        Assert.assertTrue(true);
    }
}

