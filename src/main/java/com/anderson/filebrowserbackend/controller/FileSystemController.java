package com.anderson.filebrowserbackend.controller;

import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file-system")
@RequiredArgsConstructor
public class FileSystemController {

    private final FileSystemService fileSystemService;

    @GetMapping
    public ResponseEntity<?> getFileSystem() {
        return ResponseEntity.ok(
                fileSystemService.getVirtualDisks()
        );
    }

}
