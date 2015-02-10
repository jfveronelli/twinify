package ar.org.crossknight.twinify.domain.delta;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UpdateArchiveTaskTest {

    @Test
    public void toStringShouldSerializeTheTask() {
        assertEquals("UPDATE 000002 /h/x", new UpdateArchiveTask("/h/x", "000002", "/c:/aab").toString());
    }

}