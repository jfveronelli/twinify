package ar.org.crossknight.twinify.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ar.org.crossknight.twinify.domain.snapshot.Resource;

public final class Utils {

	private static final Comparator<Resource> RESOURCE_NAME_COMPARATOR = new Comparator<Resource>() {
		public int compare(Resource r1, Resource r2) {
			return r1.getName().compareToIgnoreCase(r2.getName());
		}
	};

	private Utils() {}

    public static final <R extends Resource> R get(Collection<R> resources, String name) {
        for (R r: resources) {
            if (r.getName().equals(name)) {
                return r;
            }
        }
        return null;
    }

    public static final <R extends Resource> boolean equals(Collection<R> resources1, Collection<R> resources2) {
        if (resources1.size() != resources2.size()) {
            return false;
        }
        for (R r1: resources1) {
            R r2 = get(resources2, r1.getName());
            if (r2 == null || !r1.equals(r2)) {
                return false;
            }
        }
        return true;
    }

	public static final <R extends Resource> List<R> sort(Collection<R> resources) {
		List<R> list = new ArrayList<R>(resources);
		Collections.sort(list, RESOURCE_NAME_COMPARATOR);
		return list;
	}

    public static final Date stripMillis(Date date) {
        if (date.getTime() % 1000L > 0L) {
            return new Date(date.getTime() / 1000L * 1000L);
        }
        return date;
    }

    public static final void wipe(File file) {
        if (file.isDirectory()) {
            for (File f: file.listFiles()) {
                wipe(f);
            }
        }
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Unable to delete [" + file.getAbsolutePath() + "]");
        }
    }

    public static final void copy(File fileA, File fileB) throws IOException {
        byte[] buffer = new byte[1024 * 1024];
        InputStream in = new BufferedInputStream(new FileInputStream(fileA));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(fileB));
        int count = in.read(buffer);
        while (count != -1) {
            out.write(buffer, 0, count);
            count = in.read(buffer);
        }
        out.close();
        in.close();
        if (!fileB.setLastModified(fileA.lastModified())) {
            throw new IOException("Could not set modification time to file [" + fileB.getAbsolutePath() + "]");
        }
        if (!fileA.canWrite()) {
            if (!fileB.setWritable(false, false)) {
                throw new IOException("Could not mark as read-only the file [" + fileB.getAbsolutePath() + "]");
            }
        }
    }

}