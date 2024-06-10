package library.service;

import library.model.Book;
import library.model.Person;
import library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

@Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    //возвращаем всех людей
    public List<Person> findAll(){
        return peopleRepository.findAll(); //возвращает все сущности из таблицы
    }

    //вернем одного человека по его id
    public Person findOne(int id) { //репозиторий знает что id это целое число
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }


    //сохраняем человека
    @Transactional //-над методом будет иметь преимущество перед аннотацией над классом
    public void save(Person person) {
        peopleRepository.save(person);
    }

    //обновляем человека
    @Transactional
    public void update(int id, Person updatePerson){
        updatePerson.setPersonId(id); //назначаем id который пришел
        peopleRepository.save(updatePerson); //save для обновления и добавления сущности
    }

    //удаляем человека
    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

}
