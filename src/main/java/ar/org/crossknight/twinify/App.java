package ar.org.crossknight.twinify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.serialization.SnapshotSerializer;

public final class App {

	private App() {}

    public static final void main(String[] args) throws Exception {
        File file = new File("C:/todoviajes-env\\grails-2.1.0");
        Snapshot snapshot = Snapshot.getInstance(file);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SnapshotSerializer.write(out, snapshot);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        snapshot = SnapshotSerializer.read(in);

        SnapshotSerializer.write(System.out, snapshot);
    }

}