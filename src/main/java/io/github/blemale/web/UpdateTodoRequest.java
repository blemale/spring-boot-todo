package io.github.blemale.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateTodoRequest {

        @NotNull
        public final Long id;

        @NotNull
        @Size(min = 1, max = 100)
        public final String title;

        @NotNull
        @Size(min = 0, max = 1000)
        public final String body;

        @JsonCreator
        public UpdateTodoRequest(
                @JsonProperty("id") Long id,
                @JsonProperty("title") String title,
                @JsonProperty("body") String body) {
                this.id = id;
                this.title = title;
                this.body = body;
        }
}
