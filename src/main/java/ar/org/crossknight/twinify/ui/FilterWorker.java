package ar.org.crossknight.twinify.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.delta.Filter;
import ar.org.crossknight.twinify.serialization.FilterSerializer;

public class FilterWorker extends Worker {

    private final File filters;
    private final Delta delta;
    private boolean deltaChanged;

    public FilterWorker(AppFrame appFrame, File filters, Delta delta) {
        super(appFrame);
        this.filters = filters;
        this.delta = delta;
    }

    @Override
    protected String doInBackground() throws Exception {
        List<Filter> filterList = null;
        try {
            InputStream inStream = new FileInputStream(filters);
            filterList = FilterSerializer.read(inStream);
            inStream.close();
        } catch (IOException ex) {
            return "File does not contain filters";
        }
        setProgress(50);

        deltaChanged = delta.apply(filterList);
        setProgress(100);

        return "Filtering complete";
    }

    @Override
    protected void done() {
        super.done();
        if (deltaChanged) {
            getAppFrame().updatePreview(delta);
        } else {
            getAppFrame().setPreviewButtonsEnabled(true);
        }
    }

}