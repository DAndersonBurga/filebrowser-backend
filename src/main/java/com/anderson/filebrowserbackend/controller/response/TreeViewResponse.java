package com.anderson.filebrowserbackend.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeViewResponse {
    private String name;
    private List<TreeViewResponse> children;
    private Map<String, Object> metadata;
}
