package io.github.blemale.service;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;
import java.util.Set;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import io.github.blemale.domain.Todo;
import io.github.blemale.domain.TodoRepository;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void should_find_todo_by_id() {
        // Given
        Todo todo = new Todo(1L, "title", "body", DateTime.now());
        willReturn(todo).given(todoRepository).find(1L);

        // When
        Todo actualTodo = todoService.findTodo(1L);

        // Then
        assertThat(actualTodo).isEqualToComparingFieldByField(todo);
    }

    @Test
    public void should_find_all_todos() {
        // Given
        Todo todo = new Todo(1L, "title", "body", DateTime.now());
        willReturn(newHashSet(todo)).given(todoRepository).list();

        // When
        Set<Todo> todos = todoService.findTodos();

        // Then
        assertThat(todos).containsOnly(todo);
    }

    @Test
    public void should_create_a_todo() {
        // Given
        Todo todo = new Todo(1L, "title", "body", DateTime.now());
        willReturn(1L).given(todoRepository).create(todo);
        willReturn(todo).given(todoRepository).find(1L);

        // When
        Todo createdTodo = todoService.createTodo(todo);

        // Then
        assertThat(createdTodo).isEqualToComparingFieldByField(todo);
        verify(todoRepository).create(todo);
    }

    @Test
    public void should_update_todo() {
        // Given
        Todo todo = new Todo(1L, "title", "body", DateTime.now());
        willReturn(true).given(todoRepository).exist(1L);
        willReturn(todo).given(todoRepository).find(1L);

        // When
        Todo updatedTodo = todoService.updateTodo(todo);

        // Then
        assertThat(updatedTodo).isEqualToComparingFieldByField(todo);
        verify(todoRepository).update(todo);
    }

    @Test
    public void should_delete_todo() {
        // Given
        Todo todo = new Todo(1L, "title", "body", DateTime.now());
        willReturn(true).given(todoRepository).exist(1L);

        // When
        todoService.deleteTodo(1L);

        // Then
        verify(todoRepository).deleteTodo(1L);
    }
}
