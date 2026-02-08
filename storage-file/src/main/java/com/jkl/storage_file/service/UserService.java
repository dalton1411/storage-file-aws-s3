package com.jkl.storage_file.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StorageFileService storageFileService;

    public String save(MultipartFile file) {
        return storageFileService.upload(file);
    }

    public String getFileUrl(String key) {
        if (!storageFileService.exists(key)) {
            return null;
        }
        return storageFileService.buildFileUrl(key);
    }

    public List<String> listKeys(String prefix) {
        return storageFileService.listKeys(prefix);
    }

    public ResponseBytes<GetObjectResponse> download(String key) {
        return storageFileService.download(key);
    }

    public HeadObjectResponse head(String key) {
        return storageFileService.head(key);
    }
}
