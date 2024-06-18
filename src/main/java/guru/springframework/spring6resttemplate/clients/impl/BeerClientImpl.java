package guru.springframework.spring6resttemplate.clients.impl;

import guru.springframework.spring6resttemplate.clients.BeerClient;
import guru.springframework.spring6resttemplate.model.BeerRestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {
    
    private final RestTemplateBuilder restTemplateBuilder;
    
    private static final String BEER_PATH = "/api/v1/beer";
    
    @Override
    public BeerRestPage getBeerList() {
        final var restTemplate = this.restTemplateBuilder.build();
        
        final var pageResponse =
                restTemplate.getForEntity(BEER_PATH, BeerRestPage.class);
        
        return pageResponse.getBody();
    }
}
