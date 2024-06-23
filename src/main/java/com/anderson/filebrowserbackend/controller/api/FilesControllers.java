package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.CreateFolderRequest;
import com.anderson.filebrowserbackend.controller.request.CreateTextFileRequest;
import com.anderson.filebrowserbackend.controller.request.FileCutRequest;
import com.anderson.filebrowserbackend.controller.response.FileCreatedResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.model.FileType;
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
public class FilesControllers {

    private final FileService fileService;

    @PostMapping("/create-folder/{id-disk}/{id-parent}")
    public ResponseEntity<FileCreatedResponse> createFolder(@PathVariable("id-disk") UUID idDisk,
                                                            @PathVariable("id-parent") UUID idParent,
                                                            @RequestBody @Valid CreateFolderRequest request) {
        return ResponseEntity.ok(fileService.createFolder(idDisk, idParent, request));
    }

    @PostMapping("/create-file/{id-disk}/{id-parent}")
    public ResponseEntity<FileCreatedResponse> createFile(@PathVariable("id-disk") UUID idDisk,
                                                          @PathVariable("id-parent") UUID idParent,
                                                          @RequestBody @Valid CreateTextFileRequest request) {
        return ResponseEntity.ok(fileService.createFile(idDisk, idParent, request));
    }

    @GetMapping("/{id-disk}/{id-parent}/{id-file}")
    public ResponseEntity<FileCreatedResponse> copyElement(@PathVariable("id-disk") UUID idDisk,
                                         @PathVariable("id-parent") UUID idParent,
                                         @PathVariable("id-file") UUID idFile) {

        return ResponseEntity.ok(fileService.copyFile(idDisk, idParent, idFile));
    }

    @GetMapping("/directory/{id-disk}/{id-directory}")
    public ResponseEntity<List<FileResponse>> findDirectory(@PathVariable("id-disk") UUID idDisk,
                                                             @PathVariable("id-directory") UUID idDirectory) {

        return ResponseEntity.ok(fileService.getAllFilesInADirectory(idDisk, idDirectory));
    }

    @DeleteMapping("/delete/{id-disk}/{id-parent}/{id-file}")
    public ResponseEntity<Void> deleteFileById(@PathVariable("id-disk") UUID idDisk,
                                            @PathVariable("id-parent") UUID idParent,
                                            @PathVariable("id-file") UUID idFile) {
        fileService.deleteFile(idDisk, idParent, idFile);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cut")
    public ResponseEntity<Void> cutFile(@RequestBody @Valid FileCutRequest fileCutRequest) {
        fileService.cutFile(fileCutRequest);
        return ResponseEntity.ok().build();
    }

}
