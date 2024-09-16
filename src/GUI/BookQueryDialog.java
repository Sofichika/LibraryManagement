package GUI;

import Lib.Book;
import Lib.Library;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BookQueryDialog extends JDialog {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaBox;
    private JButton cancelButton;
    private JButton exportButton;
    private JButton searchButton;
    private final Library library;
    private BookTableModel tableModel;
    private JTable itemTable;
    private ArrayList<Book> results;

    public BookQueryDialog(JFrame parent, Library library) {
        super(parent, "Query books", true);
        this.library = library;
        results = new ArrayList<>();
        initComponents();
        layoutComponents();
        addListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        String[] options = {"Title", "Author", "ISBN", "Genre"};
        searchField = new JTextField(20);
        searchCriteriaBox = new JComboBox<>(options);
        searchCriteriaBox.setSelectedIndex(0);
        cancelButton = new JButton("Cancel");
        exportButton = new JButton("Export");
        exportButton.setEnabled(false);
        searchButton = new JButton("Search");
        tableModel = new BookTableModel();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(0, 0));
        setMaximumSize(new Dimension(400, 800));

        //Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        searchPanel.add(new JLabel("Search by:"));
        searchField.setBorder(BorderFactory.createEtchedBorder());
        searchPanel.add(searchCriteriaBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // List table panel
        itemTable = new JTable();
        itemTable.setSelectionBackground(Color.LIGHT_GRAY);
        ListSelectionModel selectionModel = itemTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(itemTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> dispose());
        searchButton.addActionListener(e -> onSearch());
        searchField.addActionListener(e -> onSearch());
        exportButton.addActionListener(e -> exportQueryResults());
    }

    private void onSearch() {
        if (searchCriteriaBox.getSelectedItem() == null) {
            return;
        }
        results = library.search(
            searchCriteriaBox.getSelectedItem().toString().toLowerCase(),
            searchField.getText().toLowerCase()
        );
        tableModel = new BookTableModel();
        itemTable.setModel(tableModel);
        tableModel.initBooks(results);
        exportButton.setEnabled(!results.isEmpty());
    }

    private void exportQueryResults() {
        if (results == null || results.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No query results to export. Please perform a query first.",
                "Export Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Query Results");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                for (Book book : results) {
                    writer.write(book.toString() + "\n\n");
                }
                JOptionPane.showMessageDialog(this,
                    "Query results exported successfully.",
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting query results: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public int showDialog() {
        setVisible(true);
        return 1;
    }
}
