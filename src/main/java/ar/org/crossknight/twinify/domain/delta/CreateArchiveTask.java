package ar.org.crossknight.twinify.domain.delta;

import java.io.File;
import java.io.IOException;

import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public class CreateArchiveTask extends Task {

    public static final String HEADER = "COPY   ";

    private String uid;
    private String contentPath;

    public CreateArchiveTask(String path, String uid, String contentPath) {
        super(path);
        this.uid = uid;
        this.contentPath = contentPath;
    }

    @Override
    public Type getType() {
        return Type.CREATE_ARCHIVE;
    }

    @Override
    public void runOn(String fullPath) {
        File fileB = new File(Path.concat(fullPath, getPath()));
        if (fileB.isFile()) {
            Utils.wipe(fileB);
        }

        try {
            Utils.copy(new File(contentPath), fileB);
        } catch (IOException ex) {
            throw new RuntimeException("Could not copy file", ex);
        }
    }

    @Override
    public void saveExtras(File directory) {
        try {
            Utils.copy(new File(contentPath), new File(directory, uid));
        } catch (IOException ex) {
            throw new RuntimeException("Could create file backup", ex);
        }
    }

    String partialToString() {
        return uid + " " + getPath();
    }

    @Override
    public String toString() {
        return HEADER + partialToString();
    }

}