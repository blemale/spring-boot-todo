package io.github.blemale.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateTodoCommand {

        @NotNull
        @Size(min = 1, max = 100)
        public final String title;

        @NotNull
        @Size(min = 0, max = 1000)
        public final String body;

        @JsonCreator
        public CreateTodoCommand(
                @JsonProperty("title") String title,
                @JsonProperty("body") String body) {
                this.title = title;
                this.body = body;
        }
}
