package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.request.CreateFolderRequest;
import com.anderson.filebrowserbackend.controller.request.CreateTextFileRequest;
import com.anderson.filebrowserbackend.controller.request.FileCutRequest;
import com.anderson.filebrowserbackend.controller.response.FileCreatedResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.model.FileType;

import java.util.List;
import java.util.UUID;

public interface FileService {
    FileCreatedResponse createFolder(UUID idDisk, UUID idParent, CreateFolderRequest request);
    FileCreatedResponse createFile(UUID idDisk, UUID idParent, CreateTextFileRequest request);

    List<FileResponse> getAllFilesInADirectory(UUID idDisk, UUID idDirectory);
    FileCreatedResponse copyFile(UUID idDisk, UUID idParent, UUID idFile);
    void deleteFile(UUID idDisk, UUID idParent, UUID idFile);

    void cutFile(FileCutRequest fileCutRequest);
}
