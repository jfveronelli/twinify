package ar.org.crossknight.twinify.domain.snapshot;

import java.io.File;
import java.util.Date;

import ar.org.crossknight.twinify.util.Path;

public class Snapshot {

	private String fullPath;
	private Folder root;

	public Snapshot(String fullPath) {
		this.fullPath = fullPath;
		this.root = new Folder(this);
	}

    public static Snapshot getInstance(File directory) {
    	Snapshot snapshot = new Snapshot(directory.getAbsolutePath());
    	populate(snapshot.root, directory);
    	return snapshot;
    }

    private static void populate(Folder folder, File directory) {
		for (File file: directory.listFiles()) {
	    	if (file.isFile()) {
	    		new Archive(folder, file.getName(), new Date(file.lastModified()), file.length());
	    	} else if (file.isDirectory()) {
	    		populate(new Folder(folder, file.getName()), file);
	    	} else {
	    		throw new RuntimeException("Not a file or directory [" + file.getAbsolutePath() + "]");
	    	}
	    }
    }

    public String getFullPath() {
        return fullPath;
    }

    public Folder getRoot() {
    	return root;
    }

    public Folder findFolder(String path) {
        Folder folder = root;
        String[] route = Path.route(path);
        for (int c = 0; c < route.length && folder != null; c++) {
            folder = folder.getSubfolder(route[c]);
        }
        return folder;
    }

    @Override
    public String toString() {
    	return "[" + fullPath + "]";
    }

}