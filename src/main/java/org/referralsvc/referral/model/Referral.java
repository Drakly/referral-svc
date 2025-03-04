package org.referralsvc.referral.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "referrals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID userId;

    @Column
    private String referralCode;

    @Column
    private LocalDateTime createdAt;

    @Column(name = "click_count")
    private int clickCount = 0;
}
