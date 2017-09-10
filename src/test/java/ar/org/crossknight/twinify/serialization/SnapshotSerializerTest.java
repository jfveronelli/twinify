package ar.org.crossknight.twinify.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.junit.Test;

import ar.org.crossknight.twinify.domain.snapshot.Archive;
import ar.org.crossknight.twinify.domain.snapshot.Folder;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import static org.junit.Assert.*;

public class SnapshotSerializerTest {

    @Test
    public void writeAndReadShouldBeInverseOperations() throws Exception {
        String path = "/a/r";
        Snapshot snapshot = new Snapshot(path);
        Folder f = new Folder(snapshot.getRoot(), "xyz");
        new Archive(f, "sample.txt", new Date(), 9L);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SnapshotSerializer.write(out, snapshot);
        byte[] buffer = out.toByteArray();
        Snapshot snapshotCopy = SnapshotSerializer.read(new ByteArrayInputStream(buffer));

        assertTrue(buffer.length > 0);
        assertEquals(snapshot.getFullPath(), snapshotCopy.getFullPath());
        assertEquals(snapshot.getRoot(), snapshotCopy.getRoot());
    }

}