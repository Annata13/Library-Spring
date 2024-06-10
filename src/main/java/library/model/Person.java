package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @NotEmpty(message = "Name should not be empty") //запрет пустого имени
    @Size(min=2, max=50, message = "Name should be between 2 and 50 characters") //диапазон символов в имени
    @Column(name = "name")
    private String name;

    @Min(value = 1500,message = "Year should be greater than 1500") // минимальное значение 0
    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private List<Book> books;

    public Person( String name, int year) {
        this.name = name;
        this.year = year;
    }
public Person(){

}

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public @NotEmpty(message = "Name should not be empty") @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name should not be empty") @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters") String name) {
        this.name = name;
    }

    @Min(value = 1500, message = "Year should be greater than 1500")
    public int getYear() {
        return year;
    }

    public void setYear(@Min(value = 1500, message = "Year should be greater than 1500") int year) {
        this.year = year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "year=" + year +
                ", name='" + name + '\'' +
                ", person_id=" + personId +
                '}';
    }
}
