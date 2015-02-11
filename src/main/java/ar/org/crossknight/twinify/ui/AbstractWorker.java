package ar.org.crossknight.twinify.ui;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public abstract class AbstractWorker extends SwingWorker<String, Object> {

    private final AppFrame appFrame;

    public AbstractWorker(AppFrame appFrame) {
        this.appFrame = appFrame;
    }

    protected AppFrame getAppFrame() {
        return appFrame;
    }

    @Override
    protected void done() {
        try {
            appFrame.workerCompleted(get());
        } catch (InterruptedException ex) {
            appFrame.workerCompleted("Cancelled");
        } catch (ExecutionException ex) {
            appFrame.workerCompleted("FAILED: " + ex.getCause().getMessage());
        }
    }

}