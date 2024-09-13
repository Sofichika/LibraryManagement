package GUI;

import Lib.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

class BookTableModel extends AbstractTableModel {
    private ArrayList<Book> books = new ArrayList<>();
    private final String[] columnNames = {"ID", "Title", "Author", "Year", "ISBN", "Genre"};

    public void addBook(Book book) {
        books.add(book);
        fireTableRowsInserted(books.size() - 1, books.size() - 1);
    }

    public void initBooks(ArrayList<Book> bs) {
        books.addAll(bs);
    }

    public void removeBook(int rowIndex) {
        fireTableRowsDeleted(rowIndex, rowIndex);
        books.remove(rowIndex);
    }

    public Book getBookAt(int rowIndex) {
        return books.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> book.getId();
            case 1 -> book.getTitle();
            case 2 -> book.getAuthor();
            case 3 -> book.getYearOfPublication();
            case 4 -> book.getISBN();
            case 5 -> book.getGenre();
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 3 || columnIndex == 4) return Integer.class;
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // All cells remain non-editable
    }

}