package ar.org.crossknight.twinify.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.serialization.SnapshotSerializer;

public class ScanCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "scan";
    }

    @Override
    public void execute(String path) throws Exception {
        File file = checkPath(path);
        Snapshot snapshot = Snapshot.getInstance(file);
        OutputStream stream = new FileOutputStream(SNAPSHOT_FILE);
        SnapshotSerializer.write(stream, snapshot);
        stream.close();
    }

}