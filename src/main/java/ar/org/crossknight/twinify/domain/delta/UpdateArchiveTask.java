package ar.org.crossknight.twinify.domain.delta;

public class UpdateArchiveTask extends CreateArchiveTask {

    public static final String HEADER = "UPDATE ";

    public UpdateArchiveTask(String path, String uid, String contentPath) {
        super(path, uid, contentPath);
    }

    @Override
    public String toString() {
        return HEADER + partialToString();
    }

}