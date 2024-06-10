package library.service;

import library.model.Book;
import library.model.Person;
import library.repositories.BooksRepository;
import library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service // Аннотация, указывающая, что класс является компонентом службы в Spring Framework.
@Transactional(readOnly = true) // Аннотация, указывающая, что все методы класса должны выполняться в транзакции только для чтения.
public class BooksService {

    // Репозитории для взаимодействия с базой данных.
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    // Конструктор с автоматическим внедрением зависимостей Spring.
    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    // Метод для получения всех книг.
    public List<Book> findAll(){
        return booksRepository.findAll(); // Возвращает все записи из таблицы книг.
    }

    // Метод для получения всех книг, принадлежащих определенному человеку.
    public List<Book> getBooksByPersonId(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        Person person;
        if (foundPerson.isPresent()){
            // Извлекаем объект Person из Optional.
            person = foundPerson.get();

            // Возвращаем книги, принадлежащие этому человеку.
            return person.getBooks();
        }
        return Collections.emptyList(); // Если человек не найден, возвращаем пустой список.
    }

    // Метод для получения человека, принадлежащих книги.
    public Optional<Person> getPersonByBookId(int id){

        Optional<Book> foundBook = booksRepository.findById(id);
        Book book;
        if (foundBook.isPresent()){
            // Извлекаем объект Person из Optional.
            book = foundBook.get();

            // Возвращаем человека.
            return Optional.ofNullable(book.getOwner());
        }
        return null;
    }


//назначить кнгу
    @Transactional
    public void assaign(int id, Person person){
        Optional<Book> foundBook = booksRepository.findById(id);
        Book book;
        if (foundBook.isPresent()){
            book=foundBook.get();
            book.setOwner(person);
            booksRepository.save(book);
        }
    }
//освободить книгу
@Transactional
public void release(int id) {
    Optional<Book> foundBook = booksRepository.findById(id);
    if (foundBook.isPresent()){
        Book book = foundBook.get();
        book.setOwner(null);
        booksRepository.save(book);
    }
}

    // Метод для получения одной книги по ее ID.
    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null); // Если книга не найдена, возвращаем null.
    }

    // Метод для сохранения книги.
    @Transactional // Эта аннотация переопределяет аннотацию на уровне класса и позволяет методу вносить изменения в базу данных.
    public void save(Book book) {
        booksRepository.save(book); // Сохраняем книгу в базе данных.
    }

    // Метод для обновления книги.
    @Transactional // Эта аннотация переопределяет аннотацию на уровне класса и позволяет методу вносить изменения в базу данных.
    public void update(int id, Book updateBook){
        updateBook.setBookId(id); // Устанавливаем ID для обновляемой книги.
        booksRepository.save(updateBook); // Сохраняем обновленную книгу в базе данных.
    }

    // Метод для удаления книги.
    @Transactional // Эта аннотация переопределяет аннотацию на уровне класса и позволяет методу вносить изменения в базу данных.
    public void delete(int id){
        booksRepository.deleteById(id); // Удаляем книгу по ID из базы данных.
    }


}