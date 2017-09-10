package ar.org.crossknight.twinify.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.org.crossknight.twinify.domain.delta.CreateFolderTask;
import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.util.Utils;

public class DeltaSerializerTest {

    private static final File BASE_FOLDER = new File("src/test/samples/b");

    @Before
    @After
    public void clear() {
        Utils.wipe(BASE_FOLDER);
    }

    @Test
    public void writeAndReadShouldBeInverseOperations() throws Exception {
        String path = "C:\\Sample";
        String folderName = "HiThere";
        Delta delta = new Delta(path);
        delta.add(new CreateFolderTask(folderName));

        DeltaSerializer.write(BASE_FOLDER, delta);
        Delta deltaCopy = DeltaSerializer.read(BASE_FOLDER);

        assertEquals(path, deltaCopy.getTwinPath());
        assertEquals(1, deltaCopy.getTasks().size());
        assertTrue(deltaCopy.getTasks().get(0) instanceof CreateFolderTask);
        assertEquals(folderName, deltaCopy.getTasks().get(0).getPath());
    }

}