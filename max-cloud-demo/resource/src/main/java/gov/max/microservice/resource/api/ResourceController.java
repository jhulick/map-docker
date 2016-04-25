package gov.max.microservice.resource.api;

import gov.max.microservice.resource.dao.TodoRepository;
import gov.max.microservice.resource.model.Todo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResourceController {

    @RequestMapping(value = "/todos", method = RequestMethod.GET, produces = "application/json")
    public List<Todo> todos() {
        return TodoRepository.getInstance().list();
    }

    @RequestMapping(value = "/todos/{id}", method = RequestMethod.GET, produces = "application/json")
    public Todo todo(@PathVariable Long id) {
        return TodoRepository.getInstance().get(id);
    }

    @RequestMapping(value = "/todos", method = RequestMethod.POST)
    public void save(@ModelAttribute Todo todo) {
        TodoRepository.getInstance().create(todo);
    }

    @RequestMapping(value = "/todos/{id}", method = RequestMethod.PUT)
    public void update(@ModelAttribute Todo todo) {
        TodoRepository.getInstance().update(todo);
    }

    @RequestMapping(value = "/todos/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        TodoRepository.getInstance().delete(id);
    }
}
