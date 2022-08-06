package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.SortOrganizationArgumentProvider;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.UpdateOrganizationArgumentProvider;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrganizationServiceTest {

    OrganizationRepository organizationRepository = Mockito.mock(OrganizationRepository.class);

    OrganizationService organizationService = new OrganizationService(organizationRepository);

    @ParameterizedTest
    @ArgumentsSource(SortOrganizationArgumentProvider.class)
    void when_get_all_with_arg_0_sort_type_then_order_info_from_arg_1_should_be_provided_to_repo(SortType arg0, Sort arg1) {
        //given
        ArgumentCaptor<Sort> sortArgumentCaptor = ArgumentCaptor.forClass(Sort.class);

        //when
        organizationService.getAllOrganizations(arg0);

        //then
        Mockito.verify(organizationRepository).findAll(sortArgumentCaptor.capture());
        assertEquals(arg1, sortArgumentCaptor.getValue());
    }

    @Test
    void when_add_unique_name_organization_then_organization_should_be_added_to_the_repo() {
        //given
        String name = "test5";
        Organization organization = new Organization(1L, name, "desc5");
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.empty());
        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

        //when
        Organization results = organizationService.addOrganization(organization);

        //then
        assertEquals(organization, results);
        Mockito.verify(organizationRepository, Mockito.times(1)).findByName(Mockito.any());
        Mockito.verify(organizationRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void when_add_non_unique_name_organization_then_exception_should_be_thrown() {
        //given
        String name = "test1";
        Organization organization = new Organization(1L, name, "desc1");
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.of(organization));

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addOrganization(organization);
        });
    }

    @Test
    void when_fetch_organization_by_existed_id_then_organization_should_be_returned() {
        //given
        String name = "test1";
        Organization organization = new Organization(1L, name, "desc1");
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.of(organization));


        //when
        Organization results = organizationService.getOrganizationByName(name);

        //then
        assertNotNull(organization);
        assertEquals(organization, results);
        Mockito.verify(organizationRepository, Mockito.times(1)).findByName(Mockito.any());
        Mockito.verify(organizationRepository, Mockito.never()).findAll();
    }

    @Test
    void when_fetch_organization_by_non_existed_id_then_exception_should_be_thrown() {
        //given
        String name = "test1";
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> {
            organizationService.getOrganizationByName(name);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateOrganizationArgumentProvider.class)
    void when_update_arg_1_organization_with_arg_2_data_then_organization_should_be_updated_to_arg_3(
            String name,
            Organization arg1,
            Organization arg2,
            Organization arg3
    ) {
        //given
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.of(arg1));
        Mockito.when(organizationRepository.save(arg1)).thenReturn(arg3);

        //when
        Organization result = organizationService.updateOrganization(name, arg2);

        //then
        assertEquals(arg3, result);
        Mockito.verify(organizationRepository).save(arg3);
    }


    @Test
    void when_update_non_exist_organization_then_exception_should_be_thrown() {
        //given
        String name = "test5";
        Organization organization = new Organization(1L, name, "desc5");
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> {
            organizationService.updateOrganization(name, organization);
        });
        Mockito.verify(organizationRepository, Mockito.times(1)).findByName(Mockito.any());
        Mockito.verify(organizationRepository, Mockito.never()).deleteById(Mockito.any());
    }

    @Test
    void when_update_organization_name_which_is_not_unique_then_exception_should_be_thrown() {
        //given
        String name1 = "test1";
        Organization existingOrganization1 = new Organization(1L, name1, "desc1");
        String name2 = "test2";
        Organization existingOrganization2 = new Organization(2L, name2, "desc2");
        Organization updateOrganization = new Organization(name2, "desc1");

        Mockito.when(organizationRepository.findByName(name1)).thenReturn(Optional.of(existingOrganization1));
        Mockito.when(organizationRepository.findByName(name2)).thenReturn(Optional.of(existingOrganization2));

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            organizationService.updateOrganization(name1, updateOrganization);
        });
        Mockito.verify(organizationRepository, Mockito.never()).save(updateOrganization);
    }

    @Test
    void when_delete_exist_organization_then_organization_should_be_deleted_from_repo() {
        //given
        String name = "test5";
        Organization organization = new Organization(1L, name, "desc5");
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.of(organization));

        //when
        Organization results = organizationService.deleteOrganization(name);

        //then
        assertEquals(organization, results);
        Mockito.verify(organizationRepository, Mockito.times(1)).findByName(Mockito.any());
        Mockito.verify(organizationRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    void when_delete_non_exist_organization_then_exception_should_be_thrown() {
        //given
        String name = "test5";
        Mockito.when(organizationRepository.findByName(name)).thenReturn(Optional.empty());

        //when
        //then
        assertThrows(NoSuchElementException.class, () -> {
            organizationService.deleteOrganization(name);
        });
        Mockito.verify(organizationRepository, Mockito.times(1)).findByName(Mockito.any());
        Mockito.verify(organizationRepository, Mockito.never()).deleteById(Mockito.any());
    }

}
