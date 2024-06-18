package com.anderson.filebrowserbackend.controller;

import com.anderson.filebrowserbackend.domain.dto.VirtualDiskRequestDto;
import com.anderson.filebrowserbackend.domain.dto.VirtualDiskSummaryResponseDto;
import com.anderson.filebrowserbackend.service.interfaces.VirtualDiskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disk")
@RequiredArgsConstructor
public class DiskController {

    private final VirtualDiskService virtualDiskService;

    @PostMapping
    public ResponseEntity<VirtualDiskSummaryResponseDto> create(@RequestBody @Valid VirtualDiskRequestDto request) {
        return ResponseEntity.ok(
                virtualDiskService.create(request)
        );
    }


}
