package ar.org.crossknight.twinify.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class AppFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable table;

    public AppFrame() {
        setTitle("Twinify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new CardLayout(0, 0));
        
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel, "name_293412821211788");
        
        JPanel previewPanel = new JPanel();
        getContentPane().add(previewPanel, "name_293450307301555");
        previewPanel.setLayout(new BorderLayout(0, 0));

        table = new JTable();
        table.setAutoCreateRowSorter(true);
        table.setModel(new DefaultTableModel(
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
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        previewPanel.add(scrollPane, BorderLayout.CENTER);
    }

}
