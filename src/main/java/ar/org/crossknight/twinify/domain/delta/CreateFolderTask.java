package ar.org.crossknight.twinify.domain.delta;

import java.io.File;
import java.io.IOException;

import ar.org.crossknight.twinify.util.Path;

public class CreateFolderTask extends Task {

    public static final String HEADER = "CREATE ";

    public CreateFolderTask(String path) {
        super(path);
    }

    @Override
    public Type getType() {
        return Type.CREATE_FOLDER;
    }

    @Override
    public void runOn(String fullPath) {
        String folderPath = Path.concat(fullPath, getPath());
        File file = new File(folderPath);
        if (file.isDirectory()) {
            // Windows is case-insensitive, so we must check the real directory name to assume it already exists
            try {
                String expectedName = Path.name(folderPath);
                String realName = Path.name(file.getCanonicalPath());
                if (!expectedName.equals(realName)) {
                    throw new RuntimeException("Folder [" + file.getAbsolutePath() + "] exists with a different case");
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error checking folder [" + file.getAbsolutePath() + "]");
            }
        } else if (!file.mkdir()) {
            throw new RuntimeException("Could not create folder [" + file.getAbsolutePath() + "]");
        }
    }

    @Override
    public String toString() {
        return HEADER + getPath();
    }

}