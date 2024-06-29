package com.anderson.filebrowserbackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "fileType",
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextFile.class, name = "TXT_FILE"),
        @JsonSubTypes.Type(value = Directory.class, name = "DIRECTORY"),
        @JsonSubTypes.Type(value = VirtualDisk.class, name = "VIRTUAL_DISK")
})
@JsonInclude()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class File implements Serializable {
    private UUID id;
    private String name;
    private String path;
    private String description;
    private LocalDateTime creationAt;
    private LocalDateTime lastModifiedAt;
    private double size;
    private FileType fileType;
    private Map<String, File> files;
    private UUID diskId;
    private UUID parentId;
}