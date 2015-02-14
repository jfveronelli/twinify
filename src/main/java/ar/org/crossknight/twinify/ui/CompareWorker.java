package ar.org.crossknight.twinify.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;
import ar.org.crossknight.twinify.serialization.SnapshotSerializer;
import ar.org.crossknight.twinify.util.Utils;

public class CompareWorker extends Worker {

    private final File folder;
    private Delta delta;

    public CompareWorker(AppFrame appFrame, File folder) {
        super(appFrame);
        this.folder = folder;
    }

    @Override
    protected String doInBackground() throws Exception {
        Snapshot twinSnapshot = null;
        try {
            InputStream inStream = new FileInputStream(Snapshot.DEFAULT_SNAPSHOT_FILE);
            twinSnapshot = SnapshotSerializer.read(inStream);
            inStream.close();
        } catch (IOException ex) {
            return "No saved scan was found";
        }
        setProgress(20);

        File deltaDirectory = new File(Delta.DEFAULT_DELTA_DIRECTORY);
        Utils.wipe(deltaDirectory);
        setProgress(40);

        Snapshot donorSnapshot = Snapshot.getInstance(folder);
        setProgress(60);

        Delta delta = new Delta(donorSnapshot, twinSnapshot);
        setProgress(80);

        if (delta.getTasks().isEmpty()) {
            setProgress(100);
            return "Comparison complete: donor and target are equals twins";
        } else {
            DeltaSerializer.write(deltaDirectory, delta);
            this.delta = delta;
            setProgress(100);
            return "Comparison complete";
        }
    }

    @Override
    protected void done() {
        super.done();
        if (delta != null) {
            getAppFrame().updatePreview(delta);
        }
    }

}