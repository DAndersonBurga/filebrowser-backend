package com.anderson.filebrowserbackend.utils;

import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

    public void searchFilesByName(List<FileResponse> coincidences, String query, File parent, VirtualDisk virtualDisk) {

        for (Map.Entry<String, File> stringFileEntry : parent.getFiles().entrySet()) {
            File subFile = stringFileEntry.getValue();

            if (subFile.getName().contains(query)) {
                FileResponse fileResponse = mapper.map(subFile, FileResponse.class);

                fileResponse.setPath(virtualDisk.getPath() + subFile.getPath());

                coincidences.add(fileResponse);
            }

            if (subFile.getFiles() != null && !subFile.getFiles().isEmpty()) {
                searchFilesByName(coincidences, query, subFile, virtualDisk);
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
}
