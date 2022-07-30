package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    Organization addOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    Organization getOrganizationById(String id) {
        return organizationRepository.findById(id).orElseThrow(() -> new NoSuchElementException(String.valueOf(id)));
    }

    List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    Organization updateOrganization(String id, Organization organization) {
        Organization organizationToUpdate = getOrganizationById(id);
        if (organization.getName() != null) {
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
