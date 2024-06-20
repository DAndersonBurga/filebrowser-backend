package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.CreateVirtualDiskRequest;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.response.VirtualDiskSummaryResponse;
import com.anderson.filebrowserbackend.service.interfaces.VirtualDiskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/disk")
@RequiredArgsConstructor
public class DiskController {

    private final VirtualDiskService virtualDiskService;

    @GetMapping("/{id}")
    public ResponseEntity<List<FileResponse>> get(@PathVariable UUID id) {
        return ResponseEntity.ok(virtualDiskService.findFiles(id));
    }

    @PostMapping
    public ResponseEntity<VirtualDiskSummaryResponse> create(@RequestBody @Valid CreateVirtualDiskRequest request) {
        return ResponseEntity.ok(virtualDiskService.create(request));
    }
}
