package ar.org.crossknight.twinify.ui;

import java.io.File;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;
import ar.org.crossknight.twinify.util.Utils;

public class ExtractWorker extends Worker {

    private final Delta delta;

    public ExtractWorker(AppFrame appFrame, Delta delta) {
        super(appFrame);
        this.delta = delta;
    }

    @Override
    protected String doInBackground() throws Exception {
        File deltaDirectory = new File(Delta.DEFAULT_DELTA_DIRECTORY);
        Utils.wipe(deltaDirectory);
        Thread.sleep(2000L); // Avoid exception later on when re-creating directory
        setProgress(50);

        DeltaSerializer.write(deltaDirectory, delta);
        setProgress(100);

        return "Extraction complete";
    }

    @Override
    protected void done() {
        super.done();
        getAppFrame().setPreviewButtonsEnabled(true);
    }

}