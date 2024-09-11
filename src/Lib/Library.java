package Lib;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Library {
    private static final String[] VALID_FIELDS = {"title", "author", "isbn", "year", "genre"};
    private final ArrayList<Book> books;
    public static int ID = 0;
    public Library() {
        books = new ArrayList<>();
        loadSamples();
    }

    public boolean addRecord(Book book) {
        return books.add(book);
    }

    public boolean removeRecord(Book book) {
        return books.remove(book);
    }

    public Book find(String field, String value) {
        for (Book book : books) {
            switch (field) {
                case "title":
                    if (book.getTitle().equals(value)) {
                        return book;
                    }
                    break;
                case "author":
                    if (book.getAuthor().equals(value)) {
                        return book;
                    }
                    break;
                case "isbn":
                    if (book.getISBN().equals(value)) {
                        return book;
                    }
                    break;
                case "genre":
                    if (book.getGenre().equals(value)) {
                        return book;
                    }
                    break;
                case "year":
                    if (book.getYearOfPublication() == Integer.parseInt(value)) {
                        return book;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("provided field " + field + " does not exist");
            }
        }
        return null;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void loadSamples() {
        try {
            File f = new File("data/books.txt");
            Scanner sc = new Scanner(f);

            Book book = new Book(ID++);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    addRecord(book);
                    book = new Book(ID++);
                    continue;
                }
                switch (line) {
                    case String s when s.startsWith("Title") -> {
                        book.setTitle(line.replace("Title: ", ""));
                    }
                    case String s when s.startsWith("Author") -> {
                        book.setAuthor(line.replace("Author: ", ""));
                    }
                    case String s when s.startsWith("Year") -> {
                        book.setYearOfPublication(Integer.parseInt(line.replace("Year: ", "")));
                    }
                    case String s when s.startsWith("ISBN") -> {
                        book.setISBN(line.replace("ISBN: ", ""));
                    }
                    case String s when s.startsWith("Genre") -> {
                        book.setGenre(line.replace("Genre: ", ""));
                    }
                    default -> throw new IllegalArgumentException("provided field " + line + " does not exist");
                }
            }

            addRecord(book);
            sc.close();
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void processInstructions() {
        try {
            File f = new File("data/instructions.txt");
            Scanner sc = new Scanner(f);

            while (sc.hasNext()) {
                String cmd = sc.nextLine();
                if (cmd.isEmpty()) {
                    continue;
                }

                switch (cmd) {
                    case String s when s.startsWith("add") -> {
                        processAddCmd(cmd);
                    }
                    case String s when s.startsWith("delete") -> {
                        processDeleteCmd(cmd);
                    }
                    case String s when s.startsWith("query") -> {
                        processQueryCmd(cmd);
                    }
                    case String s when s.equals("save") -> {
                        return;
                    }
                    default -> {
                        System.out.println("provided command " + cmd + " does not exist");
                    }
                }
            }

            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    private void processAddCmd(String cmd) {
        // Remove 'add ' from cmd
        cmd = cmd.substring(4);
        String[] subCmds = cmd.split(";");
        Book newBook = new Book(ID++);
        for (String subCmd : subCmds) {
            try {
                subCmd = subCmd.trim();
                switch (subCmd) {
                    case String s when s.startsWith("title") -> {
                        newBook.setTitle(subCmd.substring(6));
                    }
                    case String s when s.startsWith("author") -> {
                        newBook.setAuthor(subCmd.substring(7));
                    }
                    case String s when s.startsWith("year") -> {
                        newBook.setYearOfPublication(Integer.parseInt(subCmd.substring(5)));
                    }
                    case String s when s.startsWith("ISBN") -> {
                        newBook.setISBN(subCmd.substring(5));
                    }
                    case String s when s.startsWith("genre") -> {
                        newBook.setGenre(subCmd.substring(6));
                    }
                    default -> throw new IllegalStateException("Unexpected add command: " + subCmd);
                }
            } catch (Exception e) {
                System.out.printf("Error processing command '%s': %s\n", subCmd, e.getMessage());
            }

        }
        if (addRecord(newBook)) {
            logOutput();
        }
    }

    private void processDeleteCmd(String cmd) {
        String originalCmd = cmd;
        // Remove 'delete' from cmd
        cmd = cmd.substring(7);
        Book book;
        try {
            // Split cmd to get field which define the specified criteria for deleting
            String field = cmd.split(" ")[0];
            if (!Arrays.asList(VALID_FIELDS).contains(field)) {
                System.out.println("provided field " + field + " is invalid");
                return;
            }

            book = find(field, cmd.substring(field.length() + 1));
            if (book != null) {
                if (removeRecord(book)) {
                    logOutput();
                }
            }
        } catch (Exception e) {
            System.out.printf("Error processing delete command '%s': %s\n", originalCmd, e.getMessage());
        }
    }

    private void processQueryCmd(String cmd) {
        String originalCmd = cmd;
        // Remove 'query ' from cmd
        cmd = cmd.substring(6);
        Book book;

        try {
            // Split cmd to get field which define the specified criteria for deleting
            String field = cmd.split(" ")[0];

            // Check provided field is valid or not
            if (!Arrays.asList(VALID_FIELDS).contains(field)) {
                System.out.println("provided field " + field + " is invalid");
                return;
            }

            book = find(field, cmd.substring(field.length() + 1));
            if (book != null) {
                logQuery(cmd, book);
            }
        } catch (Exception e) {
            System.out.printf("Error processing delete command '%s': %s\n", originalCmd, e.getMessage());
        }
    }

    /*
    Log query to report file
     */
    private void logQuery(String cmd, Book book) {
        try {
            File reportFile = new File("report.txt");
            PrintWriter out = new PrintWriter(new FileWriter(reportFile, true));
            out.printf("Query: %s\n---------------------------------\n%s\n\n", cmd, book);
            out.close();
        } catch (IOException e) {
            System.out.println("Error when log query: " + e.getMessage());
        }
    }

    /*
    Log to output file when add or delete book
     */
    private void logOutput() {
        try {
            File reportFile = new File("output.txt");
            PrintWriter out = new PrintWriter(new FileWriter(reportFile, true));
            out.println("Records updated successfully.");
            out.close();
        } catch (IOException e) {
            System.out.println("Error when log output: " + e.getMessage());
        }
    }

    /*
    Delete report and output files of the last run time
     */
    public void clearFiles() {
        File reportFile = new File("report.txt");
        File outputFile = new File("output.txt");
        try {
            if (reportFile.delete()) {
                System.out.println("Report file deleted successfully");
            }
            if (outputFile.delete()) {
                System.out.println("Output file deleted successfully");
            }
        } catch (SecurityException e) {
            System.out.println("Can not delete file because of security reason");
        }

    }
}