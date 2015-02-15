package ar.org.crossknight.twinify.domain.delta;

public class PathFilter extends Filter {

    public static final String HEADER = "PATH ";

    private final String path;

    public PathFilter(boolean includes, String path) {
        super(includes);
        this.path = path;
    }

    @Override
    public boolean matches(Task task) {
        return task.getPath().startsWith(path);
    }

    @Override
    public String toString() {
        return (includes()? INCLUDE_TAG: EXCLUDE_TAG) + HEADER + path;
    }

}