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
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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

    public Referral getReferralByUser(UUID userId) {
        Optional<Referral> referralOpt = referralRepository.findByUserId(userId);

        return referralOpt.orElseGet(() -> Referral.builder()
                .userId(userId)
                .referralCode("")
                .clickCount(0)
                .build());
    }


    public void incrementClickCount(String referralCode) {
        Referral referral = referralRepository.findByReferralCode(referralCode);
        if (referral != null) {
            referral.setClickCount(referral.getClickCount() + 1);
            referralRepository.save(referral);
        }
    }
}
