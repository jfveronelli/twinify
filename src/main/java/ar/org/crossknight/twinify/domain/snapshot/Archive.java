package ar.org.crossknight.twinify.domain.snapshot;

import java.util.Date;

import ar.org.crossknight.twinify.util.Formatter;
import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public class Archive implements Resource {

    // Required because filesystems have different time granularity. Eg: 2s for FAT.
    private static final long MAX_ALLOWED_MILLIS_DIFFERENCE = 2000L;
	public static final String FIELD_SEPARATOR = " | ";

	private Folder parent;
	private String name;
	private Date lastModified;
	private long size;

	public Archive(Folder parent, String name, Date lastModified, long size) {
		this.parent = parent;
		this.name = name;
		this.lastModified = Utils.stripMillis(lastModified);
		this.size = size;
		parent.add(this);
	}

	@Override
	public String getName() {
		return name;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public long getSize() {
		return size;
	}

	@Override
    public String getPath() {
    	return parent.getPath() + name;
    }

    public String getFullPath() {
        return Path.concat(parent.getSnapshot().getFullPath(), getPath());
    }

    @Override
    public String toString() {
    	return name + FIELD_SEPARATOR + Formatter.format(lastModified) + FIELD_SEPARATOR + size;
    }

    @Override
    public boolean equals(Object o) {
    	if (o instanceof Archive) {
    		Archive a = (Archive)o;
    		return name.equals(a.getName()) && size == a.getSize() &&
    		        Math.abs(lastModified.getTime() - a.getLastModified().getTime()) <= MAX_ALLOWED_MILLIS_DIFFERENCE;
    	}
    	return false;
    }

}