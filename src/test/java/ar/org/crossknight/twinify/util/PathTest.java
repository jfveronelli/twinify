package ar.org.crossknight.twinify.util;

import org.junit.Test;

import ar.org.crossknight.twinify.domain.snapshot.Folder;

import static org.junit.Assert.*;

public class PathTest {

    @Test
    public void nameShouldSucceed() {
        assertEquals(Folder.ROOT_NAME, Path.name("/"));
        assertEquals("cc", Path.name("/aa/bb/cc/"));
        assertEquals("cc", Path.name("/aa/bb/cc"));
        assertEquals("cc", Path.name("aa/bb/cc/"));
        assertEquals("cc", Path.name("aa/bb/cc"));
        assertEquals("cc", Path.name("cc"));
        assertEquals(Folder.ROOT_NAME, Path.name("\\"));
        assertEquals("cc", Path.name("\\aa\\bb\\cc\\"));
        assertEquals("cc", Path.name("\\aa\\bb\\cc"));
        assertEquals("cc", Path.name("aa\\bb\\cc\\"));
        assertEquals("cc", Path.name("aa\\bb\\cc"));
        assertEquals("cc", Path.name("cc"));
        assertEquals("cc", Path.name("/aa\\bb/cc\\"));
    }

    @Test
    public void extensionShouldSucceed() {
        assertEquals("fg", Path.extension("/a/b.c/d.e.fg"));
        assertEquals("bc", Path.extension("/a/.bc"));
        assertEquals("bc", Path.extension(".bc"));
        assertEquals("", Path.extension("/a/b/"));
        assertEquals("", Path.extension("/a/b"));
        assertEquals("c", Path.extension("/a/b.c/"));
    }

    @Test
    public void parentShouldSucceed() {
        assertEquals(null, Path.parent("/"));
        assertEquals("/a/", Path.parent("/a/b/"));
        assertEquals("/a/", Path.parent("/a/b"));
        assertEquals("a/", Path.parent("a/b/"));
        assertEquals("a/", Path.parent("a/b"));
        assertEquals("/", Path.parent("/a/"));
        assertEquals("/", Path.parent("/a"));
        assertEquals("", Path.parent("a/"));
        assertEquals("", Path.parent("a"));
        assertEquals(null, Path.parent("\\"));
        assertEquals("/a/", Path.parent("\\a\\b\\"));
        assertEquals("/a/", Path.parent("\\a\\b"));
        assertEquals("a/", Path.parent("a\\b\\"));
        assertEquals("a/", Path.parent("a\\b"));
        assertEquals("/", Path.parent("\\a\\"));
        assertEquals("/", Path.parent("\\a"));
        assertEquals("", Path.parent("a\\"));
        assertEquals("", Path.parent("a"));
        assertEquals("/a/", Path.parent("/a\\b\\"));
    }

    @Test
    public void concatShouldSucceed() {
        assertEquals("/a/b/", Path.concat("/a/", "/b/"));
        assertEquals("/a/b", Path.concat("/a/", "/b"));
        assertEquals("/a/b/", Path.concat("/a/", "b/"));
        assertEquals("/a/b", Path.concat("/a/", "b"));
        assertEquals("/a/b/", Path.concat("/a", "/b/"));
        assertEquals("/a/b", Path.concat("/a", "/b"));
        assertEquals("/a/b/", Path.concat("/a", "b/"));
        assertEquals("/a/b", Path.concat("/a", "b"));
        assertEquals("a/b/", Path.concat("a/", "/b/"));
        assertEquals("a/b", Path.concat("a/", "/b"));
        assertEquals("a/b/", Path.concat("a/", "b/"));
        assertEquals("a/b", Path.concat("a/", "b"));
        assertEquals("a/b/", Path.concat("a", "/b/"));
        assertEquals("a/b", Path.concat("a", "/b"));
        assertEquals("a/b/", Path.concat("a", "b/"));
        assertEquals("a/b", Path.concat("a", "b"));
        assertEquals("/a/", Path.concat("/a/", "/"));
        assertEquals("/a/", Path.concat("/a", "/"));
        assertEquals("a/", Path.concat("a/", "/"));
        assertEquals("a/", Path.concat("a", "/"));
        assertEquals("/b/", Path.concat("/", "/b/"));
        assertEquals("/b", Path.concat("/", "/b"));
        assertEquals("/b/", Path.concat("/", "b/"));
        assertEquals("/b", Path.concat("/", "b"));
        assertEquals("/", Path.concat("/", "/"));
        assertEquals("/a/b/", Path.concat("\\a\\", "\\b\\"));
        assertEquals("/a/b", Path.concat("\\a\\", "\\b"));
        assertEquals("/a/b/", Path.concat("\\a\\", "b\\"));
        assertEquals("/a/b", Path.concat("\\a\\", "b"));
        assertEquals("/a/b/", Path.concat("\\a", "\\b\\"));
        assertEquals("/a/b", Path.concat("\\a", "\\b"));
        assertEquals("/a/b/", Path.concat("\\a", "b\\"));
        assertEquals("/a/b", Path.concat("\\a", "b"));
        assertEquals("a/b/", Path.concat("a\\", "\\b\\"));
        assertEquals("a/b", Path.concat("a\\", "\\b"));
        assertEquals("a/b/", Path.concat("a\\", "b\\"));
        assertEquals("a/b", Path.concat("a\\", "b"));
        assertEquals("a/b/", Path.concat("a", "\\b\\"));
        assertEquals("a/b", Path.concat("a", "\\b"));
        assertEquals("a/b/", Path.concat("a", "b\\"));
        assertEquals("a/b", Path.concat("a", "b"));
        assertEquals("/a/", Path.concat("\\a\\", "\\"));
        assertEquals("/a/", Path.concat("\\a", "\\"));
        assertEquals("a/", Path.concat("a\\", "\\"));
        assertEquals("a/", Path.concat("a", "\\"));
        assertEquals("/b/", Path.concat("\\", "\\b\\"));
        assertEquals("/b", Path.concat("\\", "\\b"));
        assertEquals("/b/", Path.concat("\\", "b\\"));
        assertEquals("/b", Path.concat("\\", "b"));
        assertEquals("/", Path.concat("\\", "\\"));
        assertEquals("/a/b/", Path.concat("\\a/", "/b\\"));
    }

    @Test
    public void routeShouldSucceed() {
        assertArrayEquals(new String[] {"a", "b"}, Path.route("/a/b/"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("/a/b"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("a/b/"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("a/b"));
        assertArrayEquals(new String[] {"a"}, Path.route("/a/"));
        assertArrayEquals(new String[] {"a"}, Path.route("/a"));
        assertArrayEquals(new String[] {"a"}, Path.route("a/"));
        assertArrayEquals(new String[] {"a"}, Path.route("a"));
        assertArrayEquals(new String[] {}, Path.route("/"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("\\a\\b\\"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("\\a\\b"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("a\\b\\"));
        assertArrayEquals(new String[] {"a", "b"}, Path.route("a\\b"));
        assertArrayEquals(new String[] {"a"}, Path.route("\\a\\"));
        assertArrayEquals(new String[] {"a"}, Path.route("\\a"));
        assertArrayEquals(new String[] {"a"}, Path.route("a\\"));
        assertArrayEquals(new String[] {"a"}, Path.route("a"));
        assertArrayEquals(new String[] {}, Path.route("\\"));
    }

}