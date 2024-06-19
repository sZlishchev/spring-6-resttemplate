package guru.springframework.spring6resttemplate.clients.impl;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {
    
    @Autowired
    private BeerClientImpl beerClientImpl;
    

    @Test
    void getBeerList() {
        this.beerClientImpl.getBeerList();
    }

    @Test
    void getBeerListByBeerName() {
        this.beerClientImpl.getBeerList("ALE",null, null,null, null);
    }

    @Test
    void getBeerById() {
        final var beer = this.beerClientImpl.getBeerList().getContent().get(0);

        final var result = this.beerClientImpl.getBeerById(beer.getId());

        assertNotNull(result);
    }

    @Test
    void createNewBeer() {
        final var newBeer = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.ALE)
                .upc("2421")
                .price(BigDecimal.TEN)
                .quantityOnHand(100)
                .build();

        final var createdBeer = this.beerClientImpl.createBeer(newBeer);
        
        assertNotNull(createdBeer);
    }

    @Test
    void updateBeer() {
        final var newBeer = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.ALE)
                .upc("2421")
                .price(BigDecimal.TEN)
                .quantityOnHand(100)
                .build();

        final var createdBeer = this.beerClientImpl.createBeer(newBeer);
        
        final var newName = "New Beer Name";
        
        createdBeer.setBeerName(newName);

        final var updatedBeer = this.beerClientImpl.updateBeer(createdBeer);

        assertNotNull(createdBeer);
        assertEquals(newName, updatedBeer.getBeerName());
    }

    @Test
    void deleteBeer() {
        final var newBeer = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.ALE)
                .upc("2421")
                .price(BigDecimal.TEN)
                .quantityOnHand(100)
                .build();

        final var createdBeer = this.beerClientImpl.createBeer(newBeer);

        this.beerClientImpl.deleteBeer(createdBeer.getId());
        
        assertThrows(HttpClientErrorException.class, () -> this.beerClientImpl.getBeerById(createdBeer.getId()));
    }
}