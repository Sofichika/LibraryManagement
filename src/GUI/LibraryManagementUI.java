package GUI;

import Lib.Book;
import Lib.Library;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LibraryManagementUI extends JFrame {
    private JTable itemTable;
    private BookTableModel tableModel;
    private Library library;

    private static final String[] BUTTONS = {
            "Create",
            "Delete",
            "Query",
            "Save",
            "Exit",
    };

    public LibraryManagementUI() {
        // Init base layout form
        initLayout();

        // Init action buttons
        initButtons();

        // Init list book table
        initListTable();
    }

    private void initListTable() {
        library = new Library();
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
                button.addActionListener(e -> deleteSelectedBook());
                break;
            case "Query":
                button.addActionListener(e -> queryBooks());
                break;
            case "Save":
                button.addActionListener(e -> saveLibraryState());
                break;
            case "Exit":
                button.addActionListener(e -> onClose());
                break;
        }

        return button;
    }

    private void initLayout() {
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
        BookQueryDialog dialog = new BookQueryDialog(this, library);
        dialog.showDialog();
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


    private void deleteSelectedBook() {
        BookDeletionDialog dialog = new BookDeletionDialog(this, library, tableModel);
        int indexSelectedBook = dialog.showDialog();
        if (indexSelectedBook != -1) {
            Book bookToDelete = tableModel.getBookAt(indexSelectedBook);

            if (bookToDelete == null) {
                JOptionPane.showMessageDialog(this,
                    "Can not find book " + bookToDelete,
                    "Unexpected Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showOptionDialog(this,
                "Are you sure you want to delete this book?\n\n" + bookToDelete.toString(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new Object[]{"Yes", "No"},
                "No");

            if (confirm == JOptionPane.YES_OPTION) {
                if (tableModel.removeBook(indexSelectedBook)) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete the book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void onClose() {
        System.exit(0);
    }
}