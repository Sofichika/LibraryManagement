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

        // Init table
        tableModel = new BookTableModel();
        itemTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(itemTable);

        library = new Library();
        library.clearFiles();
        library.processInstructions();
        for (Book book : library.getBooks()) {
            System.out.println(book);
            tableModel.addBook(book);
        }


        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void handleAction(String action) {
        System.out.println(action + " action triggered");
        // Implement the actual functionality here

        // Example: Add a new item to the table when "Create" is clicked
        if (action.equals("Create")) {
        }
    }
}