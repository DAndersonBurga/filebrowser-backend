package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.request.FileSystemCreateRequest;
import com.anderson.filebrowserbackend.controller.response.CreatedFileSystemResponse;
import com.anderson.filebrowserbackend.controller.response.FileSystemUploadResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.model.FileSystem;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileSystemService {
    List<VirtualDiskSummaryResponse> getVirtualDisks();
    CreatedFileSystemResponse create(FileSystemCreateRequest request);
    FileSystemUploadResponse upload(MultipartFile multipartFile);
    ByteArrayResource export();
}
