package com.anderson.filebrowserbackend.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonTypeName("DIRECTORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class Directory extends File {
    private int numberOfFiles;
}
