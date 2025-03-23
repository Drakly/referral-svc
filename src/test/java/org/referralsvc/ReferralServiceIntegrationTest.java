package org.referralsvc;

import org.junit.jupiter.api.Test;
import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.repository.ReferralRepository;
import org.referralsvc.referral.service.ReferralService;
import org.referralsvc.referral.web.dto.ReferralRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReferralServiceIntegrationTest {

    @Autowired
    private ReferralService referralService;

    @Autowired
    private ReferralRepository referralRepository;


    @Test
    public void testCreateReferral_savedInDatabase() {
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setUserId(UUID.randomUUID());
        referralRequest.setReferralCode("");
        referralRequest.setClickCount(0);

        Referral createdReferral = referralService.createReferral(referralRequest);

        assertNotNull(createdReferral.getId(), "Записаният реферал трябва да има ID");

        Optional<Referral> foundReferral = referralRepository.findById(createdReferral.getId());
        assertTrue(foundReferral.isPresent(), "Рефералът трябва да бъде намерен в базата");

        Referral referralFromDb = foundReferral.get();
        assertEquals(referralRequest.getUserId(), referralFromDb.getUserId());
        assertEquals(referralRequest.getClickCount(), referralFromDb.getClickCount());
        assertNotNull(referralFromDb.getReferralCode(), "Referral code не трябва да бъде null");
        assertFalse(referralFromDb.getReferralCode().isEmpty(), "Referral code не трябва да бъде празен");
    }
}
