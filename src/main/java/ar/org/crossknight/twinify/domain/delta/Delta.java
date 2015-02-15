package ar.org.crossknight.twinify.domain.delta;

import java.util.ArrayList;
import java.util.List;

import ar.org.crossknight.twinify.domain.snapshot.Archive;
import ar.org.crossknight.twinify.domain.snapshot.Folder;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.util.UidGenerator;
import ar.org.crossknight.twinify.util.Utils;

public class Delta {

    public static final String DEFAULT_DELTA_DIRECTORY = "delta";

    private String twinPath;
    private List<Task> deleteTasks = new ArrayList<Task>();
    private List<Task> updateTasks = new ArrayList<Task>();
    private List<Task> createTasks = new ArrayList<Task>();

    public Delta(String twinPath) {
        this.twinPath = twinPath;
    }

    public Delta(Snapshot donor, Snapshot twin) {
        this.twinPath = twin.getFullPath();
        UidGenerator uidGen = new UidGenerator();
        Folder root = donor.getRoot();
        compare(root, twin.getRoot(), uidGen);
    }

    private void compare(Folder folderA, Folder folderB, UidGenerator uidGen) {
        if (folderB == null) {
            add(new CreateFolderTask(folderA.getPath()));
            for (Archive archiveA: folderA.getArchives()) {
                add(new CreateArchiveTask(archiveA.getPath(), uidGen.next(), archiveA.getFullPath()));
            }
            for (Folder subfolderA: folderA.getSubfolders()) {
                compare(subfolderA, null, uidGen);
            }
            return;
        }

        for (Archive archiveA: folderA.getArchives()) {
            Archive archiveB = folderB.getArchive(archiveA.getName());
            if (archiveB == null) {
                add(new CreateArchiveTask(archiveA.getPath(), uidGen.next(), archiveA.getFullPath()));
            } else if (!archiveA.equals(archiveB)) {
                add(new UpdateArchiveTask(archiveA.getPath(), uidGen.next(), archiveA.getFullPath()));
            }
        }

        for (Archive archiveB: folderB.getArchives()) {
            if (folderA.getArchive(archiveB.getName()) == null) {
                add(new DeleteArchiveTask(archiveB.getPath()));
            }
        }

        for (Folder subfolderA: folderA.getSubfolders()) {
            compare(subfolderA, folderB.getSubfolder(subfolderA.getName()), uidGen);
        }

        for (Folder subfolderB: folderB.getSubfolders()) {
            if (folderA.getSubfolder(subfolderB.getName()) == null) {
                add(new DeleteFolderTask(subfolderB.getPath()));
            }
        }
    }

    public void setTwinPath(String twinPath) {
        this.twinPath = twinPath;
    }

    public String getTwinPath() {
        return twinPath;
    }

    public void add(DeleteFolderTask task) {
        deleteTasks.add(task);
    }

    public void add(DeleteArchiveTask task) {
        deleteTasks.add(task);
    }

    public void add(UpdateArchiveTask task) {
        updateTasks.add(task);
    }

    public void add(CreateFolderTask task) {
        createTasks.add(task);
    }

    public void add(CreateArchiveTask task) {
        createTasks.add(task);
    }

    public boolean remove(Task task) {
        if (task.getType() == Task.Type.CREATE_FOLDER || task.getType() == Task.Type.CREATE_ARCHIVE) {
            return createTasks.remove(task);
        }
        if (task.getType() == Task.Type.UPDATE_ARCHIVE) {
            return updateTasks.remove(task);
        }
        if (task.getType() == Task.Type.DELETE_FOLDER || task.getType() == Task.Type.DELETE_ARCHIVE) {
            return deleteTasks.remove(task);
        }
        return false;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<Task>(deleteTasks);
        tasks.addAll(updateTasks);
        tasks.addAll(createTasks);
        return tasks;
    }

    public boolean apply(List<Filter> filters) {
        boolean removed = Utils.apply(deleteTasks, filters);
        removed |= Utils.apply(updateTasks, filters);
        removed |= Utils.apply(createTasks, filters);
        return removed;
    }

    public void run() {
        for (Task task: getTasks()) {
            task.runOn(twinPath);
        }
    }

    @Override
    public String toString() {
        return "TWIN   " + twinPath;
    }

    @Override
    public Delta clone() {
        Delta delta = new Delta(twinPath);
        delta.deleteTasks.addAll(deleteTasks);
        delta.updateTasks.addAll(updateTasks);
        delta.createTasks.addAll(createTasks);
        return delta;
    }

}