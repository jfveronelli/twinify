package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

import ar.org.crossknight.twinify.util.Path;

public class CreateFolderTask implements Task {

    public static final String HEADER = "CREATE ";

    private String path;

    public CreateFolderTask(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void runOn(String fullPath) {
        File file = new File(Path.concat(fullPath, path));
        if (!file.mkdir()) {
            throw new RuntimeException("Could not create folder [" + file.getAbsolutePath() + "]");
        }
    }

    @Override
    public void saveExtras(File directory) {}

    @Override
    public String toString() {
        return HEADER + path;
    }

}