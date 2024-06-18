package com.anderson.filebrowserbackend.domain.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class VirtualDisk extends File {
    private String label;

    private Map<UUID, Object> files;
}
