package GUI;
import Lib.Book;
import Lib.Library;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class BookCreationDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;
    private JTextField ISBNField;
    private JTextField genreField;
    private JButton submitButton;
    private JButton cancelButton;
    private Book result;
    JLabel validateError;

    public BookCreationDialog(JFrame parent) {
        super(parent, "Create new book", true);
        setMaximumSize(new Dimension(400, 800));
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        yearField = new JTextField(4);
        ISBNField = new JTextField(4);
        genreField = new JTextField(4);

        submitButton = new JButton("Create");
        submitButton.setEnabled(false);
        cancelButton = new JButton("Cancel");

        // Layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 5, 5, 5);

        // Add error message
        validateError = new JLabel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        JPanel errorPanel = new JPanel();
        validateError.setText("Please enter a book information");
        errorPanel.setBackground(Color.WHITE);
        errorPanel.setPreferredSize(new Dimension(400, 30));
        errorPanel.add(validateError);
        panel.add(errorPanel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(styleField(titleField, "Title"), gbc);

        gbc.gridy = 2;
        panel.add(styleField(authorField, "Author"), gbc);

        gbc.gridy = 3;
        panel.add(styleField(yearField, "Year"), gbc);

        gbc.gridy = 4;
        panel.add(styleField(ISBNField, "ISBN"), gbc);

        gbc.gridy = 5;
        panel.add(styleField(genreField, "Genre"), gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setBackground(Color.WHITE);

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        panel.setBackground(Color.WHITE);

        // Add panel to dialog
        getContentPane().add(panel);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    result = new Book(Library.getID());
                    result.setTitle(titleField.getText());
                    result.setAuthor(authorField.getText());
                    result.setYearOfPublication(Integer.parseInt(yearField.getText()));
                    result.setISBN(ISBNField.getText());
                    result.setGenre(genreField.getText());
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = null;
                dispose();
            }
        });

        // Set dialog properties
        pack();
        setLocationRelativeTo(parent);
    }

    private JTextField styleField(JTextField field, String title) {
        // Add ActionListener
        // Add DocumentListener
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            public void onChange() {
                String errorMessage = validateWithoutDialog();
                validateError.setText(errorMessage);
                submitButton.setEnabled(errorMessage.isEmpty());
                if (errorMessage.isEmpty()) {
                    validateError.setText("Valid book! Press Create to continue");
                    validateError.setForeground(Color.GREEN);
                } else {
                    validateError.setForeground(Color.RED);
                }
            }
        });

        // Add ActionListener
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateWithoutDialog().isEmpty()) {
                    result = new Book(Library.getID());
                    result.setTitle(titleField.getText());
                    result.setAuthor(authorField.getText());
                    result.setYearOfPublication(Integer.parseInt(yearField.getText()));
                    result.setISBN(ISBNField.getText());
                    result.setGenre(genreField.getText());
                    dispose();
                }
            }
        });

        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }

    private String validateWithoutDialog() {
        if (titleField.getText().trim().isEmpty()) {
            return "Title cannot be empty";
        }
        if (authorField.getText().trim().isEmpty()) {
            return "Author cannot be empty";
        }
        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 0 || year > 9999) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            return "Year must be a valid number between 0 and 9999";
        }

        if (ISBNField.getText().trim().isEmpty()) {
            return "ISBN cannot be empty";
        }

        if (genreField.getText().trim().isEmpty()) {
            return "Genre cannot be empty";
        }

        return "";
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Author cannot be empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int year = Integer.parseInt(yearField.getText());
            if (year < 0 || year > 9999) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid number between 0 and 9999", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (ISBNField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ISBN cannot be empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (genreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Genre cannot be empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public Book showDialog() {
        setVisible(true);
        return result;
    }
}