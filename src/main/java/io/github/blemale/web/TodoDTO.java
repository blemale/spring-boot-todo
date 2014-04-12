package io.github.blemale.web;

import org.joda.time.DateTime;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TodoDTO {

    public final Long id;
    public final String title;
    public final String body;
    public final DateTime creationDate;

    @JsonCreator
    public TodoDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("body") String body,
            @JsonProperty("creation-date") DateTime creationDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.creationDate = creationDate;
    }
}
