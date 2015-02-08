package ar.org.crossknight.twinify.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;
import ar.org.crossknight.twinify.serialization.SnapshotSerializer;
import ar.org.crossknight.twinify.util.Utils;

public class CompareCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "compare";
    }

    @Override
    public void execute(String path) throws Exception {
        File donorDirectory = checkPath(path);
        Snapshot donorSnapshot = Snapshot.getInstance(donorDirectory);

        InputStream inStream = new FileInputStream(SNAPSHOT_FILE);
        Snapshot twinSnapshot = SnapshotSerializer.read(inStream);
        inStream.close();

        Delta delta = new Delta(donorSnapshot, twinSnapshot);
        File deltaDirectory = new File(DELTA_DIRECTORY);
        Utils.wipe(deltaDirectory);
        DeltaSerializer.write(deltaDirectory, delta);
    }

}