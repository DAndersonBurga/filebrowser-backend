package com.anderson.filebrowserbackend.controller.api;

import com.anderson.filebrowserbackend.controller.request.SearchRequest;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.service.interfaces.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final FileService fileService;

    @MessageMapping("/search")
    @SendTo("/topic/files")
    public List<FileResponse> search(@Payload @Valid SearchRequest searchRequest) {

        return fileService.search(searchRequest);
    }

}
