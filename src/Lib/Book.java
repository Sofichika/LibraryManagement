package Lib;

public class Book {
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private String genre;
    private int yearOfPublication;

    public Book(int id) {
        setId(id);
    }
//
//    public Book(String title, String author, String ISBN, String genre, int yearOfPublication) {
//        setTitle(title);
//        setAuthor(author);
//        setISBN(ISBN);
//        setGenre(genre);
//        setYearOfPublication(yearOfPublication);
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        if (ISBN.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        this.ISBN = ISBN;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n"
                + "Author: " + author + "\n"
                + "ISBN: " + ISBN + "\n"
                + "Genre: " + genre + "\n"
                + "Year: " + yearOfPublication;
    }

    public boolean validate() {
        return getId() > 0 &&
                !getAuthor().isEmpty() &&
                !getISBN().isEmpty() &&
                !getGenre().isEmpty() &&
                !getTitle().isEmpty() &&
                getYearOfPublication() > 0;
    }
}
