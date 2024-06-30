package com.anderson.filebrowserbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileSystem implements Serializable {
    private String name;
    List<VirtualDisk> virtualDisks = new ArrayList<>();
    List<MyFile> quickAccessList = new ArrayList<>();
}