package co.com.ies.test.pdanyos.repository;


import co.com.ies.test.pdanyos.domain.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

/**
 * Created by root on 1/07/17.
 */
@org.springframework.stereotype.Repository
public class ClienteRestRepository extends AbstractRestRepository<Cliente ,Long>  {

    private final Logger log = LoggerFactory.getLogger(ClienteRestRepository.class);

    public ClienteRestRepository() {
        super(Cliente.class);
    }

    @Override
    public Logger getLog() {
        return log;
    }

    public Cliente save(Cliente cliente){
        Assert.notNull(cliente,"Error en los argumentos");
        Cliente newCliente = null;

        newCliente = postRequest(cliente);
        getLog().debug("cliente retornado {} ",newCliente);

        return newCliente;

    }

    public Cliente update(Cliente cliente){
        Assert.notNull(cliente,"Error en los argumentos");

        Cliente updatedCliente = null;

        updatedCliente = putRequest(cliente);
        getLog().debug("cliente retornado {} ",updatedCliente);

        return updatedCliente;

    }

    public Cliente update(Long idCliente){
        Cliente encontrado = findOne(idCliente);

        Assert.notNull(encontrado, "EL cliente con el con ese id no existe");

        return encontrado;
    }


    public Cliente findOne(Long idCliente){
        Assert.notNull(idCliente,"Error en los argumentos");
        Cliente findedCliente = null;

        findedCliente = getRequest(idCliente);
        getLog().debug("cliente retornado {} ",findedCliente);

        return findedCliente;

    }


}
