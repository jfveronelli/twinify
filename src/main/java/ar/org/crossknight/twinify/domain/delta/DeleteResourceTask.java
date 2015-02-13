package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public class DeleteResourceTask extends Task {

    public static final String HEADER = "DELETE ";

    public DeleteResourceTask(String path) {
        super(path);
    }

    @Override
    public Type getType() {
        return Type.DELETE_RESOURCE;
    }

    @Override
    public void runOn(String fullPath) {
        Utils.wipe(new File(Path.concat(fullPath, getPath())));
    }

    @Override
    public String toString() {
        return HEADER + getPath();
    }

}