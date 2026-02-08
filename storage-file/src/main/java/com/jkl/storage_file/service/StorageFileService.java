package com.jkl.storage_file.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class StorageFileService {
    @Autowired
    private S3Client s3Client;
    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.endpoint}")
    private String endpoint;


    public String upload(MultipartFile file) {
        String fileName = buildFileName(file);
        File fileObj = convertMultipartfileToFile(file);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.putObject(putObjectRequest, fileObj.toPath());
        return endpoint + "/" + bucketName + "/" + fileName;
    }

    public boolean exists(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                return false;
            }
            throw ex;
        }
    }

    public String buildFileUrl(String key) {
        return endpoint + "/" + bucketName + "/" + key;
    }

    public List<String> listKeys(String prefix) {
        ListObjectsV2Request.Builder builder = ListObjectsV2Request.builder()
                .bucket(bucketName);
        if (prefix != null && !prefix.trim().isEmpty()) {
            builder.prefix(prefix.trim());
        }
        ListObjectsV2Response response = s3Client.listObjectsV2(builder.build());
        return response.contents().stream()
                .map(S3Object::key)
                .toList();
    }

    public ResponseBytes<GetObjectResponse> download(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        return s3Client.getObjectAsBytes(request);
    }

    public HeadObjectResponse head(String key) {
        return s3Client.headObject(HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    private String buildFileName(MultipartFile file) {
        String originalName = Objects.toString(file.getOriginalFilename(), "").trim();
        if (!originalName.isEmpty()) {
            return originalName;
        }
        return "upload-" + System.currentTimeMillis();
    }


    private File convertMultipartfileToFile(MultipartFile file) {
        File convertedFile = new File(buildFileName(file));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
