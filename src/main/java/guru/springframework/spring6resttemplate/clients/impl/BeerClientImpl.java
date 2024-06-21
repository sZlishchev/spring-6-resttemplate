package guru.springframework.spring6resttemplate.clients.impl;

import guru.springframework.spring6resttemplate.clients.BeerClient;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerRestPage;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {
    
    private final RestTemplateBuilder restTemplateBuilder;
    
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    @Override
    public Page<BeerDTO> getBeerList() {
        return this.getBeerList(null, null, null, null,null);
    }

    @Override
    public Page<BeerDTO> getBeerList(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageSize, Integer pageNumber) {
        final var restTemplate = this.restTemplateBuilder.build();
        
        final var uriComponentBuilder = UriComponentsBuilder.fromPath(BEER_PATH);
        
        if (beerName != null) {
            uriComponentBuilder.queryParam("beerName", beerName);
        }
        if (beerStyle != null) {
            uriComponentBuilder.queryParam("beerStyle", beerStyle);
        }

        if (showInventory != null) {
            uriComponentBuilder.queryParam("showInventory", beerStyle);
        }

        if (pageNumber != null) {
            uriComponentBuilder.queryParam("pageNumber", beerStyle);
        }

        if (pageSize != null) {
            uriComponentBuilder.queryParam("pageSize", beerStyle);
        }
        final var pageResponse =
                restTemplate.getForEntity(uriComponentBuilder.toUriString(), BeerRestPage.class);
        
        return pageResponse.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID id) {
        final var restTemplate = this.restTemplateBuilder.build();
        return restTemplate.getForObject(BEER_BY_ID_PATH, BeerDTO.class, id);
    }

    @Override
    public BeerDTO createBeer(BeerDTO newBeer) {
        final var restTemplate = this.restTemplateBuilder.build();

        final var createdBeer = restTemplate.postForEntity(BEER_PATH, newBeer, BeerDTO.class);

        return createdBeer.getBody();
    }

    @Override
    public BeerDTO updateBeer(BeerDTO beerToUpdate) {
        final var restTemplate = this.restTemplateBuilder.build();
        
        restTemplate.put(BEER_BY_ID_PATH, beerToUpdate, beerToUpdate.getId());
        
        return this.getBeerById(beerToUpdate.getId());
    }

    @Override
    public void deleteBeer(UUID id) {
        final var restTemplate = this.restTemplateBuilder.build();
        
        restTemplate.delete(BEER_BY_ID_PATH, id);
    }
}
