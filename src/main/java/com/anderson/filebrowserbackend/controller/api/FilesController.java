package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.*;
import com.anderson.filebrowserbackend.controller.response.DirectorySearchResponse;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.service.interfaces.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FilesController {

    private final FileService fileService;

    @GetMapping("/path")
    public ResponseEntity<DirectorySearchResponse> findWithPath(@RequestParam(name = "path") String path) {
        return ResponseEntity.ok(fileService.findWithPath(path));
    }

    @PostMapping("/create-folder/{id-disk}/{id-parent}")
    public ResponseEntity<FileActionResponse> createFolder(@PathVariable("id-disk") UUID idDisk,
                                                           @PathVariable("id-parent") UUID idParent,
                                                           @RequestBody @Valid CreateFolderRequest request) {
        return ResponseEntity.ok(fileService.createFolder(idDisk, idParent, request));
    }

    @PostMapping("/create-file/{id-disk}/{id-parent}")
    public ResponseEntity<FileActionResponse> createFile(@PathVariable("id-disk") UUID idDisk,
                                                         @PathVariable("id-parent") UUID idParent,
                                                         @RequestBody @Valid CreateTextFileRequest request) {
        return ResponseEntity.ok(fileService.createFile(idDisk, idParent, request));
    }

    @GetMapping("/directory/{id-disk}/{id-directory}")
    public ResponseEntity<List<FileResponse>> findDirectory(@PathVariable("id-disk") UUID idDisk,
                                                             @PathVariable("id-directory") UUID idDirectory) {

        return ResponseEntity.ok(fileService.getAllFilesInADirectory(idDisk, idDirectory));
    }

    @PutMapping("/edit/{id-disk}/{id-file}")
    public ResponseEntity<FileActionResponse> editFile(@PathVariable("id-disk") UUID idDisk,
                                                       @PathVariable("id-file") UUID idFile,
                                                       @RequestBody @Valid FileUpdateRequest fileUpdateRequest) {

        return ResponseEntity.ok(fileService.editFile(idDisk, idFile, fileUpdateRequest));
    }

    @DeleteMapping("/delete/{id-disk}/{id-parent}/{id-file}")
    public ResponseEntity<Void> deleteFile(@PathVariable("id-disk") UUID idDisk,
                                            @PathVariable("id-parent") UUID idParent,
                                            @PathVariable("id-file") UUID idFile) {

        fileService.deleteFile(idDisk, idParent, idFile);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/copy")
    public ResponseEntity<FileActionResponse> copyFile(@RequestBody @Valid FileActionRequest fileActionRequest) {

        return ResponseEntity.ok(fileService.copyFile(fileActionRequest));
    }

    @PostMapping("/cut")
    public ResponseEntity<FileActionResponse> cutFile(@RequestBody @Valid FileActionRequest fileActionRequest) {
        return ResponseEntity.ok(fileService.cutFile(fileActionRequest));
    }
}
