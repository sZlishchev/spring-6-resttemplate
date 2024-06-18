package guru.springframework.spring6resttemplate.clients.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerClientImplTest {
    
    @Autowired
    private BeerClientImpl beerClientImpl;
    

    @Test
    void getBeerList() {
        this.beerClientImpl.getBeerList();
    }
}