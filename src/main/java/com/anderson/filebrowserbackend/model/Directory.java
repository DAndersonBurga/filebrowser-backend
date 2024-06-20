package com.anderson.filebrowserbackend.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class Directory extends File {
    private int numberOfFiles;
}
