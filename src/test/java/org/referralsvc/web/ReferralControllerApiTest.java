package org.referralsvc.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.referralsvc.referral.model.Referral;
import org.referralsvc.referral.service.ReferralService;
import org.referralsvc.referral.web.controller.ReferralController;
import org.referralsvc.referral.web.dto.ReferralRequest;
import org.referralsvc.referral.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReferralController.class)
public class ReferralControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReferralService referralService;

    @Test
    void getReferralByUser_shouldReturnReferral() throws Exception {
        UUID userId = UUID.randomUUID();
        Referral referral = new Referral();
        referral.setUserId(userId);

        when(referralService.getReferralByUser(userId)).thenReturn(referral);

        mockMvc.perform(get("/api/v1/referral/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    void createReferral_shouldReturnCreatedReferral() throws Exception {
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setUserId(UUID.randomUUID());

        Referral referral = new Referral();
        referral.setUserId(referralRequest.getUserId());

        when(referralService.createReferral(any(ReferralRequest.class))).thenReturn(referral);

        ReferralRequest expectedResponse = DtoMapper.fromReferral(referral);

        String requestJson = objectMapper.writeValueAsString(referralRequest);
        String responseJson = objectMapper.writeValueAsString(expectedResponse);

        mockMvc.perform(post("/api/v1/referral")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson));
    }

    @Test
    void trackReferral_shouldReturnOk() throws Exception {
        String referralCode = "ABC123";

        mockMvc.perform(put("/api/v1/referral/track/{referralCode}", referralCode))
                .andExpect(status().isOk());

        verify(referralService, times(1)).incrementClickCount(referralCode);
    }
}
