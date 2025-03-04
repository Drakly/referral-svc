package org.referralsvc.referral.repository;

import org.referralsvc.referral.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReferralRepository extends JpaRepository<Referral, UUID> {

    Referral findByUserId(UUID userId);

    Referral findByReferralCode(String referralCode);
}
