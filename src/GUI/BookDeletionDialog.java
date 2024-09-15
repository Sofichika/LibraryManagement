package GUI;

import Lib.Book;
import Lib.Library;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BookDeletionDialog extends JDialog {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaBox;
    private JButton deleteButton;
    private JButton cancelButton;
    private final BookTableModel tableModel;
    private final Library library;
    private int indexSelectedBook = -1;

    public BookDeletionDialog(JFrame parent, Library library, BookTableModel tableModel) {
        super(parent, "Delete Book", true);
        this.tableModel = tableModel;
        this.library = library;
        initComponents();
        layoutComponents();
        addListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        searchField = new JTextField(20);
        searchCriteriaBox = new JComboBox<>(new String[]{"Title", "ISBN"});
        deleteButton = new JButton("Delete");
        cancelButton = new JButton("Cancel");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchCriteriaBox);
        searchPanel.add(searchField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        add(searchPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        deleteButton.addActionListener(e -> deleteBook());
        searchField.addActionListener(e -> deleteBook());
        cancelButton.addActionListener(e -> dispose());
    }

    private void deleteBook() {
        String searchCriteria = (String) searchCriteriaBox.getSelectedItem();
        String searchValue = searchField.getText().trim();

        if (searchValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search value.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book bookToDelete = library.find(searchCriteria.toLowerCase(), searchValue);

        if (bookToDelete == null) {
            JOptionPane.showMessageDialog(this, "No book found with the given " + searchCriteria + ".", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        indexSelectedBook = tableModel.getIndexOfBook(bookToDelete);
        dispose();
    }

    public int showDialog() {
        setVisible(true);
        return indexSelectedBook;
    }
}