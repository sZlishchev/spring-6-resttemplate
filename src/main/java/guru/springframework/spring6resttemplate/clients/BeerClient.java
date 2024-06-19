package guru.springframework.spring6resttemplate.clients;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {
    Page<BeerDTO> getBeerList();
    
    Page<BeerDTO> getBeerList(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageSize, Integer pageNumber);

    BeerDTO getBeerById(UUID id);

    BeerDTO createBeer(BeerDTO newBeer);

    BeerDTO updateBeer(BeerDTO beerToUpdate);

    void deleteBeer(UUID id);
}
