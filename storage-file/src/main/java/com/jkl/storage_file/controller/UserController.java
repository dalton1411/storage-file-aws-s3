package com.jkl.storage_file.controller;


import com.jkl.storage_file.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/file")
    public ResponseEntity<String> getFile(@RequestParam("key") String key) {
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Informe o parametro 'key'.");
        }
        String url = userService.getFileUrl(key.trim());
        if (url == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(url);
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles(
            @RequestParam(value = "prefix", required = false) String prefix) {
        return ResponseEntity.ok(userService.listKeys(prefix));
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam("key") String key) {
        if (key == null || key.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        ResponseBytes<GetObjectResponse> data = userService.download(key.trim());
        HeadObjectResponse head = userService.head(key.trim());
        String contentType = head.contentType() != null ? head.contentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        ByteArrayResource resource = new ByteArrayResource(data.asByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key.trim() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(data.asByteArray().length)
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        MultipartFile fileToUpload = file != null ? file : profileImageFile;
        if (fileToUpload == null || fileToUpload.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo nao informado.");
        }
        String profileImageUrl = userService.save(fileToUpload);
        return ResponseEntity.ok(profileImageUrl);

    }


}
