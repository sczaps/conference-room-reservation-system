package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    Organization addOrganization(Organization organization) {
        organizationRepository.findByName(organization.getName()).ifPresent(o -> {
            throw new IllegalArgumentException("Organization already exist!");
        });
        return organizationRepository.save(organization);
    }

    Organization getOrganizationByName(String name) {
        return organizationRepository.findByName(name).orElseThrow(() -> new NoSuchElementException(name));
    }

    List<Organization> getAllOrganizations(SortType sortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.name()), "name");
        return organizationRepository.findAll(sort);
    }

    Organization updateOrganization(String name, Organization organization) {
        Organization organizationToUpdate = getOrganizationByName(name);
        if (organization.getName() != null && !organizationToUpdate.getName().equals(organization.getName())) {
            organizationToUpdate.setName(organization.getName());
        }
        if (organization.getDescription() != null) {
            organizationRepository.findByName(organization.getName())
                    .ifPresent(o -> {
                        throw new IllegalArgumentException("Organization already exists!");
                    });
            organizationToUpdate.setDescription(organization.getDescription());
        }
        organizationRepository.save(organizationToUpdate);
        return organizationToUpdate;
    }

    Organization deleteOrganization(String name) {
        Organization organizationToDelete = getOrganizationByName(name);
        organizationRepository.deleteById(organizationToDelete.getId());
        return organizationToDelete;
    }

}
