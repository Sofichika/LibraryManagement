package GUI;

import Lib.Book;
import Lib.Library;

import javax.swing.*;
import java.awt.*;

public class BookDeletionDialog extends JDialog {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaBox;
    private JButton deleteButton;
    private JButton cancelButton;
    private Library library;
    private LibraryManagementUI parent;

    public BookDeletionDialog(JFrame parent, Library library) {
        super(parent, "Delete Book", true);
        this.library = library;
        this.parent = (LibraryManagementUI) parent;
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

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this book?\n\n" + bookToDelete.toString(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (library.removeRecord(bookToDelete)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}