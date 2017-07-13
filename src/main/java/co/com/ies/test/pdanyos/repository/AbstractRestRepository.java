package co.com.ies.test.pdanyos.repository;


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
import java.util.List;

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

    public abstract void validarCredenciales();

    public abstract Logger getLog();

    public abstract CredencialesRest getCredenciales();

    T postRequest(T request){
        return postPutRequest(request, HttpMethod.POST );
    }

    T putRequest(T request){
        return postPutRequest(request, HttpMethod.PUT);
    }

    public List<T> getRequest(){
        String resourcePath = getCredenciales().getResourceName();
        String pathApiRoot = getCredenciales().getUrl();
        String resourceUrl = pathApiRoot + resourcePath;
        getLog().debug(resourceUrl);
        return requestAccion(resourceUrl,this.clazz);

    }


    public T getRequest(Long idRequest){
        String resourcePath = getCredenciales().getResourceName();
        String pathApiRoot = getCredenciales().getUrl();
        String resourceUrl = pathApiRoot + resourcePath + "/"+ idRequest;
        getLog().debug(resourceUrl);
        T tNewResource = requestAccion(null, HttpMethod.GET, resourceUrl);

        return tNewResource;
    }

    private T postPutRequest(T request, HttpMethod post){

        String resourcePath = getCredenciales().getResourceName();
        String pathApiRoot = getCredenciales().getUrl();
        String resourceUrl = pathApiRoot + resourcePath;

        T tNewResource = requestAccion(request, post, resourceUrl);

        return tNewResource;
    }

    private T requestAccion(T request, HttpMethod post, String resourceUrl) {

        final StringBuilder newResource = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        T tNewResource = null;
        requestAccion(post, resourceUrl, newResource, jsonInString);

        try {

            tNewResource = mapper.readValue(newResource.toString(), clazz);
            getLog().debug("--------- prueba de cliente: {}",tNewResource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tNewResource;
    }

    private List<T> requestAccion( String resourceUrl, Class<?> target ) {

        final StringBuilder newResource = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();

        List<T> tNewResource = null;
        requestAccion(HttpMethod.GET, resourceUrl, newResource, null);

        try {


            tNewResource = mapper.readValue(newResource.toString(),mapper .getTypeFactory().constructCollectionType(List.class, Class.forName(target.getName())));
            getLog().debug("--------- prueba de cliente: {}",tNewResource);

            Object obj = tNewResource.get(0);

            getLog().info("clase "+obj.getClass().getName());

        } catch (IOException | ClassNotFoundException  e) {
            e.printStackTrace();
        }
        return tNewResource;
    }

    private void requestAccion(HttpMethod post, String resourceUrl, StringBuilder newResource, String jsonInString) {
        RestTemplate restTemplate = new RestTemplate();


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

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer "+getCredenciales().getToken());
            HttpEntity<String> entity = null;

            if(jsonInString != null){
                entity = new HttpEntity<String>(jsonInString, headers);
            }else{
                entity = new HttpEntity<String>( headers);
            }

            ResponseEntity<String> response = getStringResponseEntity(post,restTemplate, resourceUrl, entity);
            restCallback.callOnSucces(response);

        }
        catch (HttpStatusCodeException exception) {

            restCallback.callOnFailStatus(exception);

        }
        catch (RestClientException e) {
            //e.printStackTrace();
            restCallback.callOnFailException(e);
        }

        getLog().info("terminar correcto************* "+newResource+"****");
    }


    private ResponseEntity<String> getStringResponseEntity(HttpMethod httpMethod , RestTemplate restTemplate, String resourceUrl, HttpEntity<String> entity) {

        if(HttpMethod.POST.equals(httpMethod)){
            return restTemplate.postForEntity(resourceUrl, entity, String.class);
        }
        if(HttpMethod.PUT.equals(httpMethod)){
            return restTemplate.exchange(resourceUrl, HttpMethod.PUT, entity, String.class);
        }

        return restTemplate.exchange(resourceUrl, HttpMethod.GET, entity, String.class);

    }

    public static class CredencialesRest{

        private String url;

        private String token;

        private String resourceName;

        public CredencialesRest(String url, String token, String resourceName) {
            this.url = url;
            this.token = token;
            this.resourceName = resourceName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }
    }
}
