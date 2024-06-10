package library.controllers;

import jakarta.validation.Valid;
import library.model.Book;
import library.model.Person;
import library.service.BooksService;
import library.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller // Аннотация, указывающая, что класс является контроллером в Spring MVC.
@RequestMapping("/books") // Аннотация, указывающая, что все методы в этом контроллере будут обрабатывать запросы, начинающиеся с "/books".
public class BooksController {

    // Сервисы, используемые для взаимодействия с базой данных.
    private final BooksService booksService;
    private final PeopleService peopleService;

    // Конструктор с автоматическим внедрением зависимостей Spring.
    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    // Метод для отображения всех книг.
    @GetMapping() // Обрабатывает HTTP GET запросы.
    public String index(Model model){
        model.addAttribute("books",booksService.findAll()); // Добавляем атрибут со всеми книгами в модель.
        return "books/index"; // Возвращаем имя представления.
    }

    // Метод для отображения одной книги.
 /*   @GetMapping("/{id}") // Обрабатывает HTTP GET запросы с указанным id.
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.findOne(id)); // Добавляем атрибут с найденной книгой в модель.
       Optional<Person> personOwner  = booksService.getPersonByBookId(id); //получить владельца книги
        model.addAttribute("people", peopleService.findAll()); //список из всех людей для выпадающего

        if(personOwner.isPresent()){
            model.addAttribute("owen", personOwner.get()); // владелец книги

        }else {
            model.addAttribute("people", peopleService.findAll()); //список из всех людей для выпадающего
        }
        return "books/show"; // Возвращаем имя представления.
    }*/
    @GetMapping("/{id}") // Обрабатывает HTTP GET запросы с указанным id.
    public String show(@PathVariable("id") int id, Model model){
        Book book = booksService.findOne(id);
        if (book == null) {
            // Обработка ошибки, если книга не найдена
            // Возвращаем страницу с сообщением об ошибке или перенаправляем на другую страницу
            return "redirect:/error";
        }
        model.addAttribute("book", book); // Добавляем атрибут с найденной книгой в модель.
        Optional<Person> personOwner  = booksService.getPersonByBookId(id); //получить владельца книги

        if(personOwner.isPresent()){
            model.addAttribute("owen", personOwner.get()); // владелец книги
        } else {
            model.addAttribute("people", peopleService.findAll()); //список из всех людей для выпадающего
        }

        // Добавляем объект person в модель
       model.addAttribute("person", new Person());

        return "books/show"; // Возвращаем имя представления.
    }

    // Методы для назначения и освобождения книги.
  /*  @PatchMapping("{id}/assaign") // Обрабатывает HTTP PATCH запросы с указанным id.
    public String assaign(@PathVariable("id") int id, @ModelAttribute("person") Person selectPerson ){
        booksService.assaign(id,selectPerson);

        return "redirect:/books/" + id; // Перенаправляем на страницу книги.
    }*/

    @PatchMapping("{id}/assaign") // Обрабатывает HTTP PATCH запросы с указанным id.
    public String assaign(@PathVariable("id") int id, @RequestParam("personId") int personId ){
        Person selectPerson = peopleService.findOne(personId);
        if (selectPerson == null) {
            // Обработка ошибки, если Person не найден
            // Возвращаем страницу с сообщением об ошибке или перенаправляем на другую страницу
            return "redirect:/error";
        }
        booksService.assaign(id, selectPerson);
        return "redirect:/books/" + id; // Перенаправляем на страницу книги.
    }

    @PatchMapping("{id}/release") // Обрабатывает HTTP PATCH запросы с указанным id.
    public String release(@PathVariable("id") int id ){
        booksService.release(id);
        return "redirect:/books/" + id; // Перенаправляем на страницу книги.
    }

    // Методы для создания книги.
    @GetMapping("/new") // Обрабатывает HTTP GET запросы на "/books/new".
    public String newPerson(@ModelAttribute("book") Book book){
        return "books/new"; // Возвращаем имя представления.
    }
    @PostMapping() // Обрабатывает HTTP POST запросы.
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new"; // Если есть ошибки валидации, возвращаемся на страницу создания книги.
        }
        booksService.save(book); // Сохраняем книгу.
        return "redirect:/books"; // Перенаправляем на страницу со всеми книгами.
    }

    // Методы для обновления книги.
    @GetMapping("/{id}/edit") // Обрабатывает HTTP GET запросы на "/books/{id}/edit".
    public String edit(Model model, @PathVariable("id") int id ){
        model.addAttribute("book", booksService.findOne(id)); // Добавляем атрибут с найденной книгой в модель.
        return "books/edit"; // Возвращаем имя представления.
    }
    @PatchMapping("/{id}") // Обрабатывает HTTP PATCH запросы с указанным id.
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            return "books/edit"; // Если есть ошибки валидации, возвращаемся на страницу редактирования книги.
        }
        booksService.update(id,book); // Обновляем книгу.
        return "redirect:/books"; // Перенаправляем на страницу со всеми книгами.
    }

    // Метод для удаления книги.
    @DeleteMapping("/{id}") // Обрабатывает HTTP DELETE запросы с указанным id.
    public String delete(@PathVariable("id" ) int id){
        booksService.delete(id); // Удаляем книгу.
        return "redirect:/books"; // Перенаправляем на страницу со всеми книгами.
    }
}