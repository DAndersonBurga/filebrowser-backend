package com.anderson.filebrowserbackend.utils;

import com.anderson.filebrowserbackend.model.File;
import com.anderson.filebrowserbackend.model.FileSystem;
import com.anderson.filebrowserbackend.model.FileType;
import com.anderson.filebrowserbackend.model.VirtualDisk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileSystemUtils {

    private final FileSystem fileSystem;

    public Optional<VirtualDisk> findVirtualDisk(UUID idDisk) {

        for (VirtualDisk vd : fileSystem.getVirtualDisks()) {
            if (vd.getId().equals(idDisk)) {
                return Optional.of(vd);
            }
        }

        return Optional.empty();
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
