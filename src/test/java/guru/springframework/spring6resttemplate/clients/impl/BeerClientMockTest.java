package guru.springframework.spring6resttemplate.clients.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.clients.BeerClient;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerRestPage;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {

    private static final String URL = "http://localhost:8080";

    
    private BeerClient beerClient;
    
    private MockRestServiceServer mockRestServiceServer;
    
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Mock
    private RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());
    
    private BeerDTO dto;
    private String jsonDto;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        final var restTemplate = this.restTemplateBuilder.build();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);
        dto = this.getBeerDTO();
        jsonDto = this.objectMapper.writeValueAsString(dto);
        
    }

    @Test
    void testListBeer() throws JsonProcessingException {
        final var payload = this.objectMapper.writeValueAsString(this.getPage());
        
        mockRestServiceServer.expect(method(HttpMethod.GET))
                .andExpect(requestTo(URL + BeerClientImpl.BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        final var beerList = this.beerClient.getBeerList();
        
        assertThat(beerList.getSize()).isGreaterThan(0);
    }

    @Test
    void testListBeerWithQuery() throws JsonProcessingException {
        final var payload = this.objectMapper.writeValueAsString(this.getPage());
        
        final var uri = UriComponentsBuilder.fromUriString(URL + BeerClientImpl.BEER_PATH)
            .queryParam("beerName", dto.getBeerName())
            .build().toUri();
        
        this.mockRestServiceServer.expect(method(HttpMethod.GET))
            .andExpect(requestTo(uri))
            .andExpect(queryParam("beerName", dto.getBeerName()))
            .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));
        
        final var response = this.beerClient.getBeerList(dto.getBeerName(), null, null, null,null);
        
        assertThat(response).isNotNull();
        assertThat(response.getSize()).isGreaterThan(0);
    }

    @Test
    void testGetBeerById() {
        runGetOperation();

        final var result = this.beerClient.getBeerById(dto.getId());
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testCreateBeer() {
        mockRestServiceServer.expect(method(HttpMethod.POST))
                .andExpect(requestTo(URL + BeerClientImpl.BEER_PATH))
                .andRespond(withSuccess(jsonDto, MediaType.APPLICATION_JSON));

        final var result = this.beerClient.createBeer(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testUpdateBeer() {
        mockRestServiceServer.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withSuccess(jsonDto, MediaType.APPLICATION_JSON));
        
        runGetOperation();

        final var updateBeer = this.beerClient.updateBeer(dto);

        assertThat(updateBeer).isNotNull();
        assertThat(updateBeer.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testDeleteBeer() {
        mockRestServiceServer.expect(method(HttpMethod.DELETE))
            .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_BY_ID_PATH, dto.getId()))
            .andRespond(withNoContent());
        
        this.beerClient.deleteBeer(dto.getId());
        
        mockRestServiceServer.verify();
    }

    @Test
    void testDeleteBeerNotFound() {
        mockRestServiceServer.expect(method(HttpMethod.DELETE))
            .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_BY_ID_PATH, dto.getId()))
            .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.class, () -> this.beerClient.deleteBeer(dto.getId()));
        
        mockRestServiceServer.verify();
    }
    
    

    private BeerDTO getBeerDTO() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Test")
                .beerStyle(BeerStyle.ALE)
                .upc("2421")
                .price(BigDecimal.TEN)
                .quantityOnHand(100)
                .build();
    }

    BeerRestPage getPage(){
        return new BeerRestPage(Arrays.asList(getBeerDTO()), 1, 25, 1);
    }

    private void runGetOperation() {
        mockRestServiceServer.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withSuccess(jsonDto, MediaType.APPLICATION_JSON));
    }
}
