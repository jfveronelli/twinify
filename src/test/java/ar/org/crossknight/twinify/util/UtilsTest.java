package ar.org.crossknight.twinify.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ar.org.crossknight.twinify.domain.delta.CreateFolderTask;
import ar.org.crossknight.twinify.domain.delta.ExtensionFilter;
import ar.org.crossknight.twinify.domain.delta.Filter;
import ar.org.crossknight.twinify.domain.delta.PathFilter;
import ar.org.crossknight.twinify.domain.delta.Task;
import ar.org.crossknight.twinify.domain.snapshot.Folder;
import ar.org.crossknight.twinify.domain.snapshot.Resource;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void equalsShouldReturnTrueWhenCollectionsAreEmpty() {
        boolean result = Utils.equals(Collections.<Resource>emptyList(), Collections.<Resource>emptyList());

        assertTrue(result);
    }

    @Test
    public void equalsShouldReturnFalseWhenSizesAreDifferent() {
        Collection<Resource> r1 = Arrays.asList(mock(Resource.class));
        Collection<Resource> r2 = Arrays.asList(mock(Resource.class), mock(Resource.class));

        boolean result = Utils.equals(r1, r2);

        assertFalse(result);
    }

    @Test
    public void equalsShouldReturnFalseWhenAResourceNameIsNotInTheSecondCollection() {
        Resource r1 = mock(Resource.class);
        when(r1.getName()).thenReturn("a");
        Resource r2 = mock(Resource.class);
        when(r2.getName()).thenReturn("b");
        Collection<Resource> c1 = Arrays.asList(r1);
        Collection<Resource> c2 = Arrays.asList(r2);

        boolean result = Utils.equals(c1, c2);

        assertFalse(result);
    }

    @Test
    public void equalsShouldReturnFalseWhenResourcesWithSameNameAreNotEqual() {
        Resource r1 = mock(Resource.class);
        when(r1.getName()).thenReturn("a");
        Resource r2 = mock(Resource.class);
        when(r2.getName()).thenReturn("a");
        Collection<Resource> c1 = Arrays.asList(r1);
        Collection<Resource> c2 = Arrays.asList(r2);

        boolean result = Utils.equals(c1, c2);

        assertFalse(result);
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
    public void applyShouldReturnFalseWhenTasksIsEmpty() {
        List<Task> tasks = Collections.emptyList();
        List<Filter> filters = Arrays.<Filter>asList(new ExtensionFilter(false, "jpg"));

        boolean result = Utils.apply(tasks, filters);

        assertFalse(result);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void applyShouldReturnFalseWhenFiltersIsEmpty() {
        Task[] allTasks = {new CreateFolderTask("/a/u")};
        List<Task> tasks = Arrays.asList(allTasks);
        List<Filter> filters = Collections.emptyList();

        boolean result = Utils.apply(tasks, filters);

        assertFalse(result);
        assertArrayEquals(allTasks, tasks.toArray());
    }

    @Test
    public void applyShouldSucceed() {
        Task[] allTasks = {new CreateFolderTask("/a/u"), new CreateFolderTask("/a/v"), new CreateFolderTask("/a/w")};
        List<Task> tasks = new ArrayList<Task>(Arrays.asList(allTasks));
        List<Filter> filters = Arrays.<Filter>asList(new PathFilter(true, "/b/"), new PathFilter(true, "/a/u"), new PathFilter(false, "/a/v"));

        boolean result = Utils.apply(tasks, filters);

        assertTrue(result);
        assertEquals(2, tasks.size());
        assertEquals(allTasks[0], tasks.get(0));
        assertEquals(allTasks[2], tasks.get(1));
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