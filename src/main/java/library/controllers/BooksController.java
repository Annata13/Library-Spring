package library.controllers;

import jakarta.validation.Valid;
import library.dao.BookDAO;
import library.dao.PersonDAO;
import library.model.Book;
import library.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public BooksController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO=bookDAO;
    }

    //вернули весь список
    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookDAO.show(id));
        Optional<Person> bookOwner  = bookDAO.getPeopleByBook(id); //получить владельца книги

        if(bookOwner.isPresent()){
            model.addAttribute("owen", bookOwner.get()); // владелец книги

        }else model.addAttribute("people", personDAO.index()); //список из всех людей для выпадающего

        return "books/show";
    }

    //назначить книгу
    @PatchMapping("{id}/assaign")
public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectPerson ){
       bookDAO.assaign(id,selectPerson);
       return "redirect:/books/" + id;
    }
//освободить книгу
@PatchMapping("{id}/release")
public String release(@PathVariable("id") int id ){
    bookDAO.release(id);
    return "redirect:/books/" + id;
}

    //создание книги
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("book") Book book){
        return "books/new";
    }
    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
          BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "books/new";
        }
        bookDAO.save(book);
        return "redirect:/books";
    }

    //изменить книгу
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id ){
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id){
        if (bindingResult.hasErrors()){
            return "books/edit";}
        bookDAO.update(id,book);
        return "redirect:/books";
    }

    //удалить книгу
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id" ) int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }

}
