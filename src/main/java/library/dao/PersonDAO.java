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
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate){this.jdbcTemplate=jdbcTemplate;}

    //вернули весь список
    public List<Person> index(){
        return jdbcTemplate.query("SELECT*FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    //сохранить нового человека
    public void save(Person person){
jdbcTemplate.update("INSERT INTO Person(name, year) VALUES (?,?)", person.getName(), person.getYear());
    }

    //вернули из списка чела по id
    public Person show(int id){
        return jdbcTemplate.query("SELECT*FROM Person WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }


    //удаляем человека
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE person_id=?",id);
    }

    //обновляем человека
    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE Person SET name=?, year=? WHERE person_id=?", person.getName(), person.getYear(), id);
    }

    //для уникальности имени
    public Optional<Person> getPersonByFullName(String name) {
        return jdbcTemplate.query("SELECT*FROM Person WHERE name=?", new Object[]{name},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    //список книг людей
    public List<Book> getBooksByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
