package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.request.*;
import com.anderson.filebrowserbackend.controller.response.DirectorySearchResponse;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.response.TreeViewResponse;

import java.util.List;
import java.util.UUID;

public interface FileService {
    List<FileResponse> getAllFilesInADirectory(UUID idDisk, UUID idDirectory);
    FileActionResponse createFolder(UUID idDisk, UUID idParent, FolderCreateRequest request);
    FileActionResponse createFile(UUID idDisk, UUID idParent, TextFileCreateRequest request);

    FileActionResponse editFile(UUID idDisk, UUID idFile, FileUpdateRequest fileUpdateRequest);
    FileActionResponse copyFile(FileActionRequest fileActionRequest);
    FileActionResponse cutFile(FileActionRequest fileActionRequest);
    void deleteFile(UUID idDisk, UUID idParent, UUID idFile);

    List<FileResponse> search(SearchRequest searchRequest);
    DirectorySearchResponse findWithPath(String path);
    List<TreeViewResponse> treeView();
    List<FileResponse> searchQuickAccess(SearchQuickAccessRequest searchRequest);
}