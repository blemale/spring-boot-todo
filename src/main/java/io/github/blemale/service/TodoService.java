package io.github.blemale.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.github.blemale.domain.exception.DoesNotExistException;
import io.github.blemale.domain.Todo;
import io.github.blemale.domain.TodoRepository;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        checkState(todoRepository != null);
        this.todoRepository = todoRepository;
    }

    public Todo findTodo(Long todoId) throws DoesNotExistException {
        return todoRepository.find(todoId);
    }

    public Set<Todo> findTodos() {
        return todoRepository.list();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Todo createTodo(Todo todo) {
        final Long generatedId = todoRepository.create(todo);
        return findTodo(generatedId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Todo updateTodo(Todo todo) {
        checkArgument(todo.getId() != null && todo.getTitle() != null && todo.getBody() != null);
        checkExistence(todo.getId());

        todoRepository.update(todo);

        return findTodo(todo.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteTodo(Long todoId) {
        checkArgument(todoId != null);
        checkExistence(todoId);

        todoRepository.deleteTodo(todoId);
    }

    private void checkExistence(Long todoId) {
        final boolean exist = todoRepository.exist(todoId);
        if (!exist) {
            throw DoesNotExistException.create(Todo.class, todoId);
        }
    }
}
