package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public class UpdateArchiveTask extends CreateArchiveTask {

    public static final String HEADER = "UPDATE ";

    public UpdateArchiveTask(String path, String uid, String contentPath) {
        super(path, uid, contentPath);
    }

    @Override
    public void runOn(String fullPath) {
        Utils.wipe(new File(Path.concat(fullPath, getPath())));
        super.runOn(fullPath);
    }

    @Override
    public String toString() {
        return HEADER + partialToString();
    }

}