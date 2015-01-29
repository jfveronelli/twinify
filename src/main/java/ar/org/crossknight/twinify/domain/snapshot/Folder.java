package ar.org.crossknight.twinify.domain.snapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Folder implements Resource {

    public static final String ROOT_NAME = "[ROOT]";

    private Snapshot snapshot;
	private Folder parent;
	private String name;
	private Collection<Folder> subfolders = new ArrayList<Folder>();
	private Collection<Archive> archives = new ArrayList<Archive>();

	public Folder(Snapshot snapshot) {
	    this.snapshot = snapshot;
		this.name = ROOT_NAME;
	}

    public Folder(Folder parent, String name) {
        this.snapshot = parent.snapshot;
    	this.parent = parent;
        this.name = name;
        parent.add(this);
    }

    Snapshot getSnapshot() {
        return snapshot;
    }

    @Override
    public String getName() {
    	return name;
    }

    public void add(Folder folder) {
    	subfolders.add(folder);
    }

    public Collection<Folder> getSubfolders() {
    	return Collections.unmodifiableCollection(subfolders);
    }

    public Folder getSubfolder(String name) {
        return getResource(subfolders, name);
    }

    public void add(Archive archive) {
    	archives.add(archive);
    }

    public Collection<Archive> getArchives() {
    	return Collections.unmodifiableCollection(archives);
    }

    public Archive getArchive(String name) {
        return getResource(archives, name);
    }

    @Override
    public String getPath() {
    	return parent != null? parent.getPath() + name + SEPARATOR: SEPARATOR;
    }

    @Override
    public String toString() {
    	return getPath();
    }

    private static <R extends Resource> R getResource(Collection<R> resources, String name) {
        for (R r: resources) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }

}