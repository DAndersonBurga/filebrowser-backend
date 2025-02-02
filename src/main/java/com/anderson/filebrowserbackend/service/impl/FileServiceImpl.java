package com.anderson.filebrowserbackend.service.impl;

import com.anderson.filebrowserbackend.controller.request.*;
import com.anderson.filebrowserbackend.controller.response.DirectorySearchResponse;
import com.anderson.filebrowserbackend.controller.response.FileActionResponse;
import com.anderson.filebrowserbackend.controller.response.FileResponse;
import com.anderson.filebrowserbackend.controller.response.TreeViewResponse;
import com.anderson.filebrowserbackend.error.exceptions.DuplicateFileNameException;
import com.anderson.filebrowserbackend.error.exceptions.FileNotFoundException;
import com.anderson.filebrowserbackend.error.exceptions.FileSourceInvalidException;
import com.anderson.filebrowserbackend.error.exceptions.VirtualDiskNotFoundException;
import com.anderson.filebrowserbackend.model.*;
import com.anderson.filebrowserbackend.service.interfaces.FileService;
import com.anderson.filebrowserbackend.utils.FileSystemUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ModelMapper mapper;
    private final FileSystemUtils fileSystemUtils;
    private final FileSystem fileSystem;

    @Override
    public FileActionResponse createFolder(UUID idDisk, UUID idParent, FolderCreateRequest request) {

        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(virtualDisk.getId().equals(idParent)) {
            return createFolderInParent(request, virtualDisk, virtualDisk);
        }

        Directory parent = (Directory) fileSystemUtils.findFileById(virtualDisk, idParent)
                .orElseThrow(() -> new FileNotFoundException("Parent file not found"));

        return createFolderInParent(request, parent, virtualDisk);
    }

    @Override
    public FileActionResponse createFile(UUID idDisk, UUID idParent, TextFileCreateRequest request) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(virtualDisk.getId().equals(idParent)) {
            return createTextFileInParent(request, virtualDisk, virtualDisk);
        }

        Directory parent = (Directory) fileSystemUtils.findFileById(virtualDisk, idParent)
                .orElseThrow(() -> new FileNotFoundException("Parent file not found"));

        return createTextFileInParent(request, parent, virtualDisk);
    }

    @Override
    public FileActionResponse editFile(UUID idDisk, UUID idFile, FileUpdateRequest fileUpdateRequest) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        MyFile parentFile = fileSystemUtils.findParentFileById(virtualDisk, idFile)
                .orElseThrow(() -> new FileNotFoundException("Parent File not found"));

        MyFile file = parentFile.getFiles().get(idFile.toString());

        if(fileSystemUtils.verifyUniqueFileName(parentFile, MyFile.builder()
                .name(fileUpdateRequest.getName())
                .fileType(file.getFileType())
                .build())) {
            throw new DuplicateFileNameException("Ya existe un archivo con ese nombre!");
        }

        // Update size in parent
        parentFile.setSize(parentFile.getSize() - file.getSize());

        file.setName(fileUpdateRequest.getName());
        file.setDescription(fileUpdateRequest.getDescription());
        file.setLastModifiedAt(LocalDateTime.now());

        if(file.getFileType() == FileType.TXT_FILE) {
            TextMyFile textFile = (TextMyFile) file;
            textFile.setContent(fileUpdateRequest.getContent());
        }

        double size = ((double) file.getName().getBytes().length / 1024) +
                ((double) file.getDescription().getBytes().length / 1024);

        double difference = size - file.getSize();

        file.setSize(size);

        updateQuickAccess(file, fileUpdateRequest);

        // Update size in parent after update
        parentFile.setSize(parentFile.getSize() + difference);
        fileSystemUtils.updateFilesSize(virtualDisk, parentFile, difference);

        return new FileActionResponse("File updated", file.getFileType());
    }

    @Override
    public List<FileResponse> getAllFilesInADirectory(UUID idDisk, UUID idDirectory) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        Directory directory = (Directory) fileSystemUtils.findFileById(virtualDisk, idDirectory)
                .orElseThrow(() -> new FileNotFoundException("Directory not found"));

        List<FileResponse> fileResponses = new ArrayList<>();

        for (Map.Entry<String, MyFile> stringFileEntry : directory.getFiles().entrySet()) {
            FileResponse fileResponse = mapper.map(stringFileEntry.getValue(), FileResponse.class);
            fileResponses.add(fileResponse);
        }

        return fileResponses;
    }

    @Override
    public FileActionResponse copyFile(FileActionRequest fileActionRequest) {
        VirtualDisk sourceVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getSourceDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        MyFile file = fileSystemUtils.findFileById(sourceVirtualDisk, fileActionRequest.getFileId())
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        MyFile newFile = SerializationUtils.clone(file);

        VirtualDisk destinationVirtualDisk;

        if(fileActionRequest.getSourceDiskId() == fileActionRequest.getDestinationDiskId()) {
            destinationVirtualDisk = sourceVirtualDisk;
        } else {
            destinationVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getDestinationDiskId())
                    .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));
        }

        return copyFileInParent(destinationVirtualDisk, destinationVirtualDisk.getId(), fileActionRequest.getDestinationParentId(), newFile);
    }

    @Override
    public void deleteFile(UUID idDisk, UUID idParent, UUID idFile) {
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(idDisk)
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(idDisk.equals(idParent)) {
            MyFile file = virtualDisk.getFiles().get(idFile.toString());
            virtualDisk.getFiles().remove(idFile.toString());

            fileSystemUtils.updateFilesSize(virtualDisk, file, (file.getSize() * -1));
        } else {
            MyFile parent = fileSystemUtils.findFileById(virtualDisk, idParent)
                    .orElseThrow(() -> new FileNotFoundException("Parent directory not found"));

            MyFile file = parent.getFiles().get(idFile.toString());
            parent.setSize(parent.getSize() - file.getSize());

            List<MyFile> quickAccessList = fileSystem.getQuickAccessList();
            for (MyFile quickAccess : quickAccessList) {
                if(quickAccess.getId().equals(file.getId())) {
                    quickAccessList.remove(file);
                    break;
                }
            }

            parent.getFiles().remove(idFile.toString());
            fileSystemUtils.updateFilesSize(virtualDisk, parent, (file.getSize() * -1));
        }
    }

    @Override
    public FileActionResponse cutFile(FileActionRequest fileActionRequest) {
        if(fileActionRequest.getDestinationDiskId().equals(fileActionRequest.getSourceDiskId()) &&
            fileActionRequest.getDestinationParentId().equals(fileActionRequest.getSourceParentId())) {
            throw new FileSourceInvalidException("El origen y el destino son los mismos");
        }

        if(fileActionRequest.getDestinationParentId().equals(fileActionRequest.getFileId())) {
            throw new FileSourceInvalidException("Origen y destino no permitidos");
        }

        VirtualDisk sourceVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getSourceDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        MyFile file = fileSystemUtils.findFileById(sourceVirtualDisk, fileActionRequest.getFileId())
                .orElseThrow(() -> new FileNotFoundException("File not found"));

        MyFile newFile = SerializationUtils.clone(file);

        // Delete file in the source
        deleteFile(fileActionRequest.getSourceDiskId(),
                fileActionRequest.getSourceParentId(),
                fileActionRequest.getFileId());

        // Insert file in destination
        VirtualDisk destinationVirtualDisk = fileSystemUtils.findVirtualDisk(fileActionRequest.getDestinationDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(fileActionRequest.getDestinationDiskId().equals(fileActionRequest.getDestinationParentId())) {
            if(fileSystemUtils.verifyUniqueFileName(destinationVirtualDisk, newFile)) {
                throw new DuplicateFileNameException("Ya existe un archivo con ese nombre!");
            }

            if(newFile.getFileType() == FileType.TXT_FILE) {
                newFile.setPath(destinationVirtualDisk.getPath());
            } else {
                newFile.setPath(destinationVirtualDisk.getPath() + "/" + newFile.getName());
            }

            newFile.setDiskId(destinationVirtualDisk.getId());
            newFile.setParentId(destinationVirtualDisk.getId());

            destinationVirtualDisk.setSize(destinationVirtualDisk.getSize() + newFile.getSize());
            destinationVirtualDisk.getFiles().put(newFile.getId().toString(), newFile);
            updateQuickAccess(newFile);

            return new FileActionResponse("Archivo cortado correctamente!", newFile.getFileType());
        }

        Directory parent = (Directory) fileSystemUtils.findFileById(
                    destinationVirtualDisk, fileActionRequest.getDestinationParentId()
                )
            .orElseThrow(() -> new FileNotFoundException("Parent directory not found"));

        if(fileSystemUtils.verifyUniqueFileName(parent, newFile)) {
            throw new DuplicateFileNameException("Ya existe un archivo con ese nombre!");
        }

        newFile.setDiskId(destinationVirtualDisk.getId());
        newFile.setParentId(parent.getId());

        if(newFile.getFileType() == FileType.TXT_FILE) {
            newFile.setPath(parent.getPath());
        } else {
            newFile.setPath(parent.getPath() + "/" + newFile.getName());
        }

        parent.setSize(parent.getSize() + newFile.getSize());
        parent.getFiles().put(newFile.getId().toString(), newFile);
        fileSystemUtils.updateFilesSize(destinationVirtualDisk, parent, file.getSize());

        return new FileActionResponse("Archivo cortado correctamente!", newFile.getFileType());

    }

    @Override
    public List<FileResponse> search(SearchRequest searchRequest) {
        List<FileResponse> coincidences = new ArrayList<>();

        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(searchRequest.getDiskId())
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual disk not found"));

        if(searchRequest.getDiskId().equals(searchRequest.getParentId())) {
            fileSystemUtils.searchFilesByName(coincidences, searchRequest.getQuery(), virtualDisk);
            return coincidences;
        }

        MyFile parent = fileSystemUtils.findFileById(virtualDisk, searchRequest.getParentId())
                .orElseThrow(() -> new FileNotFoundException("Parent file not found"));

        fileSystemUtils.searchFilesByName(coincidences, searchRequest.getQuery(), parent);


        return coincidences;
    }

    @Override
    public DirectorySearchResponse findWithPath(String path) {
        String[] arrNames = path.split("/");

        // position 0 : "", position 1: "D" label disk
        VirtualDisk virtualDisk = fileSystemUtils.findVirtualDisk(arrNames[1])
                .orElseThrow(() -> new VirtualDiskNotFoundException("Virtual Disk Not Found"));

        System.out.println(virtualDisk.getName());

        String[] newArrNames = Arrays.copyOfRange(arrNames,2, arrNames.length);
        System.out.println(Arrays.toString(newArrNames));

        if(newArrNames.length == 0) {
            return new DirectorySearchResponse(virtualDisk.getId(), null);
        }

        MyFile directoryFound = fileSystemUtils.findDirectoryByPath(virtualDisk, newArrNames)
                .orElseThrow(() -> new FileNotFoundException("Directory not found"));


        return new DirectorySearchResponse(virtualDisk.getId(), directoryFound.getId());
    }

    @Override
    public List<TreeViewResponse> treeView() {
        return fileSystemUtils.treeView();
    }

    @Override
    public List<FileResponse> searchQuickAccess(SearchQuickAccessRequest searchRequest) {
        List<FileResponse> coincidences = new ArrayList<>();

        List<MyFile> quickAccessList = fileSystem.getQuickAccessList();
        for (MyFile file : quickAccessList) {
            if(file.getName().contains(searchRequest.getQuery())) {
                FileResponse fileResponse = mapper.map(file, FileResponse.class);
                coincidences.add(fileResponse);
            }
        }

        return coincidences;
    }

    private FileActionResponse createTextFileInParent(TextFileCreateRequest request, MyFile parent, VirtualDisk virtualDisk) {
        TextMyFile file = mapper.map(request, TextMyFile.class);

        if(fileSystemUtils.verifyUniqueFileName(parent, file)) {
            throw new DuplicateFileNameException("Ya existe un archivo con ese nombre!");
        }

        file.setCreationAt(LocalDateTime.now());
        file.setId(UUID.randomUUID());
        file.setPath(parent.getPath());
        file.setFileType(FileType.TXT_FILE);
        file.setFiles(new HashMap<>());
        file.setDiskId(virtualDisk.getId());
        file.setParentId(parent.getId());

        double size = ((double) file.getName().getBytes().length / 1024) +
                ((double) file.getContent().getBytes().length / 1024);

        file.setSize(size);

        parent.getFiles().put(file.getId().toString(), file);
        fileSystemUtils.updateFilesSize(virtualDisk, file, size);

        return new FileActionResponse("File created", file.getFileType());
    }

    private FileActionResponse createFolderInParent(FolderCreateRequest request, MyFile parent, VirtualDisk virtualDisk) {

        Directory folder = mapper.map(request, Directory.class);

        if(fileSystemUtils.verifyUniqueFileName(parent, folder)) {
            System.out.println(folder.getName());
            throw new DuplicateFileNameException("Ya existe un archivo con ese nombre!");
        }

        folder.setCreationAt(LocalDateTime.now());
        folder.setId(UUID.randomUUID());
        folder.setDiskId(virtualDisk.getId());
        folder.setParentId(parent.getId());

        if(parent.getFileType() == FileType.VIRTUAL_DISK) {
            folder.setPath(virtualDisk.getPath() + "/" +  folder.getName());
        } else {
            folder.setPath(parent.getPath() + "/" + folder.getName());
        }

        folder.setFileType(FileType.DIRECTORY);
        folder.setFiles(new HashMap<>());

        double size = (double) folder.getName().getBytes().length / 1024;
        folder.setSize(size);

        parent.getFiles().put(folder.getId().toString(), folder);
        fileSystemUtils.updateFilesSize(virtualDisk, folder, size);

        return new FileActionResponse("Folder created", folder.getFileType());
    }

    private FileActionResponse copyFileInParent(VirtualDisk virtualDisk, UUID idDisk, UUID idParent, MyFile newFile) {

        UUID uuid = UUID.randomUUID();
        newFile.setName(newFile.getName() + "-copy" + uuid.toString().substring(0,5));
        newFile.setId(uuid);
        newFile.setDiskId(virtualDisk.getId());

        if(idDisk.equals(idParent)) {

            if(newFile.getFileType() == FileType.TXT_FILE) {
                newFile.setPath(virtualDisk.getPath());
            } else {
                newFile.setPath(virtualDisk.getPath() + "/" + newFile.getName());
            }

            newFile.setParentId(idDisk);
            virtualDisk.getFiles().put(uuid.toString(), newFile);
            virtualDisk.setSize(virtualDisk.getSize() + newFile.getSize());
            return new FileActionResponse("Archivo copiado correctamente!", newFile.getFileType());
        }

        Directory directoryParent = (Directory) fileSystemUtils.findFileById(virtualDisk, idParent)
                .orElseThrow(() -> new FileNotFoundException("Directory not found"));

        newFile.setParentId(directoryParent.getId());

        directoryParent.setSize(directoryParent.getSize() + newFile.getSize());

        fileSystemUtils.updateFilesSize(virtualDisk, directoryParent, newFile.getSize());

        if(newFile.getFileType() == FileType.DIRECTORY) {
            newFile.setPath(directoryParent.getPath() + "/" + newFile.getName());
            directoryParent.getFiles().put(uuid.toString(), newFile);
        } else {
            newFile.setPath(directoryParent.getPath());
            directoryParent.getFiles().put(newFile.getId().toString(), newFile);
        }

        return new FileActionResponse("Archivo copiado correctamente!", newFile.getFileType());
    }

    private void updateQuickAccess(MyFile file, FileUpdateRequest request) {
        // Update QuickAccess
        for (MyFile myFile : fileSystem.getQuickAccessList()) {
            if(myFile.getId().equals(file.getId())) {
                myFile.setName(request.getName());
                myFile.setDescription(request.getDescription());
                myFile.setLastModifiedAt(file.getLastModifiedAt());
                myFile.setSize(myFile.getSize());

                if(myFile.getFileType() == FileType.TXT_FILE) {
                    TextMyFile textMyFile = (TextMyFile) myFile;
                    textMyFile.setContent(request.getContent());
                }
            }
        }
    }

    private void updateQuickAccess(MyFile file) {
        // Update QuickAccess
        for (MyFile myFile : fileSystem.getQuickAccessList()) {
            if(myFile.getId().equals(file.getId())) {
                myFile = mapper.map(file, MyFile.class);

                if(myFile.getFileType() == FileType.TXT_FILE) {
                    TextMyFile textMyFile = (TextMyFile) file;
                    textMyFile.setContent(textMyFile.getContent());
                }
            }
        }
    }
}
