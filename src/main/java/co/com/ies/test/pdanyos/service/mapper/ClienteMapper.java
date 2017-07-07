package co.com.ies.test.pdanyos.service.mapper;

import co.com.ies.test.pdanyos.domain.*;
import co.com.ies.test.pdanyos.service.dto.ClienteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Cliente and its DTO ClienteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteMapper extends EntityMapper <ClienteDTO, Cliente> {
    
    
    default Cliente fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }
}
