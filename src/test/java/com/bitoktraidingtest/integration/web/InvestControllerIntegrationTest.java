package com.bitoktraidingtest.integration.web;

import com.bitoktraidingtest.BaseIntegrationTest;
import com.bitoktraidingtest.controller.InvestController;
import com.bitoktraidingtest.service.InvestSuggestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

class InvestControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InvestSuggestionService investSuggestionService;

    @Test
    void shouldInvestTest() {
        //Given
        final String response = """
                {
                "shouldInvest": true,
                "message": "Фиксируем прибыль!"
                }
                """;
        when(investSuggestionService.shouldInvest()).thenReturn(true);

        //When & Then
        webTestClient.get()
                .uri(InvestController.SHOULD_INVEST_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(response);
    }

    @Test
    void shouldNotInvestTest() {
        //Given
        final String response = """
                {
                "shouldInvest": false,
                "message": "Крипта - скам!"
                }
                """;
        when(investSuggestionService.shouldInvest()).thenReturn(false);

        //When & Then
        webTestClient.get()
                .uri(InvestController.SHOULD_INVEST_PATH)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(response);
    }
}
