package ar.org.crossknight.twinify.ui;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Toolkit;

import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

import javax.swing.ListSelectionModel;

import ar.org.crossknight.twinify.domain.delta.Delta;
import ar.org.crossknight.twinify.domain.delta.Task;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AppFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private boolean processing;
    private final TasksTableModel tasksTableModel;
    private final JButton btnScan;
    private final JButton btnCompare;
    private final JButton btnClone;
    private final JButton btnFilter;
    private final JButton btnPlay;
    private final JTable tblTasks;
    private final JProgressBar progressBar;
    private final JLabel statusBar;
    private final ProgressListener progressListener;

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }

    private static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            int result = t1.getType().ordinal() - t2.getType().ordinal();
            if (result == 0) {
                result = t1.getPath().compareToIgnoreCase(t2.getPath());
            }
            return result;
        }
    }

    private static class TaskTypeTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final ImageIcon[] IMAGES = {
            new ImageIcon(AppFrame.class.getResource("/images/task-folder-create-16x16.png")),
            new ImageIcon(AppFrame.class.getResource("/images/task-archive-create-16x16.png")),
            new ImageIcon(AppFrame.class.getResource("/images/task-archive-update-16x16.png")),
            new ImageIcon(AppFrame.class.getResource("/images/task-folder-delete-16x16.png")),
            new ImageIcon(AppFrame.class.getResource("/images/task-archive-delete-16x16.png"))
        };
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setIcon(IMAGES[((Task)value).getType().ordinal()]);
            label.setText(null);
            return label;
        }
    }

    private class AppWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (!processing) {
                setVisible(false);
                dispose();
            }
        }
    }

    private class ScanActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            JFileChooser fileChooser = createFolderChooser("Choose target folder");
            if (fileChooser.showOpenDialog(AppFrame.this) == JFileChooser.APPROVE_OPTION) {
                updatePreview(null);
                progressBar.setIndeterminate(true);
                runWorker("Scanning...", new ScanWorker(AppFrame.this, fileChooser.getSelectedFile()));
            }
        }
    }

    private class CompareActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            JFileChooser fileChooser = createFolderChooser("Choose donor folder");
            if (fileChooser.showOpenDialog(AppFrame.this) == JFileChooser.APPROVE_OPTION) {
                updatePreview(null);
                progressBar.setIndeterminate(true);
                runWorker("Comparing...", new CompareWorker(AppFrame.this, fileChooser.getSelectedFile()));
            }
        }
    }

    private class CloneActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            updatePreview(null);
            progressBar.setIndeterminate(true);
            runWorker("Previewing cloning...", new ClonePreviewWorker(AppFrame.this));
        }
    }

    private class FilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setDialogTitle("Choose filters file");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Filters file (*.filters)", "filters"));
            if (fileChooser.showOpenDialog(AppFrame.this) == JFileChooser.APPROVE_OPTION) {
                setPreviewButtonsEnabled(false);
                runWorker("Applying filters...", new FilterWorker(AppFrame.this,
                        fileChooser.getSelectedFile(), tasksTableModel.getData().clone()));
            }
        }
    }

    private class PlayActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            setPreviewButtonsEnabled(false);
            runWorker("Cloning...", new CloneExecuteWorker(AppFrame.this, tasksTableModel.getData()));
        }
    }

    private class TasksTableListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent evt) {
            if (!processing && evt.getKeyCode() == KeyEvent.VK_DELETE) {
                int[] selected = tblTasks.getSelectedRows();
                if (selected.length > 0) {
                    for (int c = 0; c < selected.length; c++) {
                        selected[c] = tblTasks.convertRowIndexToModel(selected[c]);
                    }
                    tasksTableModel.removeRows(selected);
                }
            }
        }
    }

    private class ProgressListener implements PropertyChangeListener {
        @Override
        public  void propertyChange(PropertyChangeEvent evt) {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer)evt.getNewValue());
            }
        }
    }

    public AppFrame() {
        progressListener = new ProgressListener();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new AppWindowAdapter());
        setIconImage(Toolkit.getDefaultToolkit().getImage(AppFrame.class.getResource("/images/clone-16x16.png")));
        setTitle("Twinify");
        setBounds(100, 100, 400, 300);
        GridBagLayout gridBagLayout = new GridBagLayout();
        getContentPane().setLayout(gridBagLayout);

        JToolBar toolBar = new JToolBar();
        toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        toolBar.setFloatable(false);

        btnScan = new JButton();
        btnScan.addActionListener(new ScanActionListener());
        btnScan.setToolTipText("Scan target");
        btnScan.setIcon(new ImageIcon(AppFrame.class.getResource("/images/scan-16x16.png")));
        toolBar.add(btnScan);

        btnCompare = new JButton();
        btnCompare.addActionListener(new CompareActionListener());
        btnCompare.setToolTipText("Compare donor with scanned target and extract changes");
        btnCompare.setIcon(new ImageIcon(AppFrame.class.getResource("/images/compare-16x16.png")));
        toolBar.add(btnCompare);

        btnClone = new JButton();
        btnClone.addActionListener(new CloneActionListener());
        btnClone.setToolTipText("Preview cloning donor over target");
        btnClone.setIcon(new ImageIcon(AppFrame.class.getResource("/images/clone-16x16.png")));
        toolBar.add(btnClone);

        toolBar.addSeparator();

        btnFilter = new JButton((String)null);
        btnFilter.addActionListener(new FilterActionListener());
        btnFilter.setEnabled(false);
        btnFilter.setToolTipText("Apply filters");
        btnFilter.setIcon(new ImageIcon(AppFrame.class.getResource("/images/filter-16x16.png")));
        toolBar.add(btnFilter);

        btnPlay = new JButton((String)null);
        btnPlay.addActionListener(new PlayActionListener());
        btnPlay.setEnabled(false);
        btnPlay.setToolTipText("Execute cloning");
        btnPlay.setIcon(new ImageIcon(AppFrame.class.getResource("/images/play-16x16.png")));
        toolBar.add(btnPlay);

        GridBagConstraints gbc_toolBar = new GridBagConstraints();
        gbc_toolBar.weightx = 1.0;
        gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolBar.anchor = GridBagConstraints.NORTHWEST;
        gbc_toolBar.gridx = 0;
        gbc_toolBar.gridy = 0;
        getContentPane().add(toolBar, gbc_toolBar);

        tblTasks = new JTable();
        tblTasks.addKeyListener(new TasksTableListener());
        tblTasks.setShowVerticalLines(false);
        tblTasks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblTasks.setDefaultRenderer(Task.class, new TaskTypeTableCellRenderer());
        tblTasks.setFillsViewportHeight(true);
        tblTasks.setGridColor(tblTasks.getTableHeader().getBackground());
        tasksTableModel = new TasksTableModel();
        tblTasks.setModel(tasksTableModel);
        tblTasks.getColumnModel().getColumn(0).setMinWidth(24);
        tblTasks.getColumnModel().getColumn(0).setPreferredWidth(24);
        tblTasks.getColumnModel().getColumn(0).setMaxWidth(24);
        tblTasks.getColumnModel().getColumn(2).setMinWidth(60);
        tblTasks.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblTasks.getColumnModel().getColumn(2).setMaxWidth(120);
        StringComparator stringComparator = new StringComparator();
        TableRowSorter<TasksTableModel> rowSorter = new TableRowSorter<TasksTableModel>(tasksTableModel);
        rowSorter.setComparator(0, new TaskComparator());
        rowSorter.setComparator(1, stringComparator);
        rowSorter.setComparator(2, stringComparator);
        tblTasks.setRowSorter(rowSorter);

        JScrollPane spTasks = new JScrollPane(tblTasks);
        spTasks.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        GridBagConstraints gbc_spTasks = new GridBagConstraints();
        gbc_spTasks.weighty = 1.0;
        gbc_spTasks.weightx = 1.0;
        gbc_spTasks.fill = GridBagConstraints.BOTH;
        gbc_spTasks.gridx = 0;
        gbc_spTasks.gridy = 1;
        getContentPane().add(spTasks, gbc_spTasks);

        progressBar = new JProgressBar();
        progressBar.setAlignmentY(Component.TOP_ALIGNMENT);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc_progressBar = new GridBagConstraints();
        gbc_progressBar.weightx = 1.0;
        gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_progressBar.gridx = 0;
        gbc_progressBar.gridy = 2;
        getContentPane().add(progressBar, gbc_progressBar);

        statusBar = new JLabel(" ");
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        GridBagConstraints gbc_statusBar = new GridBagConstraints();
        gbc_statusBar.weightx = 1.0;
        gbc_statusBar.insets = new Insets(1, 1, 1, 1);
        gbc_statusBar.fill = GridBagConstraints.HORIZONTAL;
        gbc_statusBar.gridx = 0;
        gbc_statusBar.gridy = 3;
        getContentPane().add(statusBar, gbc_statusBar);

        setMinimumSize(new Dimension(400, 300));
    }

    private static JFileChooser createFolderChooser(String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return fileChooser;
    }

    private void runWorker(String status, Worker worker) {
        processing = true;
        btnScan.setEnabled(false);
        btnCompare.setEnabled(false);
        btnClone.setEnabled(false);
        progressBar.setValue(0);
        setStatus(status);
        worker.addPropertyChangeListener(progressListener);
        worker.execute();
    }

    public void workerCompleted(String status) {
        processing = false;
        btnScan.setEnabled(true);
        btnCompare.setEnabled(true);
        btnClone.setEnabled(true);
        progressBar.setIndeterminate(false);
        setStatus(status);
    }

    public void setPreviewButtonsEnabled(boolean enabled) {
        btnFilter.setEnabled(enabled);
        btnPlay.setEnabled(enabled);
    }

    public void updatePreview(Delta delta) {
        setPreviewButtonsEnabled(delta != null);
        if (delta != null || tblTasks.getRowCount() > 0) {
            tasksTableModel.setData(delta);
        }
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    public void setStatus(String text) {
        statusBar.setText(text == null? " ": " " + text);
    }

}