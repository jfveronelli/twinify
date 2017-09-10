package ar.org.crossknight.twinify.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.delta.Task;
import ar.org.crossknight.twinify.util.Path;

public class TasksTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] COLUMN_NAMES = {"", "Path", "Extension"};

    private Delta delta;
    private List<Task> tasks = Collections.emptyList();

    public void setData(Delta delta) {
        this.delta = delta;
        this.tasks = delta != null? delta.getTasks(): Collections.<Task>emptyList();
        fireTableDataChanged();
    }

    public Delta getData() {
        return delta;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return col == 0? Task.class: String.class;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Task task = tasks.get(row);
        switch (col) {
        case 0:
            return task;
        case 1:
            return task.getPath();
        case 2:
            return getFileExtension(task);
        default:
            throw new IllegalArgumentException();
        }
    }

    private static String getFileExtension(Task task) {
        if (task.getType() == Task.Type.CREATE_FOLDER || task.getType() == Task.Type.DELETE_FOLDER) {
            return "";
        }
        return Path.extension(task.getPath());
    }

    public void removeRows(int[] rows) {
        Arrays.sort(rows);
        for (int c = rows.length - 1; c >= 0; c--) {
            int row = rows[c];
            delta.remove(tasks.remove(row));
            fireTableRowsDeleted(row, row);
        }
    }

}