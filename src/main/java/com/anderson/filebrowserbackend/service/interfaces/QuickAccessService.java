package com.anderson.filebrowserbackend.service.interfaces;

import com.anderson.filebrowserbackend.controller.request.QuickAccessCreateRequest;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;

import java.util.List;

public interface QuickAccessService {
    FileActionResponse createQuickAccess(QuickAccessCreateRequest request);
    List<FileResponse> getQuickAccess();
}
