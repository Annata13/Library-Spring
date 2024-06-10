package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @NotEmpty(message = "Name should not be empty") //запрет пустого имени
    @Size(min=2, max=50, message = "Name should be between 2 and 50 characters") //диапазон символов в имени
    @Column(name = "name")
    private String name;

    @Min(value = 1000,message = "Year should be greater than 1000") // минимальное значение 0
    @Column(name = "year")
    private int year;

    @NotEmpty(message = "Author should not be empty") //запрет пустого имени
    @Column(name = "author")
    private String author;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    public Book( String name, int year, String author) {
        this.name = name;
        this.year = year;
        this.author = author;
    }
    public Book(){}

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public @NotEmpty(message = "Author should not be empty") String getAuthor() {
        return author;
    }

    public void setAuthor(@NotEmpty(message = "Author should not be empty") String author) {
        this.author = author;
    }

    @Min(value = 1000, message = "Year should be greater than 1000")
    public int getYear() {
        return year;
    }

    public void setYear(@Min(value = 1000, message = "Year should be greater than 1000") int year) {
        this.year = year;
    }

    public @NotEmpty(message = "Name should not be empty") @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name should not be empty") @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters") String name) {
        this.name = name;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int book_id) {
        this.bookId = book_id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", year=" + year +
                ", name='" + name + '\'' +
                ", book_id=" + bookId +
                '}';
    }
}
