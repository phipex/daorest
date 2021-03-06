package co.com.ies.test.pdanyos.service;

import co.com.ies.test.pdanyos.service.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Cliente.
 */
public interface ClienteService {

    /**
     * Save a cliente.
     *
     * @param clienteDTO the entity to save
     * @return the persisted entity
     */
    ClienteDTO save(ClienteDTO clienteDTO);
    ClienteDTO save2(ClienteDTO clienteDTO);

    ClienteDTO update2(ClienteDTO clienteDTO);

    /**
     *  Get all the clientes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ClienteDTO> findAll(Pageable pageable);
    List<ClienteDTO> findAll2();
    /**
     *  Get the "id" cliente.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ClienteDTO findOne(Long id);
    ClienteDTO findOne2(Long id);
    /**
     *  Delete the "id" cliente.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
