package com.arduino.telegrambot.feature.anki.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Media {

    private String url;
    private String path;
    private String data;

    @NonNull
    private String filename;

    private Boolean skipHash;
    private Boolean deleteExisting;

    @Singular("targetField")
    private List<String> fields;
}
