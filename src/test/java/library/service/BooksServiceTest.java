package library.service;
import library.model.Book;
import library.model.Person;
import library.repositories.BooksRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BooksServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BooksService booksService;

    @Test
    void testFindOne() {
        Book book = new Book();
        book.setBookId(1);
        book.setName("Test Book");

        when(booksRepository.findById(1)).thenReturn(Optional.of(book));

        Book foundBook = booksService.findOne(1);

        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getName());
    }

    @Test
    void testGetPersonByBookId() {
        // Создаем объект Person для теста. Это будет человек, которого мы ожидаем найти.
        Person person = new Person();
        person.setPersonId(1);
        person.setName("Test Person");

        // Создаем объект Book для теста. Это книга, которую мы будем использовать для поиска владельца.
        Book book = new Book();
        book.setBookId(1);
        book.setName("Test Book");
        book.setOwner(person); // Устанавливаем созданного выше человека владельцем этой книги.

        // Создаем объект Optional из объекта book.
        // Optional используется, потому что метод findById возвращает Optional.
        Optional<Book> optionalBook = Optional.of(book);

        // Настраиваем поведение mock-объекта.
        // Мы говорим mock-объекту, что когда он вызывает метод findById с параметром 1, он должен вернуть optionalBook.
        when(booksRepository.findById(1)).thenReturn(optionalBook);

        // Вызываем тестируемый метод. Это метод, который мы хотим проверить.
        Optional<Person> foundPerson = booksService.getPersonByBookId(1);

        // Выводим результат в консоль.
        // Если foundPerson содержит значение (то есть мы нашли человека), выводим его имя.
        // Если foundPerson не содержит значение (то есть мы не нашли человека), выводим сообщение об ошибке.
        if (foundPerson.isPresent()) {
            System.out.println("Found person: " + foundPerson.get().getName());
        } else {
            System.out.println("No person found for this book ID.");
        }

        // Проверяем результат.
        // Мы ожидаем, что foundPerson будет содержать значение (то есть мы нашли человека).
        // Мы также ожидаем, что найденный человек будет равен ожидаемому (то есть person).
        assertTrue(foundPerson.isPresent());
        assertEquals(person, foundPerson.get());
    }
}