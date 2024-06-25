package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.request.*;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;

import java.util.List;
import java.util.UUID;

public interface FileService {
    List<FileResponse> getAllFilesInADirectory(UUID idDisk, UUID idDirectory);
    FileActionResponse createFolder(UUID idDisk, UUID idParent, CreateFolderRequest request);
    FileActionResponse createFile(UUID idDisk, UUID idParent, CreateTextFileRequest request);

    FileActionResponse editFile(UUID idDisk, UUID idFile, FileUpdateRequest fileUpdateRequest);
    FileActionResponse copyFile(FileActionRequest fileActionRequest);
    FileActionResponse cutFile(FileActionRequest fileActionRequest);
    void deleteFile(UUID idDisk, UUID idParent, UUID idFile);

    List<FileResponse> search(SearchRequest searchRequest);

}
