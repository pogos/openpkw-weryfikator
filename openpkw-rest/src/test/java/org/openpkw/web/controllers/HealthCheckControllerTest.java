package org.openpkw.web.controllers;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.codec.binary.Base64;
import org.openpkw.web.config.TestAppConfig;
import org.openpkw.web.config.TestJpaConfig;
import org.openpkw.web.configuration.MVCConfig;
import org.openpkw.web.configuration.OAuth2ServerConfiguration;
import org.openpkw.web.configuration.SecurityConfig;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.servlet.Filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Security test for health check controller
 * @author Sebastian Pogorzelski
 */
@ContextConfiguration(classes = {TestJpaConfig.class,TestAppConfig.class, MVCConfig.class, SecurityConfig.class, OAuth2ServerConfiguration.class})
@WebAppConfiguration
public class HealthCheckControllerTest extends AbstractTestNGSpringContextTests {

    @Inject
    private WebApplicationContext context;

    @Inject
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @BeforeClass
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    /*

     var data = "username=" +  encodeURIComponent(credentials.username) + "&password="
                    + encodeURIComponent(credentials.password) + "&grant_type=password&scope=read%20write&" +
                    "client_secret=mySecretOAuthSecret&client_id=oauth_sampleapp";

     */

    private MvcResult authenticate() throws Exception {
        return mvc.perform(post("/oauth/token")
                .param("username", "admin@openpkw.pl")
                .param("password", "admin")
                .param("grant_type", "password")
//                        .param("scope", "read write")
                .param("client_secret", "secret")
                .param("client_id", "openpkw")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + new String(Base64.encodeBase64(("openpkw:secret").getBytes())))

                )
//                .andExpect(status().isOk())
                        //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        //.andExpect(jsonPath("token").exists())

//        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("<username>:<password>").getBytes()));
//        this.mockMvc.perform(get("</get/url>").header("Authorization", basicDigestHeaderValue).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

                .andReturn();
    }

    @Test
    public void shouldGetHealthCheckSimpleTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/healthCheck/simpleTest")).andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("HealthCheck Simple Test OK");
    }

    @Test
    public void shouldGetHealthCheckDataTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/healthCheck/dataTest")).andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("HealthCheck Data Test OK");
    }

    @Test
    public void shouldHealthCheckSecureTestReturn401WithoutAuthenticate() throws Exception {
        mvc.perform(get("/healthCheck/securityTest")).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldGetHealthCheckSecureTestWithAuthenticate() throws Exception {

        MvcResult authMvcResult = authenticate();
        String content = authMvcResult.getResponse().getContentAsString();
        String token = JsonPath.read(content, "token");

        MvcResult mvcResult = mvc.perform(get("/healthCheck/securityTest").header("x-auth-token", token))
                .andExpect(status().isOk()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("HealthCheck Security Test OK");
    }

}
