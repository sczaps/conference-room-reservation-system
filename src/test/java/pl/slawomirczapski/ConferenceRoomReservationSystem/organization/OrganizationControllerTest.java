package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.SortTypeArgumentProvider;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.ValidateAddOrganizationArgumentProvider;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.ValidateUpdateOrganizationArgumentProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    @Test
    void when_add_unique_organization_then_organization_should_be_provided_to_service() throws Exception {
        //given
        Organization organization = new Organization("test1", "desc1");
        Organization addedOrganization = new Organization(1L, "test1", "desc1");
        Mockito.when(organizationService.addOrganization(organization)).thenReturn(addedOrganization);

        //when
        //then
        mockMvc.perform(post("/organizations")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                                        {"name":"test1","description":"desc1"}
                                """))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("test1")))
                .andExpect(jsonPath("$.description", equalTo("desc1")));
    }

    @Test
    void when_add_duplicated_organization_then_bad_request_should_be_returned() throws Exception {
        //given
        Organization organization = new Organization("test1", "desc1");
        Mockito.when(organizationService.addOrganization(organization)).thenThrow(new IllegalArgumentException(("Organization already exists!")));

        //when
        //then
        mockMvc.perform(post("/organizations")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                                        {"name":"test1","description":"desc1"}
                                """))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Organization already exists!")));
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateAddOrganizationArgumentProvider.class)
    void when_add_not_valid_organization_with_arg_0_content_then_bad_request_with_arg_1_description_should_be_thrown(String arg0, List<String> arg1) throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post("/organizations")
                        .contentType(APPLICATION_JSON)
                        .content(arg0))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.name", equalTo(arg1)));
        Mockito.verify(organizationService, Mockito.never()).addOrganization(Mockito.any());
    }

    @Test
    void when_fetch_existing_organization_by_name_then_organization_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Organization organization = new Organization(1L, name, "desc1");
        Mockito.when(organizationService.getOrganizationByName(name)).thenReturn(organization);

        //when
        //then
        mockMvc.perform(get("/organizations/" + name)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.description", equalTo("desc1")));
    }

    @Test
    void when_fetch_non_existing_organization_by_name_then_not_found_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Mockito.when(organizationService.getOrganizationByName(name)).thenThrow(new NoSuchElementException(name));

        //when
        //then
        mockMvc.perform(get("/organizations/" + name)
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("test1")));
    }
    //get(all)

    @Test
    void when_get_empty_organization_list_then_empty_array_should_be_returned() throws Exception {
        //given
        Mockito.when(organizationService.getAllOrganizations(SortType.ASC)).thenReturn(Collections.emptyList());

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/organizations")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @ParameterizedTest
    @ArgumentsSource(SortTypeArgumentProvider.class)
    void when_get_non_empty_organization_list_then_array_with_orgs_should_be_returned(String arg1, SortType arg2) throws Exception {
        //given
        ArgumentCaptor<SortType> sortArgumentCaptor = ArgumentCaptor.forClass(SortType.class);
        Mockito.when(organizationService.getAllOrganizations(arg2)).thenReturn(
                Arrays.asList(
                        new Organization("Intive", "IT company"),
                        new Organization("Tieto", "Delivery company")
                )
        );

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/organizations" + arg1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
        Mockito.verify(organizationService).getAllOrganizations(sortArgumentCaptor.capture());
        Assertions.assertEquals(arg2, sortArgumentCaptor.getValue());
    }

    @Test
    void when_update_non_existing_organization_then_not_found_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Organization organization = new Organization(name, "desc1");
        Mockito.when(organizationService.updateOrganization(name, organization)).thenThrow(new NoSuchElementException(name));

        //when
        //then
        mockMvc.perform(put("/organizations/" + name)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                                        {"name":"test1","description":"desc1"}
                                """))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("test1")));

    }

    @Test
    void when_update_existing_organization_then_organization_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Organization organization = new Organization(name, "desc1");
        Organization updatedOrganization = new Organization(1L, name, "desc1");
        Mockito.when(organizationService.updateOrganization(name, organization)).thenReturn(updatedOrganization);

        //when
        //then
        mockMvc.perform(put("/organizations/" + name)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                                        {"name":"test1","description":"desc1"}
                                """))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("test1")))
                .andExpect(jsonPath("$.description", equalTo("desc1")));
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateUpdateOrganizationArgumentProvider.class)
    void when_update_organization_arg1_then_validation_should_happen(String arg1, boolean result, List<String> arg2) throws Exception {
        //given
        String existingOrgName = "Tieto";

        //when
        //then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/organizations/" + existingOrgName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(arg1)
        );
        if (result) {
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } else {
            resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.details.name", equalTo(arg2)));
        }
    }

    @Test
    void when_update_existing_organization_with_empty_name_and_empty_description_then_illegal_argument_exception_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Organization organization = new Organization("", "");
        Mockito.when(organizationService.updateOrganization(name, organization)).thenThrow(new IllegalArgumentException());

        //when
        //then
        mockMvc.perform(put("/organizations/" + name)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                                        {"name":"","description":""}
                                """))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void when_remove_non_existing_organization_then_not_found_error_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Mockito.when(organizationService.deleteOrganization(name)).thenThrow(new NoSuchElementException(name));

        //when
        //then
        mockMvc.perform(delete("/organizations/" + name)
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("test1")));
        ;
    }

    @Test
    void when_remove_existing_organization_then_deleted_organization_should_be_returned() throws Exception {
        //given
        String name = "test1";
        Organization organization = new Organization(1L, name, "desc1");
        Mockito.when(organizationService.deleteOrganization(name)).thenReturn(organization);

        //when
        //then
        mockMvc.perform(delete("/organizations/" + name)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.description", equalTo("desc1")));
    }
}