package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationTransformer organizationTransformer;

    @Autowired
    OrganizationService(OrganizationRepository organizationRepository, OrganizationTransformer organizationTransformer) {
        this.organizationRepository = organizationRepository;
        this.organizationTransformer = organizationTransformer;
    }

    OrganizationDto addOrganization(OrganizationDto organizationDto) {
        Organization organization = organizationTransformer.fromDto(organizationDto);
        organizationRepository.findByName(organization.getName()).ifPresent(o -> {
            throw new IllegalArgumentException("Organization already exist!");
        });
        return organizationTransformer.toDto(organizationRepository.save(organization));
    }

    OrganizationDto getOrganizationByName(String name) {
        return organizationTransformer.toDto(getOrganizationEntityByName(name));
    }

    List<OrganizationDto> getAllOrganizations(SortType sortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.name()), "name");
        return organizationRepository.findAll(sort)
                .stream()
                .map(organizationTransformer::toDto)
                .collect(Collectors.toList());
    }

    OrganizationDto updateOrganization(String name, OrganizationDto organizationDto) {
        Organization organization = organizationTransformer.fromDto(organizationDto);
        Organization organizationToUpdate = getOrganizationEntityByName(name);
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
        return organizationTransformer.toDto(organizationToUpdate);
    }

    OrganizationDto deleteOrganization(String name) {
        OrganizationDto organizationToDelete = getOrganizationByName(name);
        organizationRepository.deleteById(organizationToDelete.getId());
        return organizationToDelete;
    }

    private Organization getOrganizationEntityByName(String name) {
        return organizationRepository.findByName(name).orElseThrow(() -> new NoSuchElementException(name));
    }

}
