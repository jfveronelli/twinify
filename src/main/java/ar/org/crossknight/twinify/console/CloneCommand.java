package ar.org.crossknight.twinify.console;

import java.io.File;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.serialization.DeltaSerializer;

public class CloneCommand extends Command {

    @Override
    public String getName() {
        return "clone";
    }

    @Override
    public void execute(String path) throws Exception {
        File deltaDirectory = new File(Delta.DEFAULT_DELTA_DIRECTORY);
        Delta delta = DeltaSerializer.read(deltaDirectory);
        delta.setTwinPath(path);
        delta.run();
    }

}