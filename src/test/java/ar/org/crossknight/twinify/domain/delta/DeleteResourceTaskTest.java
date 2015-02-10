package ar.org.crossknight.twinify.domain.delta;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeleteResourceTaskTest {

    private static final String PATH = "/a/b";

    @Test
    public void getPathShouldReturnGivenPath() {
        assertEquals(PATH, new DeleteResourceTask(PATH).getPath());
    }

    @Test
    public void toStringShouldSerializeTheTask() {
        assertEquals("DELETE /a/b", new DeleteResourceTask(PATH).toString());
    }

}