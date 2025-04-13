package com.demo.Service.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.demo.DTO.User.UserDTO;
import com.demo.Entity.User.User;
import com.demo.Exception.User.UserNotFoundException;
import com.demo.Interface.User.UserService;
import com.demo.Repository.User.UserRepository;

@Service    //Marks this class as a Spring service component
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO)
    {
        User user = User.builder().username(userDTO.getUsername()).email(userDTO.getEmail()).role(userDTO.getRole())
        .password(passwordEncoder.encode(userDTO.getPassword())).build();
        user = userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public UserDTO getUser(Long uid) 
    {
        User user = userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
        return mapToDTO(user);
    }

    @Override
    public List<UserDTO> getUsers() 
    {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO partialUpdateUser(Long uid, UserDTO userDTO)
    {
        User user = userRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(uid));
        
        if(userDTO.getUsername() != null && !userDTO.getUsername().isBlank())
            user.setUsername(userDTO.getUsername());

        if(userDTO.getEmail() != null && !userDTO.getEmail().isBlank())
            user.setEmail(userDTO.getEmail());

        if(userDTO.getRole() != null) 
            user.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public void deleteUser(Long uid) 
    {
        if (!userRepository.existsById(uid)) 
            throw new UserNotFoundException(uid);
        
        userRepository.deleteById(uid);
    }

    private UserDTO mapToDTO(User user) 
    {
        return UserDTO.builder().uid(user.getUid()).username(user.getUsername()).email(user.getEmail())
        .role(user.getRole()).build();
    }
}