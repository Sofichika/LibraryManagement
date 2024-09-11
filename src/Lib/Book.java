package Lib;

public class Book {
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private String genre;
    private int yearOfPublication;

    public Book(int id) {
        this.id = id;
    }

    public Book(String title, String author, String ISBN, String genre, int yearOfPublication) {
        this.title = title;
        this.author = author;
        setISBN(ISBN);
        this.genre = genre;
        this.yearOfPublication = yearOfPublication;
    }

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
//        Title: To Kill a Mockingbird
//        Author: Harper Lee
//        ISBN: 9780061120084
//        Genre: Fiction
        return "Title: " + title + "\n"
                + "Author: " + author + "\n"
                + "ISBN: " + ISBN + "\n"
                + "Genre: " + genre + "\n"
                + "Year: " + yearOfPublication;
    }
}
