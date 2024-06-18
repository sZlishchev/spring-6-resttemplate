package guru.springframework.spring6resttemplate.clients;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import org.springframework.data.domain.Page;

public interface BeerClient {
    Page<BeerDTO> getBeerList();
}
