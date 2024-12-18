package com.sr.Ziply.service;


import com.sr.Ziply.dto.UserDto;
import com.sr.Ziply.enums.UserRole;
import com.sr.Ziply.exception.SourceNotFoundException;
import com.sr.Ziply.model.Address;
import com.sr.Ziply.model.Cart;
import com.sr.Ziply.model.User;
import com.sr.Ziply.repository.CartRepository;
import com.sr.Ziply.repository.UserRepository;
import com.sr.Ziply.request.RegisterRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final CartRepository cartRepository;
    private final JwtUtils jwtUtils;

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if(request.getUserRole() .equals( UserRole.CUSTOMER.toString())){
            user.setRole(UserRole.CUSTOMER);
        }
        else
            user.setRole(UserRole.RESTAURENT_OWNER);


        Cart cart = new Cart();

        User saveuser = userRepository.save(user);
//        Cart cart = new Cart();
        cart.setCustomer(saveuser);
        cartRepository.save(cart);
        return saveuser;

    }

    public UserDto convertUserDto(User user){
        UserDto  userDto= new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        userDto.setEmail(user.getEmail());
        userDto.setOrders(user.getOrders());
        userDto.setFavorites(user.getFavorites());
        userDto.setAddresses(user.getAddresses());
        return userDto;
    }

    public User findUserByJwt(String jwt)throws SourceNotFoundException{
        try {
            String name = jwtUtils.extractUsername(jwt);
            return userRepository.findByEmail(name);
        } catch (Exception e) {
            throw new SourceNotFoundException("User Not Found");
        }
    }
//   @PostConstruct

    private void createAdmin(){
        User user = new User();
        user.setName("admin");
        user.setEmail("admin@gmail.com");
        user.setRole(UserRole.ADMIN);
        user.setPassword(passwordEncoder.encode("admin"));
        User saveuser = userRepository.save(user);
    }
}