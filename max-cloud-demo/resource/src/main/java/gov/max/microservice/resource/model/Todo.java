package gov.max.microservice.resource.model;

import org.springframework.stereotype.Component;

@Component
public class Todo {

    private Long id;
    private String title;
    private String description;
    private boolean done;

    public Todo() {
    }

    public Todo(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id.equals(todo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}