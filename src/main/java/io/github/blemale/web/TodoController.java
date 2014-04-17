package io.github.blemale.web;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import io.github.blemale.domain.exception.DoesNotExistException;
import io.github.blemale.domain.Todo;
import io.github.blemale.service.TodoService;

@RestController
@RequestMapping(value = "/todos")
public class TodoController {

    private static final String JSON = "application/json";

    private final TodoService todoService;

    private final Function<Todo, TodoDTO> todoToDTOMapper = new Function<Todo, TodoDTO>() {
        @Override
        public TodoDTO apply(Todo todo) {
            return new TodoDTO(todo.getId(), todo.getTitle(), todo.getBody(), todo.getCreationDate());
        }
    };

    private final Function<CreateTodoRequest, Todo> createRequestToTodoMapper = new Function<CreateTodoRequest, Todo>() {
        @Override
        public Todo apply(CreateTodoRequest createTodoRequest) {
            return new Todo(null, createTodoRequest.title, createTodoRequest.body, null);
        }
    };

    private final Function<UpdateTodoRequest, Todo> updateRequestToTodoMapper = new Function<UpdateTodoRequest, Todo>() {
        @Override
        public Todo apply(UpdateTodoRequest updateTodoRequest) {
            return new Todo(updateTodoRequest.id, updateTodoRequest.title, updateTodoRequest.body, null);
        }
    };

    @Autowired
    public TodoController(TodoService todoService) {
        checkState(todoService != null);
        this.todoService = todoService;
    }

    @RequestMapping(value = "/{todoId}", method = GET, produces = JSON)
    public TodoDTO getTodo(@PathVariable Long todoId) {
        final Todo todo = todoService.findTodo(todoId);
        return todoToDTOMapper.apply(todo);
    }

    @RequestMapping(method = GET, produces = JSON)
    public Set<TodoDTO> getTodos() {
        final Set<Todo> todos = todoService.findTodos();
        return FluentIterable.from(todos).transform(todoToDTOMapper).toSet();
    }

    @RequestMapping(method = POST, consumes = JSON, produces = JSON)
    public TodoDTO createTodo(@RequestBody @Valid CreateTodoRequest createTodoRequest) {
        final Todo todoToCreate = createRequestToTodoMapper.apply(createTodoRequest);
        final Todo createdTodo = todoService.createTodo(todoToCreate);
        return todoToDTOMapper.apply(createdTodo);
    }

    @RequestMapping(value = "/{todoId}", method = PUT, consumes = JSON, produces = JSON)
    public TodoDTO updateTodo(@PathVariable Long todoId, @RequestBody @Valid UpdateTodoRequest updateTodoRequest) {
        checkArgument(todoId.equals(updateTodoRequest.id));

        final Todo todoToUpdate = updateRequestToTodoMapper.apply(updateTodoRequest);
        final Todo updatedTodo = todoService.updateTodo(todoToUpdate);
        return todoToDTOMapper.apply(updatedTodo);
    }

    @RequestMapping(value = "/{todoId}", method = DELETE)
    public void deleteTodo(@PathVariable Long todoId) throws DoesNotExistException {
        todoService.deleteTodo(todoId);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DoesNotExistException.class)
    public void handleDoesNotExist() {
    }
}
