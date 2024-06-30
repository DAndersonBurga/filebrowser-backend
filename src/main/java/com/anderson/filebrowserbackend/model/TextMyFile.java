package com.anderson.filebrowserbackend.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;


@JsonTypeName("TXT_FILE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class TextMyFile extends MyFile {
    private String content;
}
