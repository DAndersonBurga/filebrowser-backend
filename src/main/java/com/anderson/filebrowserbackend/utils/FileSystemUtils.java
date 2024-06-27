package com.anderson.filebrowserbackend.utils;

import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public void searchFilesByName(List<FileResponse> coincidences, String query, File parent) {

        for (Map.Entry<String, File> stringFileEntry : parent.getFiles().entrySet()) {
            File subFile = stringFileEntry.getValue();

            if (subFile.getName().contains(query)) {
                FileResponse fileResponse = mapper.map(subFile, FileResponse.class);
                coincidences.add(fileResponse);
            }

            if (subFile.getFiles() != null && !subFile.getFiles().isEmpty()) {
                searchFilesByName(coincidences, query, subFile);
            }
        }

    }

    public Optional<File> findFileById(File file, UUID id) {

        if(file == null) {
            return Optional.empty();
        }

        if(file.getId().equals(id)) {
            return Optional.of(file);
        }

        for (Map.Entry<String, File> stringFileEntry : file.getFiles().entrySet()) {
            File subFile = stringFileEntry.getValue();

            if (subFile.getId().equals(id)) {
                return Optional.of(subFile);
            }

            if (subFile.getFiles() != null && !subFile.getFiles().isEmpty()) {
                Optional<File> result = findFileById(subFile, id);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    public Optional<File> findParentFileById(File file, UUID id) {

        if(file.getId().equals(id)) {
            return Optional.of(file);
        }

        for (Map.Entry<String, File> stringFileEntry : file.getFiles().entrySet()) {
            File subFile = stringFileEntry.getValue();

            if (subFile.getId().equals(id)) {
                return Optional.of(file);
            }

            if (subFile instanceof Directory) {
                Optional<File> result = findParentFileById(subFile, id);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    public void updateFilesSize(VirtualDisk virtualDisk, File file, double size) {

        Optional<File> parentFoundOptional = findParentFileById(virtualDisk, file.getId());

        while (parentFoundOptional.isPresent()) {
            File parentFound = parentFoundOptional.get();
            parentFound.setSize(parentFound.getSize() + size);

            if (parentFound instanceof VirtualDisk) {
                break;
            }

            parentFoundOptional = findParentFileById(virtualDisk, parentFound.getId());
        }

    }

    public Optional<File> findDirectoryByPath(VirtualDisk virtualDisk, String[] arrPaths) {
        return findDirectoryByPath(virtualDisk.getFiles(), arrPaths, 0);
    }

    private Optional<File> findDirectoryByPath(Map<String, File> files, String[] arrPaths, int position) {

        for (Map.Entry<String, File> stringFileEntry : files.entrySet()) {
            File subFile = stringFileEntry.getValue();

            if(subFile.getFileType() == FileType.DIRECTORY) {
                if(arrPaths.length - 1 == position) {
                    return Optional.of(subFile);
                }

                return findDirectoryByPath(subFile.getFiles(), arrPaths, position+1);
            }
        }

        return Optional.empty();
    }
}
