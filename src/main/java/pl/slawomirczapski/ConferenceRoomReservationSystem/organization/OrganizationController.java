package pl.slawomirczapski.ConferenceRoomReservationSystem.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.slawomirczapski.ConferenceRoomReservationSystem.Error;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;

import java.util.*;

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
    Organization findOrganizationByName(@PathVariable String name) {
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
    ResponseEntity<Error<String>> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new Error<>(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<Error<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new Error<>(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Error<Map<String, List<String>>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            List<String> params = errors.getOrDefault(fieldName, new ArrayList<>());
            params.add(errorMessage);
            errors.put(fieldName, params);
        });
        return new ResponseEntity<>(new Error<>(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors
        ), HttpStatus.BAD_REQUEST);
    }
}
