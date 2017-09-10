package ar.org.crossknight.twinify.ui;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

public abstract class Worker extends SwingWorker<String, Object> {

    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    private final AppFrame appFrame;

    public Worker(AppFrame appFrame) {
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
            LOGGER.log(Level.SEVERE, "Worker execution failed. Cause: ", ex);
            appFrame.workerCompleted("FAILED: " + ex.getCause().getMessage());
        }
    }

}