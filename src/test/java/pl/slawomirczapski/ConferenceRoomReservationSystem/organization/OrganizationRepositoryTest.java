package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.GetAllOrganizationArgumentProvider;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.args.GetByIdOrganizationArgumentProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrganizationRepositoryTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    void should_add_1_organization_when_save_via_repository() {
        //given
        Organization organization = new Organization("test1", "desc1");

        //when
        organizationRepository.save(organization);

        //then
        Organization result = testEntityManager.find(Organization.class, "test1");
        assertEquals(organization, result);
    }

    @Test
    void when_save_arg_0_with_wrong_primary_key_then_exception_should_be_thrown() {
        //given
        Organization arg0 = new Organization(null, "desc1");

        //when
        //then
        assertThrows(JpaSystemException.class, ()->{
            organizationRepository.save(arg0);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(GetAllOrganizationArgumentProvider.class)
    void when_arg_0_data_are_available_in_repo_then_find_all_should_return_arg_1_list(List<Organization> arg0, List<Organization> arg1) {
        //given
        arg0.forEach(o -> testEntityManager.persist(o));
        //when
        List<Organization> results = organizationRepository.findAll();

        //then
        assertEquals(arg1, results);

    }

    @ParameterizedTest
    @ArgumentsSource(GetByIdOrganizationArgumentProvider.class)
    void when_find_by_arg_1_when_arg_0_list_is_available_then_arg_2_item_should_be_returned(List<Organization> arg0,
                                                                                            String arg1,
                                                                                            Optional<Organization> arg2) {
        //given
        arg0.forEach(o -> testEntityManager.persist(o));

        //when
        Optional<Organization> result = organizationRepository.findById(arg1);

        //then
        assertEquals(arg2, result);
    }

}