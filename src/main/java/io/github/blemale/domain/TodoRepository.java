package io.github.blemale.domain;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static io.github.blemale.domain.Todo.BODY;
import static io.github.blemale.domain.Todo.CREATION_DATE;
import static io.github.blemale.domain.Todo.ID;
import static io.github.blemale.domain.Todo.TITLE;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.blemale.domain.exception.DoesNotExistException;

@Repository
public class TodoRepository {

    public static final String SELECT_BY_ID = "SELECT * FROM Todo WHERE id = :id;";
    public static final String SELECT_ALL = "SELECT * FROM Todo";
    public static final String INSERT = "INSERT INTO Todo (title, body, creation_date) VALUES (:title, :body, :creation_date)";
    public static final String UPDATE = "UPDATE Todo SET title = :title, body = :body WHERE id = :id";
    public static final String DELETE = "DELETE FROM Todo WHERE id = :id";
    public static final String EXIST = "SELECT COUNT(1) FROM Todo WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Todo> todoRowMapper = new RowMapper<Todo>() {
        @Override
        public Todo mapRow(ResultSet resultSet, int i) throws SQLException {
            return buildTodo(resultSet);
        }
    };

    @Autowired
    public TodoRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        checkState(jdbcTemplate != null);
        this.jdbcTemplate = jdbcTemplate;
    }

    private Todo buildTodo(ResultSet resultSet) throws SQLException {
        return new Todo(
                resultSet.getLong(ID),
                resultSet.getString(TITLE),
                resultSet.getString(BODY),
                new DateTime(resultSet.getDate(CREATION_DATE).getTime()));
    }

    public Todo find(Long todoId) {
        final Map<String, Object> params = newHashMap();
        params.put(ID, todoId);

        final List<Todo> todos = jdbcTemplate.query(SELECT_BY_ID, params, todoRowMapper);

        if(todos.isEmpty()) {
            throw DoesNotExistException.create(Todo.class, todoId);
        } else {
            return todos.get(0);
        }
    }

    public Set<Todo> list() {
        final List<Todo> todos = jdbcTemplate.query(SELECT_ALL, todoRowMapper);
        return newHashSet(todos);
    }

    public Long create(Todo todo) {
        final DateTime now = DateTime.now();

        final Map<String, Object> params = newHashMap();
        params.put(TITLE, todo.getTitle());
        params.put(BODY, todo.getBody());
        params.put(CREATION_DATE, now.toDate());
        final SqlParameterSource parameterSource = new MapSqlParameterSource(params);

        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(INSERT, parameterSource, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void update(Todo todo) {
        final Map<String, Object> params = newHashMap();
        params.put(ID, todo.getId());
        params.put(TITLE, todo.getTitle());
        params.put(BODY, todo.getBody());

        jdbcTemplate.update(UPDATE, params);
    }

    public void deleteTodo(Long todoId) {
        final Map<String, Object> params = newHashMap();
        params.put(ID, todoId);
        jdbcTemplate.update(DELETE, params);
    }

    public boolean exist(Long todoId) {
        final Map<String, Object> params = newHashMap();
        params.put(ID, todoId);

        return jdbcTemplate.queryForObject(EXIST, params, Boolean.class);

    }
}
