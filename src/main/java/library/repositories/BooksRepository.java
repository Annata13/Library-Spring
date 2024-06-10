package library.repositories;

import library.model.Book;
import library.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    //Ищем книги по названию
    List<Book> findByName(String name); //Name - это поле в классе Person

    //Ищем книги по имени и сортируем по возрасту
    List<Book> findByNameOrderByYear(String name);

    //Ищем книги  по году
    List<Book> findByYear(Integer year);

    //ищем книги по первой букве в имени
    List<Book> findByNameStartingWith(String startingWith); //передаем букву или несколько букв имени по которым будем искать

    //метод возвращает людей по имени или адрессу электронной почты
    // List<Person> findByNameOrEmail(String name, String email );
}
