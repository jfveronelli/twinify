package ar.org.crossknight.twinify.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ar.org.crossknight.twinify.console.AbstractCommand;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.serialization.SnapshotSerializer;

public class ScanWorker extends AbstractWorker {

    private final File folder;

    public ScanWorker(AppFrame appFrame, File folder) {
        super(appFrame);
        this.folder = folder;
    }

    @Override
    protected String doInBackground() throws Exception {
        Snapshot snapshot = Snapshot.getInstance(folder);
        setProgress(50);

        OutputStream stream = new FileOutputStream(AbstractCommand.SNAPSHOT_FILE);
        SnapshotSerializer.write(stream, snapshot);
        stream.close();
        setProgress(100);

        return "Scan complete";
    }

}