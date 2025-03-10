package org.referralsvc.referral.web.mapper;

import lombok.experimental.UtilityClass;
import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.web.dto.ReferralRequest;

@UtilityClass
public class DtoMapper {

    public static ReferralRequest fromReferral(Referral referral) {
        return ReferralRequest.builder()
                .id(referral.getId())
                .referralCode(referral.getReferralCode())
                .createdAt(referral.getCreatedAt())
                .userId(referral.getUserId())
                .clickCount(referral.getClickCount())
                .build();
    }
}
