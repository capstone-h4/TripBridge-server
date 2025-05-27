package com.example.tripbridgeserver.service;

import com.example.tripbridgeserver.dto.ResponseDTO;
import com.example.tripbridgeserver.dto.ScrapRequest;
import com.example.tripbridgeserver.entity.Scrap;
import com.example.tripbridgeserver.entity.User;
import com.example.tripbridgeserver.repository.ScrapRepository;
import com.example.tripbridgeserver.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    public ResponseDTO<Scrap> createPlaceScrap(ScrapRequest scrapRequest, String userEmail) {

        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            return ResponseDTO.setFailed("사용자를 찾을 수 없습니다.");
        }

        Scrap scrap = scrapRequest.toEntity(user);

        try {
            Scrap saved = scrapRepository.save(scrap);
            return ResponseDTO.setSuccessData("성공적으로 저장하였습니다.", saved);
        } catch (DataIntegrityViolationException e) {
            return ResponseDTO.setFailed("해당 장소가 이미 저장되어 있습니다.");
        } catch (Exception e) {
            return ResponseDTO.setFailed("저장 중 알 수 없는 오류가 발생했습니다.");
        }
    }


    public ResponseEntity<ResponseDTO<Void>> deletePlaceScrap(Long id) {
        Scrap scrap = scrapRepository.findById(id).orElse(null);
        if (scrap == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO.set(false, "Scrap ID " + id + " 을(를) 찾지 못하였습니다.", null));
        }
        try {
            scrapRepository.delete(scrap);
            return ResponseEntity.ok().body(ResponseDTO.setSuccess("Scrap ID " + id + " 이(가) 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseDTO.setError("Scrap 삭제에 실패하였습니다."));
        }
    }
}

