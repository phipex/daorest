package co.com.ies.test.pdanyos.repository;


import co.com.ies.test.pdanyos.domain.Cliente;
import co.com.ies.test.pdanyos.service.RemoteLoginService;
import co.com.ies.test.pdanyos.service.impl.RemoteLoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by root on 1/07/17.
 */
@org.springframework.stereotype.Repository
public class ClienteRestRepository extends AbstractRestRepository<Cliente ,Long>  {

    private final Logger log = LoggerFactory.getLogger(ClienteRestRepository.class);

    private  CredencialesRest credencialesRest = null;

    private RemoteLoginService remoteLoginService;

    public ClienteRestRepository(RemoteLoginService remoteLoginService)
    {
        super(Cliente.class);
        this.remoteLoginService= remoteLoginService;
    }

    @Override
    public void validarCredenciales() {

        //todo traer este dato desde propierties
        this.credencialesRest = new CredencialesRest("http://localhost:8080/api/",null,"clientes");
        //todo traer este dato desde proierties
        RemoteLoginServiceImpl.Login login = new RemoteLoginServiceImpl.Login("admin","admin");

        remoteLoginService.loginRemoto(this.credencialesRest,login);
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public CredencialesRest getCredenciales() {

        if(this.credencialesRest == null) {
            validarCredenciales();
        }

        return this.credencialesRest;
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

    public List<Cliente> findAll() {
        return getRequest();
    }


}
