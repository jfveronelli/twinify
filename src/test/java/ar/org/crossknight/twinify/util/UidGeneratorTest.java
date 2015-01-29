package ar.org.crossknight.twinify.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class UidGeneratorTest {

    @Test
    public void nextShouldSucceed() {
        UidGenerator uidGen = new UidGenerator();

        assertEquals("000000", uidGen.next());
        assertEquals("000001", uidGen.next());
        assertEquals("000002", uidGen.next());
        for (int c = 1; c <= 7; c++) uidGen.next();
        assertEquals("00000a", uidGen.next());
        assertEquals("00000b", uidGen.next());
        for (int c = 1; c <= 23; c++) uidGen.next();
        assertEquals("00000z", uidGen.next());
        assertEquals("000010", uidGen.next());
    }

}