package com.jkl.storage_file.model.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserRequest(String name, MultipartFile profileImageFile) {
}
