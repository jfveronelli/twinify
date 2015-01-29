package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public class DeleteResourceTask implements Task {

    public static final String HEADER = "DELETE ";

    private String path;

    public DeleteResourceTask(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void runOn(String fullPath) {
        Utils.wipe(new File(Path.concat(fullPath, path)));
    }

    @Override
    public void saveExtras(File directory) {}

    @Override
    public String toString() {
        return HEADER + path;
    }

}