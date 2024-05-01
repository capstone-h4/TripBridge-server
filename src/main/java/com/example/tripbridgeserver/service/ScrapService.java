package com.example.tripbridgeserver.service;


import com.example.tripbridgeserver.common.ResponseDTO;
import com.example.tripbridgeserver.dto.ScrapDTO;
import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.UserEntity;
import com.example.tripbridgeserver.repository.ScrapRepository;
import com.example.tripbridgeserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    @Autowired
    public ScrapService(ScrapRepository scrapRepository, UserRepository userRepository) {
        this.scrapRepository = scrapRepository;
        this.userRepository = userRepository;
    }

    public Scrap create(ScrapDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserEntity currentUser = userRepository.findByEmail(userEmail);
        Scrap scrap = dto.toEntity(currentUser);
        return scrapRepository.save(scrap);
    }

//    public ResponseEntity<Scrap> delete(Long id) {
//        Scrap target = scrapRepository.findById(id).orElse(null);
//        if (target == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//        scrapRepository.delete(target);
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }

    public ResponseEntity<ResponseDTO<Void>> delete(Long id) {
        Scrap target = scrapRepository.findById(id).orElse(null);
        if (target == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.set(false, "Scrap ID " + id + " 을(를) 찾지 못하였습니다.", null));
        }
        try {
            scrapRepository.delete(target);
            return ResponseEntity.ok().body(ResponseDTO.setSuccess("Scrap ID " + id + " 이(가) 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDTO.setError("Scrap 삭제에 실패하였습니다."));
        }
    }

    public List<Scrap> findByUser(UserEntity userEntity) {
        return scrapRepository.findByUserEntity(userEntity);
    }
}

