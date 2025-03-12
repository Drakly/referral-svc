package org.referralsvc.referral.web.controller;

import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.service.ReferralService;
import org.referralsvc.referral.web.dto.ReferralRequest;
import org.referralsvc.referral.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/referral")
public class ReferralController {

    private  final ReferralService referralService;

    @Autowired
    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getReferralByUser(@PathVariable("userId") UUID userId) {
        Optional<Referral> referralOpt = referralService.getReferralByUser(userId);
        if (referralOpt.isPresent()) {
            return ResponseEntity.ok(referralOpt.get());
        } else {
            Referral emptyReferral = Referral.builder()
                    .userId(userId)
                    .referralCode("")
                    .clickCount(0)
                    .build();
            return ResponseEntity.ok(emptyReferral);
        }
    }

    @PostMapping
    public ResponseEntity<ReferralRequest> createReferral(@RequestBody ReferralRequest referral) {
        Referral createdReferral = referralService.createReferral(referral);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.fromReferral(createdReferral));
    }

    @PutMapping("/track/{referralCode}")
    public ResponseEntity<Void> trackReferral(@PathVariable String referralCode) {
        referralService.incrementClickCount(referralCode);
        return ResponseEntity.ok().build();
    }
}
