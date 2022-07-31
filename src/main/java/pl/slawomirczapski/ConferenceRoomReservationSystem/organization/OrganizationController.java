package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    Organization addOrganization(@Validated(value = AddOrganization.class) @RequestBody Organization organization) {
        return organizationService.addOrganization(organization);
    }

    @GetMapping("/{name}")
    Organization findOrganizationById(@PathVariable String name) {
        return organizationService.getOrganizationByName(name);
    }

    @GetMapping
    List<Organization> findAllOrganizations(@RequestParam(defaultValue = "ASC") SortType sortType) {
        return organizationService.getAllOrganizations(sortType);
    }

    @PutMapping("/{name}")
    Organization updateOrganizationById(@PathVariable String name, @Validated(value = UpdateOrganization.class) @RequestBody Organization organization) {
        return organizationService.updateOrganization(name, organization);
    }

    @DeleteMapping("/{name}")
    Organization deleteOrganizationById(@PathVariable String name) {
        return organizationService.deleteOrganization(name);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<Object> exception(NoSuchElementException exception) {
        return new ResponseEntity<>(String.format("Organization with id %s not found", exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exception(IllegalArgumentException exception) {
        return new ResponseEntity<>(String.format(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
