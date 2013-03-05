/**
 * Copyright (C) 2010 Causata
 */
package com.hbasetmp.hadvisor.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testMessage() {
        assertEquals("'big'", Util.message("{}", "big"));
        assertEquals("'big''eye'", Util.message("{}{}", "big", "eye"));
        assertEquals("x'big''eye'", Util.message("x{}{}", "big", "eye"));
        assertEquals("x'big'y'eye'", Util.message("x{}y{}", "big", "eye"));
        assertEquals("x'big'y'eye'z", Util.message("x{}y{}z", "big", "eye"));
    }
    
    @Test
    public void testMessageWithUnequalNumbersOfSubstitutions() {
        // We should be tolerant of these cases, as, at runtime, it's better to have an incomplete
        // message than nothing at all
        assertEquals("'big'{}", Util.message("{}{}", "big"));
        assertEquals("'big'", Util.message("{}", "big", "eye"));
    }
    
    @Test
    public void testCsvList() {
        assertEquals("big", Util.csvList("big"));
        assertEquals("big,eye", Util.csvList("big", "eye"));
        assertEquals("big,eye,aye", Util.csvList("big", "eye", "aye"));
    }
    
}
