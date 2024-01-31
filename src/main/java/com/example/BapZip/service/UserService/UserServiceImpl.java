package com.example.BapZip.service.UserService;


import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.Major;
import com.example.BapZip.domain.School;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.enums.AdminStatus;
import com.example.BapZip.domain.enums.Term;
import com.example.BapZip.repository.MajorRepository;
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
    private final MajorRepository majorRepository;


    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();


    @Override
    public UserResonseDTO.JoinDTO create(final UserRequestDTO.JoinDTO dto)  {
            Optional<School> school = schoolRepository.findById(dto.getSchool());
            Optional<Major> major = majorRepository.findById(dto.getMajor());
            if(school.isEmpty()) throw new GeneralException(ErrorStatus.USER_JOIN_ERROR);
            User user= User.builder()
                    .userId(dto.getUserId())
                    .nickname(dto.getNickname())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .admin(AdminStatus.USER)
                    .school(school.get())
                    .major(major.get().getName())
                    // 이미지 url 처리
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

    @Override
    public UserResonseDTO.checkNicknameAndIdDTO checkNickname(String nickname){
        final Optional<User> user=userRepository.findByNickname(nickname);
        if(user.isEmpty()){ return UserResonseDTO.checkNicknameAndIdDTO.builder().available(true).build();}
        else { return UserResonseDTO.checkNicknameAndIdDTO.builder().available(false).build();}
    }

    @Override
    public UserResonseDTO.checkNicknameAndIdDTO checkUserid(String nickname){
        final Optional<User> user=userRepository.findByUserId(nickname);
        if(user.isEmpty()){  return UserResonseDTO.checkNicknameAndIdDTO.builder().available(true).build();}
        else {  return UserResonseDTO.checkNicknameAndIdDTO.builder().available(false).build();}
    }

    @Override
    public void agreementToTerm(UserRequestDTO.TermDTO agreement) {
        User user = userRepository.findById(agreement.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        if(!agreement.getTerm1().equals(Term.CHECKED.toString()) || !agreement.getTerm2().equals(Term.CHECKED.toString()))
            throw new GeneralException(ErrorStatus.USER_TERM_ERROR);
        user.setTerm1(Term.valueOf(agreement.getTerm1())); // 약관1
        user.setTerm2(Term.valueOf(agreement.getTerm2())); // 약관2
        user.setTerm3(Term.valueOf(agreement.getTerm3())); // 약관3
        user.setTerm4(Term.valueOf(agreement.getTerm4())); // 약관3
        userRepository.save(user);
    }
}
