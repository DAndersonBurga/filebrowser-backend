package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.service.interfaces.FileSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/file-system")
@RequiredArgsConstructor
public class
FileSystemController {

    private final FileSystemService fileSystemService;

    @GetMapping
    public ResponseEntity<List<VirtualDiskSummaryResponse>> getFileSystem() {
        return ResponseEntity.ok(fileSystemService.getVirtualDisks());
    }

}
