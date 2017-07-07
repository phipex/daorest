package co.com.ies.test.pdanyos.repository;

import co.com.ies.test.pdanyos.domain.Cliente;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.data.repository.Repository;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
/**
 * Created by root on 1/07/17.
 */

public abstract class AbstractRestRepository<T,ID extends Serializable> implements Repository<T ,ID> {

    /**
     * La entidad que se manejará.
     */
    private Class<T> clazz;

    public AbstractRestRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    public abstract Logger getLog();

    T postRequest(T request){
        return postPutRequest(request, HttpMethod.POST );
    }

    T putRequest(T request){
        return postPutRequest(request, HttpMethod.PUT);
    }


    public T getRequest(Long idRequest){
        String resourcePath = "clientes";//todo ingresar los datos en un objeto **************
        String pathApiRoot = "http://localhost:8080/api/";//todo ingresar los datos en un objeto **************
        String resourceUrl = pathApiRoot + resourcePath + "/"+ idRequest;

        T tNewResource = requestAccion(null, HttpMethod.GET, resourceUrl);

        return tNewResource;
    }

    private T postPutRequest(T request, HttpMethod post){

        String resourcePath = "clientes";//todo ingresar los datos en un objeto **************
        String pathApiRoot = "http://localhost:8080/api/";//todo ingresar los datos en un objeto **************
        String resourceUrl = pathApiRoot + resourcePath;

        T tNewResource = requestAccion(request, post, resourceUrl);

        return tNewResource;
    }

    private T requestAccion(T request, HttpMethod post, String resourceUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        final StringBuilder newResource = new StringBuilder();
        T tNewResource = null;
        //Object request = new Object();//************

        AbstractRestCallback restCallback = new AbstractRestCallback() {
            @Override
            public void onSucces(ResponseEntity<String> stringResponseEntity) {
                HttpStatus statusCode = stringResponseEntity.getStatusCode();
                String bodyResponse = stringResponseEntity.getBody();



                final String responseMsg = "crearRecargaWPlay::status=" + statusCode.name() + ",bodyResponse"
                    + bodyResponse;

                getLog().info(responseMsg);

                if (HttpStatus.OK.equals(statusCode) || HttpStatus.CREATED.equals(statusCode)) {


                    getLog().info("request correcto************* "+newResource+"****");
                    newResource.append(bodyResponse);
                    getLog().info("request correcto************* "+newResource+"****");
                }
            }

            @Override
            public void onFailStatus(HttpStatusCodeException exception) {
                String bodyResponseError = exception.getResponseBodyAsString();

                getLog().error("error en la peticion:"+ exception.getMessage());
            }

            @Override
            public void onFailException(Exception exception) {
                getLog().error("error en la peticion:"+ exception.getMessage());
            }
        };


        try {


            jsonInString = mapper.writeValueAsString(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTQ5OTAxOTc1OX0.GOSqAFmFO1iwANJzOrBSjP3NrT86oLTOEBVu4M9TtxpV_knaXYX4hXbe_TUUo2rueuCFT-5x_WGNKm9fNDmTEQ");
            HttpEntity<String> entity = new HttpEntity<String>(jsonInString, headers);

            ResponseEntity<String> response = getStringResponseEntity(post,restTemplate, resourceUrl, entity);
            restCallback.callOnSucces(response);

        }
        catch (HttpStatusCodeException exception) {

            restCallback.callOnFailStatus(exception);


        }
        catch (RestClientException | JsonProcessingException e) {
            //e.printStackTrace();

            restCallback.callOnFailException(e);
        }

        getLog().info("terminar correcto************* "+newResource+"****");

        try {

            tNewResource = mapper.readValue(newResource.toString(), clazz);
            getLog().debug("--------- prueba de cliente: {}",tNewResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tNewResource;
    }

    private ResponseEntity<String> getStringResponseEntity(HttpMethod httpMethod , RestTemplate restTemplate, String resourceUrl, HttpEntity<String> entity) {

        if(HttpMethod.POST.equals(httpMethod)){
            return restTemplate.postForEntity(resourceUrl, entity, String.class);
        }
        if(HttpMethod.PUT.equals(httpMethod)){
            return restTemplate.exchange(resourceUrl, HttpMethod.PUT, entity, String.class);
        }

        return restTemplate.getForEntity(resourceUrl, String.class);

    }
}
