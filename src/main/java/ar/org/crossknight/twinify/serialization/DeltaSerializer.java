package ar.org.crossknight.twinify.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import ar.org.crossknight.twinify.domain.delta.CreateArchiveTask;
import ar.org.crossknight.twinify.domain.delta.CreateFolderTask;
import ar.org.crossknight.twinify.domain.delta.DeleteResourceTask;
import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.delta.Task;
import ar.org.crossknight.twinify.domain.delta.UpdateArchiveTask;
import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.UidGenerator;

public final class DeltaSerializer {

    private static final String FILENAME = "@tasks.txt";
    private static final String EOL = "\n";

    private DeltaSerializer() {}

    public static final void write(File directory, Delta delta) throws IOException {
        if (!directory.exists()) {
            createDirectory(directory);
        } else {
            checkDirectory(directory);
        }
        File file = new File(directory, FILENAME);
        Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), "UTF-8");
        writer.write(delta + EOL);
        for (Task task: delta.getTasks()) {
            writer.write(task + EOL);
            task.saveExtras(directory);
        }
        writer.close();
    }

    public static final Delta read(File directory) throws IOException {
        checkDirectory(directory);
        File file = new File(directory, FILENAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), "UTF-8"));

        String twinPath = reader.readLine().substring(Task.HEADER_SIZE);
        Delta delta = new Delta(twinPath);

        String line = reader.readLine();
        while (line != null) {
            String header = line.substring(0, Task.HEADER_SIZE);
            String config = line.substring(Task.HEADER_SIZE);
            if (header.equals(DeleteResourceTask.HEADER)) {
                delta.add(new DeleteResourceTask(config));
            } else if (header.equals(UpdateArchiveTask.HEADER)) {
                String uid = config.substring(0, UidGenerator.LENGTH);
                String path = config.substring(UidGenerator.LENGTH + 1);
                String contentPath = Path.concat(directory.getAbsolutePath(), uid);
                delta.add(new UpdateArchiveTask(path, uid, contentPath));
            } else if (header.equals(CreateFolderTask.HEADER)) {
                delta.add(new CreateFolderTask(config));
            } else if (header.equals(CreateArchiveTask.HEADER)) {
                String uid = config.substring(0, UidGenerator.LENGTH);
                String path = config.substring(UidGenerator.LENGTH + 1);
                String contentPath = Path.concat(directory.getAbsolutePath(), uid);
                delta.add(new CreateArchiveTask(path, uid, contentPath));
            }
        }

        reader.close();
        return delta;
    }

    private static void checkDirectory(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IOException("Expected a directory at [" + directory.getAbsolutePath() + "]");
        }
    }

    private static void createDirectory(File directory) throws IOException {
        if (!directory.mkdirs()) {
            throw new IOException("Could not create directory [" + directory.getAbsolutePath() + "]");
        }
    }

}