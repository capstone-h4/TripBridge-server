package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.UserRequestDTO;
import com.example.tripbridgeserver.dto.UserResponseDTO;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.UserRepository;
import com.example.tripbridgeserver.common.DtoMapper;
import com.example.tripbridgeserver.common.ResponseDTO;
import com.example.tripbridgeserver.common.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;
    private final JwtProvider jwtProvider;

    public ResponseDTO<?> signup(UserRequestDTO.SignUp dto) {

        UserEntity user = userRepository.findByEmail(dto.getEmail());
        UserEntity user_nickname = userRepository.findByNickname(dto.getNickname());

        if (user != null) {
            return ResponseDTO.setFailed("중복된 Email 입니다.");

        }

        if (user_nickname != null) {
            return ResponseDTO.setFailed("중복된 Nickname 입니다.");
        }

        if (!dto.getPassword().equals(dto.getPw_check())) {
            return ResponseDTO.setFailed("비밀번호가 일치하지 않습니다.");
        }

        user = dtoMapper.transform(dto, UserEntity.class);

        userRepository.save(user);

        return ResponseDTO.setSuccess("회원 생성에 성공했습니다.");
    }

    @Transactional
    public ResponseDTO<?> login(UserRequestDTO.Login dto) {

        UserEntity user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            return ResponseDTO.setFailed("존재하지 않는 사용자입니다.");
        }

        if (!verifyPassword(dto.getPassword(), user.getPassword())) {
            return ResponseDTO.setFailed("비밀번호가 일치하지 않습니다.");
        }

        String refreshToken = jwtProvider.createRefreshToken(user.getEmail(), null);
        String accessToken = jwtProvider.createAccessToken(user.getEmail(), null);
        String nickname = user.getNickname();

        UserResponseDTO.Login data = UserResponseDTO.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .nickname(nickname)
                .build();

        user.setToken(refreshToken);

        return ResponseDTO.setSuccessData("성공적으로 토큰이 발급 되었습니다.", data);
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return inputPassword.equals(storedPassword);
    }

    public boolean existUser(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public ResponseDTO<?> logout() {
        String email = jwtProvider.getUserIdFromAccessToken(jwtProvider.resolveAccessToken());
        UserEntity user = userRepository.findByEmail(email);

        if (user != null) {
            user.setToken(null);
        }

        SecurityContextHolder.clearContext();
        return ResponseDTO.setSuccess("로그아웃 되었습니다.");
    }
}
