package ar.org.crossknight.twinify.ui;

import java.util.List;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.delta.Task;

public class CloneExecuteWorker extends Worker {

    private Delta delta;

    public CloneExecuteWorker(AppFrame appFrame, Delta delta) {
        super(appFrame);
        this.delta = delta;
    }

    @Override
    protected String doInBackground() throws Exception {
        String twinPath = delta.getTwinPath();
        List<Task> tasks = delta.getTasks();

        int total = tasks.size();
        for (int c = 0; c < total; c++) {
            tasks.get(c).runOn(twinPath);
            setProgress((c + 1) * 100 / total);
        }
        setProgress(100);

        return "Cloning complete";
    }

    @Override
    protected void done() {
        super.done();
        getAppFrame().setPreviewButtonsEnabled(true);
    }

}