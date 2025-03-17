package org.referralsvc.referral;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.repository.ReferralRepository;
import org.referralsvc.referral.service.ReferralService;
import org.referralsvc.referral.web.dto.ReferralRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferralServiceUnitTests {

    @Mock
    private ReferralRepository referralRepository;

    @InjectMocks
    private ReferralService referralService;

    @Test
    void testCreateReferral_withProvidedReferralCode() {
        UUID userId = UUID.randomUUID();
        ReferralRequest request = new ReferralRequest();
        request.setUserId(userId);
        request.setReferralCode("REF12345");
        request.setClickCount(5);

        Referral savedReferral = Referral.builder()
                .userId(userId)
                .referralCode("REF12345")
                .clickCount(5)
                .createdAt(LocalDateTime.now())
                .build();
        savedReferral.setId(UUID.randomUUID());
        when(referralRepository.save(any(Referral.class))).thenReturn(savedReferral);

        Referral result = referralService.createReferral(request);

        assertNotNull(result.getId());
        assertEquals("REF12345", result.getReferralCode());
        assertEquals(5, result.getClickCount());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void testCreateReferral_withoutProvidedReferralCode() {
        UUID userId = UUID.randomUUID();
        ReferralRequest request = new ReferralRequest();
        request.setUserId(userId);
        request.setReferralCode("");
        request.setClickCount(0);
        when(referralRepository.save(any(Referral.class))).thenAnswer(invocation -> {
            Referral referral = invocation.getArgument(0);
            referral.setId(UUID.randomUUID());
            return referral;
        });

        Referral result = referralService.createReferral(request);

        assertNotNull(result.getId());
        assertNotNull(result.getReferralCode());
        assertEquals(8, result.getReferralCode().length());
        assertEquals(0, result.getClickCount());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void testGetReferralByUser_found() {
        UUID userId = UUID.randomUUID();
        Referral existingReferral = Referral.builder()
                .userId(userId)
                .referralCode("EXIST123")
                .clickCount(10)
                .createdAt(LocalDateTime.now())
                .build();
        when(referralRepository.findByUserId(userId)).thenReturn(Optional.of(existingReferral));

        Referral result = referralService.getReferralByUser(userId);

        assertEquals("EXIST123", result.getReferralCode());
        assertEquals(10, result.getClickCount());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void testGetReferralByUser_notFound() {
        UUID userId = UUID.randomUUID();
        when(referralRepository.findByUserId(userId)).thenReturn(Optional.empty());
        Referral result = referralService.getReferralByUser(userId);

        assertEquals(userId, result.getUserId());
        assertEquals("", result.getReferralCode());
        assertEquals(0, result.getClickCount());
    }

    @Test
    void testIncrementClickCount() {
        String referralCode = "REFCODE1";
        Referral referral = Referral.builder()
                .userId(UUID.randomUUID())
                .referralCode(referralCode)
                .clickCount(3)
                .createdAt(LocalDateTime.now())
                .build();
        referral.setId(UUID.randomUUID());
        when(referralRepository.findByReferralCode(referralCode)).thenReturn(referral);

        referralService.incrementClickCount(referralCode);

        assertEquals(4, referral.getClickCount());
        verify(referralRepository).save(referral);
    }
}
