package gov.max.microservice.resource.dao;

import gov.max.microservice.resource.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class TodoRepository {

    private static final Map<Long, Todo> REPOSITORY = new ConcurrentSkipListMap<>();
    private static final AtomicLong IDS = new AtomicLong(0);

    private static final TodoRepository INSTANCE = new TodoRepository();

    static {
        INSTANCE.create(new Todo(1L, "Learn Spring Cloud", "Awesome tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state). Coordination of distributed systems leads to boiler plate patterns, and using Spring Cloud developers can quickly stand up services and applications that implement those patterns. They will work well in any distributed environment, including the developer's own laptop, bare metal data centres, and managed platforms such as Cloud Foundry.."));
        INSTANCE.create(new Todo(2L, "Learn AngularJS", "HTML is great for declaring static documents, but it falters when we try to use it for declaring dynamic views in web-applications. AngularJS lets you extend HTML vocabulary for your application. The resulting environment is extraordinarily expressive, readable, and quick to develop. "));
        INSTANCE.create(new Todo(3L, "Integrate with MAX", "The Office of Management and Budget uses the MAX Information System to collect, validate, analyze, model, collaborate with agencies on, and publish information relating to its government-wide management and budgeting activities. Perhaps the most visible end product of the MAX system is the \"Budget of the United States Government,\" also known as the President's Budget, which is produced each February."));
    }

    public static TodoRepository getInstance() {
        return INSTANCE;
    }

    private TodoRepository() {
    }

    public List<Todo> list() {
        return new ArrayList<>(REPOSITORY.values());
    }

    public Todo get(Long id) {
        return REPOSITORY.get(id);
    }

    public void create(Todo todo) {
        long id = IDS.getAndIncrement();
        todo.setId(id);
        REPOSITORY.put(id, todo);
    }

    public void update(Todo todo) {
        REPOSITORY.put(todo.getId(), todo);
    }

    public boolean delete(Long id) {
        return REPOSITORY.remove(id) != null;
    }
}
