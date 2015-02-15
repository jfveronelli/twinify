package ar.org.crossknight.twinify.domain.delta;

public abstract class Filter {

    public static final String INCLUDE_TAG = "+";
    public static final String EXCLUDE_TAG = "-";

    private boolean includes;

    public Filter(boolean includes) {
        this.includes = includes;
    }

    public final boolean includes() {
        return includes;
    }

    public abstract boolean matches(Task task);

}