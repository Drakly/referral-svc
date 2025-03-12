package org.referralsvc.referral.service;

import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.repository.ReferralRepository;
import org.referralsvc.referral.web.dto.ReferralRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
        return "https://darex.com/referral/" + code;
    }

    public Referral createReferral(ReferralRequest referralRequest) {
        if (referralRequest.getReferralCode() == null || referralRequest.getReferralCode().isEmpty()) {
            referralRequest.setReferralCode(generateReferralLink());
        }
        Referral referral = Referral.builder()
                .userId(referralRequest.getUserId())
                .referralCode(referralRequest.getReferralCode())
                .createdAt(LocalDateTime.now())
                .clickCount(referralRequest.getClickCount())
                .build();

        Referral savedReferral = referralRepository.save(referral);
        return savedReferral;
    }

    public Optional<Referral> getReferralByUser(UUID userId) {
        return referralRepository.findByUserId(userId);
    }

    public void incrementClickCount(String referralCode) {
        Referral referral = referralRepository.findByReferralCode(referralCode);
        if (referral != null) {
            referral.setClickCount(referral.getClickCount() + 1);
            referralRepository.save(referral);
        }
    }
}
