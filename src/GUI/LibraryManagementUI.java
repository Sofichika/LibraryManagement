package GUI;

import Lib.Book;
import Lib.Library;
import javax.swing.*;
import java.awt.*;

public class LibraryManagementUI extends JFrame {
    private JTable itemTable;
    private BookTableModel tableModel;
    private Library library;
    private JButton deleteBtn;

    private static final String[] BUTTONS = {
        "Create",
        "Delete",
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
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = itemTable.getSelectedRow();
                if (selectedRow != -1) {
                    deleteBtn.setEnabled(true);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(itemTable);

        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void initButtons() {
        JPanel buttonPanel = new JPanel(
            new GridLayout(5, 1, 10, 10)
        );
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
            case "Read":
//                button.addActionListener(e -> deleteSelectedBook());
                break;
            case "Delete":
                button.setEnabled(false);
                deleteBtn = button;
                button.addActionListener(e -> deleteSelectedBook());
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

    private void deleteSelectedBook() {
        int selectedRow = itemTable.getSelectedRow();
        Book selectedBook = tableModel.getBookAt(selectedRow);
        System.out.println(selectedBook);
        if (selectedBook == null) {
            return;
        }
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this book?",
            "Delete confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            tableModel.removeBook(selectedRow);
        }
    }

    private void onClose() {
        System.exit(0);
    }
}