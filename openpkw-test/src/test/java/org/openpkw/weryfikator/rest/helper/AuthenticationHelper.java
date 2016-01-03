package org.openpkw.weryfikator.rest.helper;

import org.openpkw.weryfikator.rest.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.Base64;

/**
 * Utility class for authentication
 * @author Sebastian Pogorzelski
 */
public class AuthenticationHelper {

    public static ResponseDTO login(String email, String password) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(Configuration.getHost() + "/api/login");
        Response response = target.request()
                .header("Authorization", "Basic " + new String(Base64.getEncoder().encode(("openpkw:secret").getBytes())))
                .post(Entity.form(createLoginForm(email, password)), Response.class);
        return new ResponseDTO(response.getStatus(), MessageHelper.getMessageBody(response));
    }

    private static Form createLoginForm(String email, String password) {
        Form form = new Form();
        form.param("username", email);
        form.param("password", password);
        form.param("client_id", "openpkw");
        form.param("grant_type", "password");
        form.param("client_secret", "secret");
        return form;
    }

    public static String retriveToken(ResponseDTO responseDTO) {
        return responseDTO.getResponseBody().get("access_token");
    }

    public static ResponseDTO logout(String email) {
        return null;
    }

//    public static ResponseDTO checkToken(String accessToken) {
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target(Configuration.getHost() + "/oauth/check_token/" + accessToken);
//        Response response = target.request()
//                //.header("Authorization", "Basic " + new String(Base64.getEncoder().encode(("openpkw:secret").getBytes())))
//                .header("Authorization", "Bearer " + accessToken)  //add security token
//                //.post(Entity.form(createLoginForm(email, password)), Response.class);
//                .get(Response.class);
//        return new ResponseDTO(response.getStatus(), MessageHelper.getMessageBody(response));
//    }

}
