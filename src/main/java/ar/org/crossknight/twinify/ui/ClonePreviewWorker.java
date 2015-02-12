package ar.org.crossknight.twinify.ui;

import java.io.File;

import ar.org.crossknight.twinify.console.AbstractCommand;
import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;

public class ClonePreviewWorker extends AbstractWorker {

    private Delta delta;

    public ClonePreviewWorker(AppFrame appFrame) {
        super(appFrame);
    }

    @Override
    protected String doInBackground() throws Exception {
        File deltaDirectory = new File(AbstractCommand.DELTA_DIRECTORY);
        delta = DeltaSerializer.read(deltaDirectory);
        setProgress(100);

        return "Please review changes before executing tasks";
    }

    @Override
    protected void done() {
        super.done();
        if (delta != null) {
            getAppFrame().updatePreview(delta);
        }
    }

}