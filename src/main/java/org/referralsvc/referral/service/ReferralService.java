package org.referralsvc.referral.service;

import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.repository.ReferralRepository;
import org.referralsvc.referral.web.dto.ReferralRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReferralService {

    private final ReferralRepository referralRepository;

    @Autowired
    public ReferralService(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    private String generateReferralLink() {

        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Връщаме линк с основния домейн
        return "https://darex.com/referral/" + code;
    }

    // POST операция: създаване на нов реферал
    public ReferralRequest createReferral(ReferralRequest referralRequest) {
        // Генерирайте реферален код ако не е предоставен
        if (referralRequest.getReferralCode() == null || referralRequest.getReferralCode().isEmpty()) {
            referralRequest.setReferralCode(generateReferralLink());
        }
        Referral referral = Referral.builder()
                .id(referralRequest.getId())
                .userId(referralRequest.getUserId())
                .referralCode(referralRequest.getReferralCode())
                .createdAt(LocalDateTime.now())
                .build();

        Referral savedReferral = referralRepository.save(referral);
        // Преобразуване в DTO
        return new ReferralRequest(savedReferral.getId(), savedReferral.getUserId(), savedReferral.getReferralCode(), savedReferral.getCreatedAt());
    }

    // GET операция: извличане на реферал по userId
    public ReferralRequest getReferralByUser(UUID userId) {
        Referral referral = referralRepository.findByUserId(userId);
        if (referral != null) {
            return new ReferralRequest(referral.getId(), referral.getUserId(), referral.getReferralCode(), referral.getCreatedAt());
        }
        return null;
    }

    public void incrementClickCount(String referralCode) {
        Referral referral = referralRepository.findByReferralCode(referralCode);
        if (referral != null) {
            referral.setClickCount(referral.getClickCount() + 1);
            referralRepository.save(referral);
        }
    }
}
