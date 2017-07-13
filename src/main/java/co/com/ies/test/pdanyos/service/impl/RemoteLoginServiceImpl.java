package co.com.ies.test.pdanyos.service.impl;

import co.com.ies.test.pdanyos.repository.AbstractRestCallback;
import co.com.ies.test.pdanyos.repository.AbstractRestRepository;
import co.com.ies.test.pdanyos.service.RemoteLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;

@Service
@Transactional
public class RemoteLoginServiceImpl implements RemoteLoginService {

    public static final String ID_TOKEN = "id_token";
    private final Logger log = LoggerFactory.getLogger(RemoteLoginServiceImpl.class);

    @Override
    public boolean loginRemoto(AbstractRestRepository.CredencialesRest credencialesRest, Login login) {
        boolean loginOk = false;
        String resourceUrl = credencialesRest.getUrl();
        String newToken = postRequest(resourceUrl,login);
        if(newToken != null && !"".equals(newToken)){
            credencialesRest.setToken(newToken);
            loginOk = true;
        }

        return loginOk;
    }

    private String postRequest(String resourceUrl,Login login){
        String token = null;

        String urlAutenticate = resourceUrl + "authenticate";

        String tokenObject = requestAccion(urlAutenticate, login);

        ObjectMapper mapper = new ObjectMapper();

        try {
            final JsonNode jsonNode = mapper.readTree(tokenObject);

            log.debug(jsonNode.toString());
            if (jsonNode != null) {
                final JsonNode id_token = jsonNode.path(ID_TOKEN);
                log.debug(""+id_token);
                token = id_token.asText();
            }
            log.debug(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    private String requestAccion(String resourceUrl, Login login) {
        RestTemplate restTemplate = new RestTemplate();

        final StringBuilder newResource = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(login);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = null;


                entity = new HttpEntity<String>(jsonInString, headers);


            ResponseEntity<String> stringResponseEntity = restTemplate.exchange(resourceUrl, HttpMethod.POST, entity, String.class);
            HttpStatus statusCode = stringResponseEntity.getStatusCode();
            String bodyResponse = stringResponseEntity.getBody();

            final String responseMsg = "crearRecargaWPlay::status=" + statusCode.name() + ",bodyResponse"
                + bodyResponse;

            log.info(responseMsg);

            if (HttpStatus.OK.equals(statusCode) || HttpStatus.CREATED.equals(statusCode)) {

                log.info("request correcto************* "+newResource+"****");
                newResource.append(bodyResponse);
                log.info("request correcto************* "+newResource+"****");
            }

        }
        catch (HttpStatusCodeException exception) {

            exception.printStackTrace();
        }
        catch (RestClientException e) {
            e.printStackTrace();

        }

        log.info("terminar correcto************* "+newResource+"****");

        return newResource.toString();
    }

    public static class Login implements Serializable{
        public String username;
        public String password;

        public Login() {
            // . para compatibilidad
        }

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
