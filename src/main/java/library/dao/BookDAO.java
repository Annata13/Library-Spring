package library.dao;

import library.model.Book;
import library.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
        private final JdbcTemplate jdbcTemplate;
    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate){this.jdbcTemplate=jdbcTemplate;}

        //вернули весь список
        public List<Book> index(){
        return jdbcTemplate.query("SELECT*FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

        //сохранить новую книгу
        public void save(Book book){
        jdbcTemplate.update("INSERT INTO Book(name, author, year) VALUES (?,?,?)", book.getName(),book.getAuthor(), book.getYear());
    }

        //вернули из списка книнг по id
        public Book show(int id){
        return jdbcTemplate.query("SELECT*FROM Book WHERE book_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }


        //удаляем книгу
        public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?",id);
    }

        //обновляем книгу
        public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=? WHERE book_id=?", book.getName(),
                book.getAuthor(), book.getYear(), id);
    }

    //вернем человека у которого сейчас книга
    public Optional<Person> getPeopleByBook(int id){
    return jdbcTemplate.query("select person.* from book  JOIN Person  on book.person_id = Person.person_id WHERE Book.book_id=?",
            new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    //назначить книгу
    public void assaign(int id, Person selectPerson) {
         jdbcTemplate.update("UPDATE  Book SET person_id=? WHERE book_id=? ", selectPerson.getPerson_id(), id);
    }

    //освободить книгу
    public void release(int id) {
        jdbcTemplate.update("UPDATE  Book SET person_id=NULL WHERE book_id=? ",  id);
    }
}
