package ar.org.crossknight.twinify.domain.snapshot;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

public class SnapshotTest {

    private static final File BASE_FOLDER = new File("src/test/samples/a/");

    @Test
    public void getInstanceShouldSucceed() {
        File file = new File(BASE_FOLDER, "b/c/file.txt");
        long lastModified = file.lastModified() / 1000L * 1000L;
        long length = file.length();

        Snapshot snapshot = Snapshot.getInstance(BASE_FOLDER);

        Folder folder = snapshot.getRoot();
        assertTrue(folder.getArchives().isEmpty());
        assertEquals(1, folder.getSubfolders().size());
        folder = folder.getSubfolder("b");
        assertTrue(folder.getArchives().isEmpty());
        assertEquals(1, folder.getSubfolders().size());
        folder = folder.getSubfolder("c");
        assertTrue(folder.getSubfolders().isEmpty());
        assertEquals(1, folder.getArchives().size());
        Archive archive = folder.getArchive("file.txt");
        assertEquals(lastModified, archive.getLastModified().getTime());
        assertEquals(length, archive.getSize());
    }

}