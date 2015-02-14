package ar.org.crossknight.twinify.ui;

import java.io.File;
import java.io.IOException;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;

public class ClonePreviewWorker extends Worker {

    private Delta delta;

    public ClonePreviewWorker(AppFrame appFrame) {
        super(appFrame);
    }

    @Override
    protected String doInBackground() throws Exception {
        File deltaDirectory = new File(Delta.DEFAULT_DELTA_DIRECTORY);
        try {
            delta = DeltaSerializer.read(deltaDirectory);
        } catch (IOException ex) {
            return "No saved changes were found";
        }
        setProgress(100);

        if (delta.getTasks().isEmpty()) {
            delta = null;
            return "Donor and target are equals twins";
        }
        return "Please review changes before executing cloning";
    }

    @Override
    protected void done() {
        super.done();
        if (delta != null) {
            getAppFrame().updatePreview(delta);
        }
    }

}