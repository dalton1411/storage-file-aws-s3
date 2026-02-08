package com.jkl.storage_file.controller;


import com.jkl.storage_file.model.dto.UserRequest;
import com.jkl.storage_file.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/multipart/form-data")
    public ResponseEntity<String> saveUser(@ModelAttribute UserRequest request) {
        String profileImageUrl = userService.save(request);
        return ResponseEntity.ok(profileImageUrl);

    }


}
