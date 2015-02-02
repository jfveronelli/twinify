package ar.org.crossknight.twinify.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ar.org.crossknight.twinify.domain.snapshot.Folder;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import static org.junit.Assert.*;

public class UtilsTest {

    private static final File BASE_FOLDER = new File("src/test/samples/");

    @Test
    public void getShouldReturnNullWhenNameIsNotFound() {
        Folder root = new Snapshot("/").getRoot();
        Folder f1 = new Folder(root, "Albany");
        Folder f2 = new Folder(root, "addendum");

        Folder result = Utils.get(Arrays.asList(f1, f2), "norway");

        assertNull(result);
    }

    @Test
    public void getShouldSucceed() {
        Folder root = new Snapshot("/").getRoot();
        Folder f1 = new Folder(root, "Albany");
        Folder f2 = new Folder(root, "albany");

        Folder result = Utils.get(Arrays.asList(f1, f2), "albany");

        assertSame(f2, result);
    }

    @Test
    public void sortShouldSucceed() {
        Folder root = new Snapshot("/").getRoot();
        Folder f1 = new Folder(root, "Albany");
        Folder f2 = new Folder(root, "addendum");
        Folder f3 = new Folder(root, "aDd");
        Folder f4 = new Folder(root, "balcony");
        List<Folder> folders = Arrays.asList(f1, f2, f3, f4);

        List<Folder> sorted = Utils.sort(folders);

        assertNotSame(folders, sorted);
        assertArrayEquals(new Object[] {f3, f2, f1, f4}, sorted.toArray());
    }

    @Test
    public void stripMillisShouldReturnSameDateWhenDateHas0Milliseconds() {
        Date date = new Date(23000L);

        Date result = Utils.stripMillis(date);

        assertSame(date, result);
    }

    @Test
    public void stripMillisShouldReturnDateWith0Milliseconds() {
        Date date = new Date(23050L);

        Date result = Utils.stripMillis(date);

        assertEquals(23000L, result.getTime());
    }

    @Test
    public void wipeShouldDeleteSubfoldersAndReadOnlyFiles() throws IOException {
        File folder = new File(BASE_FOLDER, "aa");
        assertTrue(folder.mkdir());
        File subfolder = new File(folder, "bb");
        assertTrue(subfolder.mkdir());
        File file = new File(folder, "cc");
        assertTrue(file.createNewFile());
        assertTrue(file.setWritable(false, false));

        Utils.wipe(folder);

        assertFalse(folder.exists());
    }

    @Test
    public void copyShouldCreateWritableFileWithSameContentAndModificationTime() throws IOException {
        File from = new File(BASE_FOLDER, "copy.txt");
        assertTrue(from.setWritable(true, false));
        File to = new File(BASE_FOLDER, "copyA.txt");

        Utils.copy(from, to);

        assertEquals(from.length(), to.length());
        assertEquals(from.lastModified(), to.lastModified());
        assertTrue(to.canWrite());
        assertTrue(to.delete());
    }

    @Test
    public void copyShouldCreateReadOnlyFile() throws IOException {
        File from = new File(BASE_FOLDER, "copy.txt");
        assertTrue(from.setWritable(false, false));
        File to = new File(BASE_FOLDER, "copyA.txt");

        Utils.copy(from, to);

        assertFalse(to.canWrite());
        assertTrue(to.delete());
    }

}