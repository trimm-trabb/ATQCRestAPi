package com.automation.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;

public class WebClient {

    private Client client;
    private WebTarget target;

    public WebClient(String accessToken, String uri) {
        Feature feature = OAuth2ClientSupport.feature(accessToken);
        client = ClientBuilder.newBuilder().register(feature).build();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        target = client.target(uri);
    }

    public WebTarget setPath(String path) {
        if (target != null) {
            return target.path(path);
        }
        return null;
    }

    public Response sendGetRequest(WebTarget target) {
        return target.request().get();
    }

    public Response sendPostRequest(WebTarget target, String json) {
        return target.request().post(Entity.json(json));
    }

    public Response sendPutRequest(WebTarget target, String json) {
        return target.request().put(Entity.json(json));
    }

    public Response sendDeleteRequest(WebTarget target, String json) {
        return target.request().method("DELETE", Entity.json(json));
    }
}