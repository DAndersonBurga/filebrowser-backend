package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.request.CreateFolderRequest;
import com.anderson.filebrowserbackend.controller.request.CreateTextFileRequest;
import com.anderson.filebrowserbackend.controller.request.FileActionRequest;
import com.anderson.filebrowserbackend.controller.request.SearchRequest;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;

import java.util.List;
import java.util.UUID;

public interface FileService {
    FileActionResponse createFolder(UUID idDisk, UUID idParent, CreateFolderRequest request);
    FileActionResponse createFile(UUID idDisk, UUID idParent, CreateTextFileRequest request);

    List<FileResponse> getAllFilesInADirectory(UUID idDisk, UUID idDirectory);
    void deleteFile(UUID idDisk, UUID idParent, UUID idFile);

    FileActionResponse copyFile(FileActionRequest fileActionRequest);
    FileActionResponse cutFile(FileActionRequest fileActionRequest);

    List<FileResponse> search(SearchRequest searchRequest);
}
