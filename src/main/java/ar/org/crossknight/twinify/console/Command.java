package ar.org.crossknight.twinify.console;

import java.io.File;

public abstract class Command {

    protected File checkPath(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            App.write("The path [" + path + "] is not a directory!");
            throw new TerminationException();
        }
        return file;
    }

    public abstract String getName();

    public abstract void execute(String path) throws Exception;

}