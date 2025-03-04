package org.referralsvc.referral.web.controller;

import org.referralsvc.referral.service.ReferralService;
import org.referralsvc.referral.web.dto.ReferralRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1//referral")
public class ReferralController {

    private  final ReferralService referralService;

    @Autowired
    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ReferralRequest> getReferralByUser(@PathVariable UUID userId) {
        ReferralRequest referral = referralService.getReferralByUser(userId);
        if (referral != null) {
            return ResponseEntity.ok(referral);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<ReferralRequest> createReferral(@RequestBody ReferralRequest referral) {
        ReferralRequest createdReferral = referralService.createReferral(referral);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReferral);
    }

    @GetMapping("/track/{referralCode}")
    public ResponseEntity<Void> trackReferral(@PathVariable String referralCode) {
        referralService.incrementClickCount(referralCode);
        // Можеш да пренасочиш към началната страница или към друга страница, ако желаеш:
        return ResponseEntity.ok().build();
    }
}
