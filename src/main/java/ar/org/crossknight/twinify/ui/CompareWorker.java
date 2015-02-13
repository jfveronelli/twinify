package ar.org.crossknight.twinify.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;
import ar.org.crossknight.twinify.serialization.SnapshotSerializer;
import ar.org.crossknight.twinify.util.Utils;

public class CompareWorker extends Worker {

    private final File folder;

    public CompareWorker(AppFrame appFrame, File folder) {
        super(appFrame);
        this.folder = folder;
    }

    @Override
    protected String doInBackground() throws Exception {
        Snapshot donorSnapshot = Snapshot.getInstance(folder);
        setProgress(20);

        InputStream inStream = new FileInputStream(Snapshot.DEFAULT_SNAPSHOT_FILE);
        Snapshot twinSnapshot = SnapshotSerializer.read(inStream);
        inStream.close();
        setProgress(40);

        Delta delta = new Delta(donorSnapshot, twinSnapshot);
        setProgress(60);

        File deltaDirectory = new File(Delta.DEFAULT_DELTA_DIRECTORY);
        Utils.wipe(deltaDirectory);
        setProgress(80);

        DeltaSerializer.write(deltaDirectory, delta);
        setProgress(100);

        return "Comparison complete";
    }

}