package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public class DeleteFolderTask extends Task {

    public static final String HEADER = "REMOVE ";

    public DeleteFolderTask(String path) {
        super(path);
    }

    @Override
    public Type getType() {
        return Type.DELETE_FOLDER;
    }

    @Override
    public void runOn(String fullPath) {
        File folder = new File(Path.concat(fullPath, getPath()));
        if (folder.exists()) {
            if (!folder.isDirectory()) {
                throw new RuntimeException("Expected folder at [" + folder.getAbsolutePath() + "]");
            }

            File[] files = folder.listFiles();
            if (files != null) {
                for (File f: files) {
                    Utils.wipe(f);
                }
            }

            if (!folder.delete()) {
                throw new RuntimeException("Unable to delete folder [" + folder.getAbsolutePath() + "]");
            }
        }
    }

    @Override
    public String toString() {
        return HEADER + getPath();
    }

}