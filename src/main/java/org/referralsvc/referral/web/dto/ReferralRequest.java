package org.referralsvc.referral.web.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReferralRequest {

    private UUID id;

    private UUID userId;

    private String referralCode;

    private LocalDateTime createdAt;

    private int clickCount;

}
