package com.example.BapZip.service.UserService;


import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.School;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.enums.AdminStatus;
import com.example.BapZip.repository.SchoolRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.security.TokenProvider;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final UserRepository userRepository;

    private final SchoolRepository schoolRepository;

    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();


    @Override
    public UserResonseDTO.JoinDTO create(final UserRequestDTO.JoinDTO dto)  {
            Optional<School> school = schoolRepository.findById(dto.getSchool());
            if(school.isEmpty()) throw new GeneralException(ErrorStatus.USER_JOIN_ERROR);
            User user= User.builder()
                    .userId(dto.getUserId())
                    .nickname(dto.getNickname())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .admin(AdminStatus.USER)
                    .school(school.get())
                    .major(dto.getMajor())
                    .build();
            User result = userRepository.save(user);

        return UserResonseDTO.JoinDTO.builder().id(result.getId()).build();

    }

    @Override
    public UserResonseDTO.loginDTO login(UserRequestDTO.LoginDTO loginDTO){
        final Optional<User> user=userRepository.findByUserId(loginDTO.getUserId());
        if(user.isPresent() && passwordEncoder.matches(loginDTO.getPassword(),user.get().getPassword())){
            return UserResonseDTO.loginDTO.builder().token(tokenProvider.create(user.get())).build();
        }
        else{
            throw new GeneralException(ErrorStatus.USER_LOGIN_ERROR);
        }
    }
}
