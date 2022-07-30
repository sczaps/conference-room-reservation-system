package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        organizationRepository.findById(organization.getName()).ifPresent(o -> {
            throw new IllegalArgumentException("Organization already exist!");
        });
        return organizationRepository.save(organization);
    }

    Organization getOrganizationById(String id) {
        return organizationRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id));
    }

    List<Organization> getAllOrganizations(SortType sortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.name()), "name");
        return organizationRepository.findAll(sort);
    }

    Organization updateOrganization(String id, Organization organization) {
        Organization organizationToUpdate = getOrganizationById(id);
        if (organization.getName() != null && !organizationToUpdate.getName().equals(organization.getName())) {
            organizationToUpdate.setName(organization.getName());
        }
        if (organization.getDescription() != null) {
            organizationToUpdate.setDescription(organization.getDescription());
        }
        organizationRepository.save(organizationToUpdate);
        return organizationToUpdate;
    }

    Organization deleteOrganization(String id) {
        Organization organizationToDelete = getOrganizationById(id);
        organizationRepository.deleteById(id);
        return organizationToDelete;
    }

}
