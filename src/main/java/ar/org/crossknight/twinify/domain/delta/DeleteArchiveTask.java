package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

import ar.org.crossknight.twinify.util.Path;

public class DeleteArchiveTask extends Task {

    public static final String HEADER = "DELETE ";

    public DeleteArchiveTask(String path) {
        super(path);
    }

    @Override
    public Type getType() {
        return Type.DELETE_ARCHIVE;
    }

    @Override
    public void runOn(String fullPath) {
        File file = new File(Path.concat(fullPath, getPath()));
        if (file.exists()) {
            if (!file.isFile()) {
                throw new RuntimeException("Expected file at [" + file.getAbsolutePath() + "]");
            }
            if (!file.delete()) {
                throw new RuntimeException("Unable to delete file [" + file.getAbsolutePath() + "]");
            }
        }
    }

    @Override
    public String toString() {
        return HEADER + getPath();
    }

}