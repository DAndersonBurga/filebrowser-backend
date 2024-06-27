package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.QuickAccessCreateRequest;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.service.interfaces.QuickAccessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class QuickAccessController {

    private final QuickAccessService quickAccessService;

    @GetMapping("/quickAccess")
    public ResponseEntity<List<FileResponse>> getQuickAccess() {
        return ResponseEntity.ok(quickAccessService.getQuickAccess());
    }

    @PostMapping("/quickAccess")
    public ResponseEntity<FileActionResponse> createQuickAccess(@RequestBody @Valid QuickAccessCreateRequest request) {
        return ResponseEntity.ok(quickAccessService.createQuickAccess(request));
    }
}
