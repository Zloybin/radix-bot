package com.arduino.telegrambot.feature.anki.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Options {

    private Boolean allowDuplicate;
    private String duplicateScope;
    private DuplicateScopeOptions duplicateScopeOptions;
}