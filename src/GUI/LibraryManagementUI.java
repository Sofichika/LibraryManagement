package GUI;

import Lib.Book;
import Lib.Library;
import javax.swing.*;
import java.awt.*;

public class LibraryManagementUI extends JFrame {
    private JTable itemTable;
    private BookTableModel tableModel;
    private Library library;

    public LibraryManagementUI() {
        setTitle("Library Management");
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
//        setResizable(false);

        setLayout(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttonLabels = {
                "Create",
                "Delete",
                "Export",
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> handleAction(label));
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.WEST);



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
        };

        tableModel.initBooks(library.getBooks());

        itemTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(itemTable);

        add(tableScrollPane, BorderLayout.CENTER);
    }
    private void handleAction(String action) {
        System.out.println(action + " action triggered");

        switch (action) {
            case "Create":
                createBook();
                break;
            case "Read":
//                readSelectedBook();
                break;
            case "Delete":
//                deleteSelectedBook();
                break;
        }
    }

    private void createBook() {
        BookCreationDialog dialog = new BookCreationDialog(this);
        Book newBook = dialog.showDialog();
        if (newBook != null && newBook.validate()) {
            tableModel.addBook(newBook);
        }
    }
}