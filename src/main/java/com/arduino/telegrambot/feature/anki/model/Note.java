package com.arduino.telegrambot.feature.anki.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Note {

    @NonNull
    private String deckName;

    @NonNull
    private String modelName;

    @Singular("field")
    private Map<String, String> fields;

    private Options options;

    @Singular("tag")
    private List<String> tags;

    @Singular("addAudio")
    private List<Media> audio;

    @Singular("addVideo")
    private List<Media> video;

    @Singular("addPicture")
    private List<Media> picture;
}
