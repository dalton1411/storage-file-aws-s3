package com.jkl.storage_file.service;


import com.jkl.storage_file.model.dto.UserRequest;
import com.jkl.storage_file.model.entity.User;
import com.jkl.storage_file.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StorageFileService storageFileService;

    public String save(UserRequest request) {
        String profileImageUrl = storageFileService.upload(
                request.profileImageFile(),
                request.name(),
                "profileImage");
        User user = new User(request.name(), profileImageUrl);

        userRepository.save(user);
        StringBuilder builder = new StringBuilder();
        builder.append("Username: "+profileImageUrl+"\n");
        return builder.toString();
    }
}
