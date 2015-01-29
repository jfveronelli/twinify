package ar.org.crossknight.twinify.domain.delta;

import java.io.File;

public interface Task {

    int HEADER_SIZE = 7;

    String getPath();

    void runOn(String fullPath);

    void saveExtras(File directory);

}