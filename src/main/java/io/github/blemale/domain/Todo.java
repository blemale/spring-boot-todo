package io.github.blemale.domain;

import java.util.Objects;
import org.joda.time.DateTime;

public class Todo {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String CREATION_DATE = "creation_date";

    private final Long id;
    private final String title;
    private final String body;
    private final DateTime creationDate;

    public Todo(Long id, String title, String body, DateTime creationDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Todo todo = (Todo) o;

        return Objects.equals(this.id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects
                .toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
