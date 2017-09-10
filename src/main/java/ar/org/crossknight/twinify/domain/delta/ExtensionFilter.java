package ar.org.crossknight.twinify.domain.delta;

import ar.org.crossknight.twinify.util.Path;

public class ExtensionFilter extends Filter {

    public static final String HEADER = "EXTENSION ";

    private final String extension;

    public ExtensionFilter(boolean includes, String extension) {
        super(includes);
        this.extension = extension;
    }

    @Override
    public boolean matches(Task task) {
        return Path.extension(task.getPath()).equals(extension);
    }

    @Override
    public String toString() {
        return (includes()? INCLUDE_TAG: EXCLUDE_TAG) + HEADER + extension;
    }

}