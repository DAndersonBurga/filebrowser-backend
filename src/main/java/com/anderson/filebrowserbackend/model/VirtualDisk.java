package com.anderson.filebrowserbackend.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

@JsonTypeName("VIRTUAL_DISK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class VirtualDisk extends File {
    private String label;
}
