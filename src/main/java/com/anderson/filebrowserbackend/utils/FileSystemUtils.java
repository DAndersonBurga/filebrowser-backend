package com.anderson.filebrowserbackend.utils;

import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.response.TreeViewResponse;
import com.anderson.filebrowserbackend.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FileSystemUtils {

    private final FileSystem fileSystem;
    private final ModelMapper mapper;

    public Optional<VirtualDisk> findVirtualDisk(UUID idDisk) {

        for (VirtualDisk vd : fileSystem.getVirtualDisks()) {
            if (vd.getId().equals(idDisk)) {
                return Optional.of(vd);
            }
        }

        return Optional.empty();
    }

    public Optional<VirtualDisk> findVirtualDisk(String path) {

        for (VirtualDisk vd : fileSystem.getVirtualDisks()) {
            if (vd.getLabel().equals(path.trim())) {
                return Optional.of(vd);
            }
        }

        return Optional.empty();
    }

    public void searchFilesByName(List<FileResponse> coincidences, String query, MyFile parent) {

        for (Map.Entry<String, MyFile> stringFileEntry : parent.getFiles().entrySet()) {
            MyFile subFile = stringFileEntry.getValue();

            if (subFile.getName().contains(query)) {
                FileResponse fileResponse = mapper.map(subFile, FileResponse.class);
                coincidences.add(fileResponse);
            }

            if (subFile.getFiles() != null && !subFile.getFiles().isEmpty()) {
                searchFilesByName(coincidences, query, subFile);
            }
        }

    }

    public Optional<MyFile> findFileById(MyFile file, UUID id) {

        if(file == null) {
            return Optional.empty();
        }

        if(file.getId().equals(id)) {
            return Optional.of(file);
        }

        for (Map.Entry<String, MyFile> stringFileEntry : file.getFiles().entrySet()) {
            MyFile subFile = stringFileEntry.getValue();

            if (subFile.getId().equals(id)) {
                return Optional.of(subFile);
            }

            if (subFile.getFiles() != null && !subFile.getFiles().isEmpty()) {
                Optional<MyFile> result = findFileById(subFile, id);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    public Optional<MyFile> findParentFileById(MyFile file, UUID id) {

        if(file.getId().equals(id)) {
            return Optional.of(file);
        }

        for (Map.Entry<String, MyFile> stringFileEntry : file.getFiles().entrySet()) {
            MyFile subFile = stringFileEntry.getValue();

            if (subFile.getId().equals(id)) {
                return Optional.of(file);
            }

            if (subFile instanceof Directory) {
                Optional<MyFile> result = findParentFileById(subFile, id);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    public void updateFilesSize(VirtualDisk virtualDisk, MyFile file, double size) {

        Optional<MyFile> parentFoundOptional = findParentFileById(virtualDisk, file.getId());

        while (parentFoundOptional.isPresent()) {
            MyFile parentFound = parentFoundOptional.get();
            parentFound.setSize(parentFound.getSize() + size);

            if (parentFound instanceof VirtualDisk) {
                break;
            }

            parentFoundOptional = findParentFileById(virtualDisk, parentFound.getId());
        }

    }

    public Optional<MyFile> findDirectoryByPath(VirtualDisk virtualDisk, String[] arrPaths) {
        return findDirectoryByPath(virtualDisk.getFiles(), arrPaths, 0);
    }

    private Optional<MyFile> findDirectoryByPath(Map<String, MyFile> files, String[] arrPaths, int position) {

        for (Map.Entry<String, MyFile> stringFileEntry : files.entrySet()) {
            MyFile subFile = stringFileEntry.getValue();

            if(subFile.getFileType() == FileType.DIRECTORY) {
                if(arrPaths.length - 1 == position) {
                    return Optional.of(subFile);
                }

                return findDirectoryByPath(subFile.getFiles(), arrPaths, position+1);
            }
        }

        return Optional.empty();
    }

    public List<TreeViewResponse> treeView() {

        List<TreeViewResponse> treeViewResponses = new ArrayList<>();
        List<VirtualDisk> virtualDisks = fileSystem.getVirtualDisks();
        for (VirtualDisk virtualDisk : virtualDisks) {
            TreeViewResponse treeViewResponse = mapper.map(virtualDisk, TreeViewResponse.class);
            treeViewResponse.setChildren(new ArrayList<>());
            treeViewResponse.setName(virtualDisk.getLabel());

            Map<String, Object> metadata = generateMetadata(virtualDisk);
            treeViewResponse.setMetadata(metadata);

            treeViewResponses.add(treeViewResponse);

            buildTreeView(virtualDisk,  treeViewResponse);
        }

        return treeViewResponses;
    }

    public void buildTreeView(MyFile file, TreeViewResponse treeViewResponse) {

        for (Map.Entry<String, MyFile> stringFileEntry : file.getFiles().entrySet()) {
            MyFile subFile = stringFileEntry.getValue();

            if (subFile.getFileType() != FileType.TXT_FILE) {
                TreeViewResponse child = mapper.map(subFile, TreeViewResponse.class);
                child.setChildren(new ArrayList<>());

                Map<String, Object> metadata = generateMetadata(subFile);
                child.setMetadata(metadata);

                treeViewResponse.getChildren().add(child);

                if(subFile.getFiles() != null && !subFile.getFiles().isEmpty()) {
                    buildTreeView(subFile, child);
                }
            }

        }

    }

    private Map<String, Object> generateMetadata(MyFile file) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("diskId", file.getDiskId());
        metadata.put("parentId", file.getParentId());
        metadata.put("fileType", file.getFileType());
        metadata.put("id", file.getId());
        metadata.put("path", file.getPath());
        return metadata;
    }
}
