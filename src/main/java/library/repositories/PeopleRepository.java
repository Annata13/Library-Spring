package library.repositories;

import library.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  PeopleRepository extends JpaRepository<Person, Integer> {
    //придерживаемся синтаксиса Spring DAta JPA

    //Ищем людей по имени
    List<Person> findByName(String name); //Name - это поле в классе Person

    //Ищем людей по имени и сортируем по возрасту
    List<Person> findByNameOrderByAge(String age);

    //Ищем людей  по емаил
    List<Person> findByEmail(String email);

    //ищем людей по первой букве в имени
    List<Person> findByNameStartingWith(String startingWith); //передаем букву или несколько букв имени по которым будем искать

    //метод возвращает людей по имени или адрессу электронной почты
    List<Person> findByNameOrEmail(String name, String email );
}
