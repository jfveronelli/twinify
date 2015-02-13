package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

public abstract class Task {

    public static final int HEADER_SIZE = 7;

    public static enum Type {CREATE_FOLDER, DELETE_FOLDER, CREATE_ARCHIVE, UPDATE_ARCHIVE, DELETE_ARCHIVE}

    private String path;

    Task(String path) {
        this.path = path;
    }

    public abstract Type getType();

    public String getPath() {
        return path;
    }

    public abstract void runOn(String fullPath);

    public void saveExtras(File directory) {}

}