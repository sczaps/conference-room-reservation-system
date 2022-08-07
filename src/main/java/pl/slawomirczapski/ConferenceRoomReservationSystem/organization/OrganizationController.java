package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;

import java.util.List;

@RestController
@RequestMapping("/organizations")
class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    OrganizationDto addOrganization(@Validated(value = AddOrganization.class) @RequestBody OrganizationDto organizationDto) {
        return organizationService.addOrganization(organizationDto);
    }

    @GetMapping("/{name}")
    OrganizationDto findOrganizationByName(@PathVariable String name) {
        return organizationService.getOrganizationByName(name);
    }

    @GetMapping
    List<OrganizationDto> findAllOrganizations(@RequestParam(defaultValue = "ASC") SortType sortType) {
        return organizationService.getAllOrganizations(sortType);
    }

    @PutMapping("/{name}")
    OrganizationDto updateOrganizationById(@PathVariable String name, @Validated(value = UpdateOrganization.class) @RequestBody OrganizationDto organizationDto) {
        return organizationService.updateOrganization(name, organizationDto);
    }

    @DeleteMapping("/{name}")
    OrganizationDto deleteOrganizationById(@PathVariable String name) {
        return organizationService.deleteOrganization(name);
    }


}
