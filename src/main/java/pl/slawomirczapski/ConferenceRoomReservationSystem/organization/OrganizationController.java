package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    @Valid
    Organization addOrganization(@RequestBody Organization organization) {
        return organizationService.addOrganization(organization);
    }

    @GetMapping("/{id}")
    Organization findOrganizationById(@PathVariable String id) {
        return organizationService.getOrganizationById(id);
    }

    @GetMapping
    List<Organization> findAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @PutMapping("/{id}")
    @Valid
    Organization updateOrganizationById(@PathVariable String id, Organization organization) {
        return organizationService.updateOrganization(id, organization);
    }

    @DeleteMapping("/{id}")
    Organization deleteOrganizationById(@PathVariable String id) {
        return organizationService.deleteOrganization(id);
    }
}
