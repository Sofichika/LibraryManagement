package GUI;

import Lib.Book;
import Lib.Library;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LibraryManagementUI extends JFrame {
    private JTable itemTable;
    private BookTableModel tableModel;
    private Library library;

    private ArrayList<Book> lastQueryResults;
    private JButton deleteBtn;

    private JButton queryBtn;
    private JButton saveBtn;

    private static final String[] BUTTONS = {
            "Create",
            "Delete",
            "Query",
            "Save",
            "Export",
            "Exit",
    };

    public LibraryManagementUI() {
        // Init base layout form
        initMetadata();

        // Init action buttons
        initButtons();

        // Init list book table
        initListTable();
    }

    private void initListTable() {
        library = new Library();
        library.clearFiles();
        // Init table
        tableModel = new BookTableModel() {
            @Override
            public void fireTableRowsInserted(int firstRow, int lastRow) {
                super.fireTableRowsInserted(firstRow, lastRow);
                for (int i = firstRow; i <= lastRow; i++) {
                    Book book = tableModel.getBookAt(i);
                    library.addRecord(book);
                }
            }

            @Override
            public void fireTableRowsDeleted(int firstRow, int lastRow) {
                super.fireTableRowsDeleted(firstRow, lastRow);
                for (int i = firstRow; i <= lastRow; i++) {
                    Book book = tableModel.getBookAt(i);
                    library.removeRecord(book);
                }
            }
        };

        tableModel.initBooks(library.getBooks());
        itemTable = new JTable(tableModel);
        itemTable.setSelectionBackground(Color.LIGHT_GRAY);

        ListSelectionModel selectionModel = itemTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(itemTable);

        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void initButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(BUTTONS.length, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String label : BUTTONS) {
            JButton button = createButton(label);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.WEST);
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);

        switch (label) {
            case "Create":
                button.addActionListener(e -> createBook());
                break;
            case "Delete":
                deleteBtn = button;
                button.addActionListener(e -> deleteSelectedBook());
                break;
            case "Query":
                button.addActionListener(e -> queryBooks());
                break;
            case "Save":
                button.addActionListener(e -> saveLibraryState());
                break;
            case "Export":
                button.addActionListener(e -> exportQueryResults());
                break;
            case "Exit":
                button.addActionListener(e -> onClose());
                break;
        }

        return button;
    }

    private void initMetadata() {
        setTitle("Library Management");
        ImageIcon icon = new ImageIcon("icon.png");
        setIconImage(icon.getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


    }

    private void createBook() {
        BookCreationDialog dialog = new BookCreationDialog(this);
        Book newBook = dialog.showDialog();
        if (newBook != null && newBook.validate()) {
            tableModel.addBook(newBook);
        }
    }

    private void queryBooks() {
        String[] options = {"Title", "Author", "ISBN", "Genre"};
        String field = (String) JOptionPane.showInputDialog(this,
                "Select search field:",
                "Query Books",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (field != null) {
            String value = JOptionPane.showInputDialog(this,
                    "Enter " + field + " to search for:",
                    "Query Books",
                    JOptionPane.QUESTION_MESSAGE);

            if (value != null && !value.trim().isEmpty()) {
                Book result = library.find(field.toLowerCase(), value);
                if (result != null) {
                    lastQueryResults = new ArrayList<>();
                    lastQueryResults.add(result);
                    JOptionPane.showMessageDialog(this,
                            result.toString(),
                            "Query Result",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No book found with the given " + field + ".",
                            "Query Result",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void saveLibraryState() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Library State");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                for (Book book : library.getBooks()) {
                    writer.write(book.toString() + "\n\n");
                }
                JOptionPane.showMessageDialog(this,
                        "Library state saved successfully.",
                        "Save Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving library state: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportQueryResults() {
        if (lastQueryResults == null || lastQueryResults.isEmpty()) {
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
                for (Book book : lastQueryResults) {
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

    private void deleteSelectedBook() {
        BookDeletionDialog dialog = new BookDeletionDialog(this, library);
        dialog.setVisible(true);
    }

    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }

    private void onClose() {
        System.exit(0);
    }
}