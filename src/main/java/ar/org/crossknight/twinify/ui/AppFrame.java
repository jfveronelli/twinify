package ar.org.crossknight.twinify.ui;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Toolkit;

import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.border.BevelBorder;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ListSelectionModel;

import ar.org.crossknight.twinify.domain.delta.Delta;

public class AppFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private boolean processing = false;
    private Delta delta;
    private final JButton btnScan;
    private final JButton btnCompare;
    private final JButton btnClone;
    private final JButton btnPlay;
    private final JTable tblTasks;
    private final JProgressBar progressBar;
    private final JLabel statusBar;
    private final ProgressListener progressListener;

    private class AppWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (!processing) {
                setVisible(false);
                dispose();
            }
        }
    };

    private class ScanActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            JFileChooser fileChooser = createFolderChooser("Choose target folder");
            if (fileChooser.showOpenDialog(AppFrame.this) == JFileChooser.APPROVE_OPTION) {
                updatePreview(null);
                progressBar.setIndeterminate(true);
                runWorker(new ScanWorker(AppFrame.this, fileChooser.getSelectedFile()));
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
                runWorker(new CompareWorker(AppFrame.this, fileChooser.getSelectedFile()));
            }
        }
    }

    private class CloneActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            updatePreview(null);
            progressBar.setIndeterminate(true);
            runWorker(new ClonePreviewWorker(AppFrame.this));
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

        btnPlay = new JButton((String)null);
        btnPlay.setEnabled(false);
        btnPlay.setToolTipText("Execute tasks");
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
        tblTasks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tblTasks.setAutoCreateRowSorter(true);
        tblTasks.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null},
            },
            new String[] {
                "Operation", "Path"
            }
        ) {
            Class[] columnTypes = new Class[] {
                String.class, String.class
            };
            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        tblTasks.setFillsViewportHeight(true);
        JScrollPane spTasks = new JScrollPane(tblTasks);
        spTasks.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        spTasks.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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

        progressListener = new ProgressListener();

        setMinimumSize(new Dimension(400, 300));
    }

    private static JFileChooser createFolderChooser(String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        return fileChooser;
    }

    private void runWorker(AbstractWorker worker) {
        processing = true;
        btnScan.setEnabled(false);
        btnCompare.setEnabled(false);
        btnClone.setEnabled(false);
        progressBar.setValue(0);
        setStatus(null);
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

    public void updatePreview(Delta delta) {
        this.delta = delta;
        boolean enabled = delta != null;
        btnPlay.setEnabled(enabled);
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    public void setStatus(String text) {
        String status = text == null? " ": " " + text;
        statusBar.setText(status);
    }

}