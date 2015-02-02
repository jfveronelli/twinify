package ar.org.crossknight.twinify.util;

import ar.org.crossknight.twinify.domain.snapshot.Folder;
import ar.org.crossknight.twinify.domain.snapshot.Resource;

public final class Path {

    private Path() {}

    public static final String name(String path) {
        path = uniformSeparators(path);
        if (path.equals(Resource.SEPARATOR)) {
            return Folder.ROOT_NAME;
        }
        path = stripTrailingSeparator(path);
        return path.substring(path.lastIndexOf(Resource.SEPARATOR) + 1);
    }

    public static final String extension(String path) {
        String name = name(path);
        int pos = name.indexOf('.');
        return pos >= 0? name.substring(pos + 1): "";
    }

    public static final String parent(String path) {
        path = uniformSeparators(path);
        if (path.equals(Resource.SEPARATOR)) {
            return null;
        }
        path = stripTrailingSeparator(path);
        return path.substring(0, path.lastIndexOf(Resource.SEPARATOR) + 1);
    }

    public static final String concat(String path1, String path2) {
        path2 = uniformSeparators(path2);
        if (!path2.startsWith(Resource.SEPARATOR)) {
            path2 = Resource.SEPARATOR + path2;
        }
        return stripTrailingSeparator(uniformSeparators(path1)) + path2;
    }

    public static final String[] route(String path) {
        path = uniformSeparators(path);
        if (path.equals(Resource.SEPARATOR)) {
            return new String[0];
        }
        return stripTrailingSeparator(stripLeadingSeparator(path)).split(Resource.SEPARATOR);
    }

    private static String uniformSeparators(String path) {
        return path.replace('\\', '/');
    }

    private static String stripLeadingSeparator(String path) {
        return path.startsWith(Resource.SEPARATOR)? path.substring(1): path;
    }

    private static String stripTrailingSeparator(String path) {
        return path.endsWith(Resource.SEPARATOR)? path.substring(0, path.length() - 1): path;
    }

}