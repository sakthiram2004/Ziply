package com.sr.Ziply.service.common;

import com.sr.Ziply.dto.UserDto;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.User;
import com.sr.Ziply.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final UserRepository userRepository;

    public UserDto getUserProfile(String email) throws SourceNotFoundException {
        // Use Optional to handle user retrieval and checking existence
        User user = userRepository.findByEmail(email)
                ;

        // Map the User entity to UserDto
        UserDto userProfile = new UserDto();
        userProfile.setId(user.getId());
        userProfile.setName(user.getName());
        userProfile.setEmail(user.getEmail());
        userProfile.setRole(user.getRole());
        userProfile.setFavorites(user.getFavorites());
        userProfile.setAddresses(user.getAddresses());
        userProfile.setOrders(user.getOrders());

        return userProfile;
    }
}
