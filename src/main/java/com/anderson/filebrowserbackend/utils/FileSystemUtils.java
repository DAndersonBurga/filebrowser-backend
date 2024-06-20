package com.anderson.filebrowserbackend.utils;

import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
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
            if(vd.getId().equals(idDisk)) {
                return  Optional.of(vd);
            }
        }

        return Optional.empty();
    }


    public Optional<File> findFileById(File file, UUID id) {

        for (Map.Entry<String, File> stringFileEntry : file.getFiles().entrySet()) {

            if(id.toString().equals(stringFileEntry.getKey())) {
                return Optional.of(stringFileEntry.getValue());
            } else {

                if(stringFileEntry.getValue().getFileType() == FileType.DIRECTORY) {
                    return findFileById(stringFileEntry.getValue(), id);
                }

            }
        }

        return Optional.empty();
    }
}
