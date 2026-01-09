package com.acme.jga.rest.controllers;

import com.acme.jga.config.AppGenericProperties;
import com.acme.jga.domain.model.organization.OrganizationKind;
import com.acme.jga.domain.model.organization.OrganizationStatus;
import com.acme.jga.rest.dtos.v1.organizations.OrganizationDto;
import com.acme.jga.rest.dtos.v1.tenants.UidDto;
import com.acme.jga.rest.services.organizations.api.AppOrganizationsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebSecurity(debug = true)
@WebMvcTest(controllers = OrganizationsController.class)
class OrganizationsControllerTest {
    private static final String TENANT_UID = UUID.randomUUID().toString();
    @MockitoBean
    private AppOrganizationsService organizationPortService;
    @MockitoBean
    private AppGenericProperties appGenericProperties;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void createOrganization() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // GIVEN
        UidDto uidDto = new UidDto(UUID.randomUUID().toString());
        OrganizationDto organizationDto = new OrganizationDto(null, "org-code", "org-label", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        ObjectMapper objectMapper = new ObjectMapper();
        String orgJson = objectMapper.writeValueAsString(organizationDto);

        // WHEN
        Mockito.when(organizationPortService.createOrganization(Mockito.any(), Mockito.any())).thenReturn(uidDto);

        // THEN
        String targetUri = "/api/v1/tenants/" + TENANT_UID + "/organizations";
        mockMvc.perform(post(targetUri)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(orgJson)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uid", "").exists())
                .andExpect(jsonPath("$.uid", "").value(uidDto.getUid()));
    }

    @Test
    void updateOrganization() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // GIVEN
        String uid = UUID.randomUUID().toString();
        OrganizationDto organizationDto = new OrganizationDto(uid, "org-code", "org-label", OrganizationKind.COMMUNITY, "fr", OrganizationStatus.ACTIVE);
        ObjectMapper objectMapper = new ObjectMapper();
        String orgJson = objectMapper.writeValueAsString(organizationDto);

        // WHEN

        // THEN
        String targetUri = "/api/v1/tenants/" + TENANT_UID + "/organizations/" + uid;
        mockMvc.perform(post(targetUri)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(orgJson)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteOrganization() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // GIVEN
        UidDto uidDto = new UidDto(UUID.randomUUID().toString());

        // WHEN

        // THEN
        String targetUri = "/api/v1/tenants/" + TENANT_UID + "/organizations/" + uidDto.getUid();
        mockMvc.perform(delete(targetUri)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNoContent());
    }
}