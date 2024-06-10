package library.controllers;

import jakarta.validation.Valid;
import library.dao.PersonDAO;
import library.model.Person;
import library.service.BooksService;
import library.service.PeopleService;
import library.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    // Инжектируем зависимости
    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator, PeopleService peopleService, BooksService booksService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    // Метод для показа всех людей
    @GetMapping()
    public String index(Model model){
        // Добавляем в модель список всех людей и возвращаем представление "index"
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    // Метод для показа информации о конкретном человеке
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        // Добавляем в модель выбранного человека и его книги, а затем возвращаем представление "show"
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", booksService.getBooksByPersonId(id));
        //model.addAttribute("books", booksService.findOne(id));

        return "people/show";
    }

    // Метод для отображения формы создания нового человека
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        // Возвращаем представление "new"
        return "people/new";
    }

    // Метод для создания нового человека
    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        // Валидируем данные
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()){
            // Если обнаружены ошибки, возвращаем представление "new"
            return "people/new";
        }

        // Сохраняем нового человека и перенаправляем на список всех людей
        peopleService.save(person);
        return "redirect:/people";
    }

    // Метод для отображения формы редактирования человека
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        // Добавляем в модель выбранного человека и возвращаем представление "edit"
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    // Метод для обновления информации о человеке
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        // Валидируем данные
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()){
            // Если обнаружены ошибки, возвращаем представление "edit"
            return "people/edit";
        }

        // Обновляем информацию о человеке и перенаправляем на список всех людей
        peopleService.update(id, person);
        return "redirect:/people";
    }

    // Метод для удаления человека
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        // Удаляем человека и перенаправляем на список всех людей
        peopleService.delete(id);
        return "redirect:/people";
    }
}