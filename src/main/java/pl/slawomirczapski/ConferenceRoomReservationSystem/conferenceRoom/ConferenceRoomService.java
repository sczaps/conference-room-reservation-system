package pl.slawomirczapski.ConferenceRoomReservationSystem.conferenceRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.Organization;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.OrganizationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
class ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final OrganizationRepository organizationRepository;
    private final ConferenceRoomUpdater conferenceRoomUpdater;
    private final ConferenceRoomTransformer conferenceRoomTransformer;

    @Autowired
    ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository, OrganizationRepository organizationRepository, ConferenceRoomUpdater conferenceRoomUpdater, ConferenceRoomTransformer conferenceRoomTransformer) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.organizationRepository = organizationRepository;
        this.conferenceRoomUpdater = conferenceRoomUpdater;
        this.conferenceRoomTransformer = conferenceRoomTransformer;
    }

    ConferenceRoomDto addConferenceRoom(ConferenceRoomDto conferenceRoomDto) {
        ConferenceRoom conferenceRoom = conferenceRoomTransformer.fromDto(conferenceRoomDto);
        String organizationName = conferenceRoom.getOrganization().getName();
        Organization organizationFromRepo = organizationRepository.findByName(organizationName)
                .orElseThrow(() -> new NoSuchElementException("No organization found!"));
        conferenceRoom.setOrganization(organizationFromRepo);
        conferenceRoomRepository.findByNameAndOrganization_Name(conferenceRoom.getName(),
                        organizationName)
                .ifPresent(cr -> {
                    throw new IllegalArgumentException("Conference Room already exist!");
                });
        return conferenceRoomTransformer.toDto(conferenceRoomRepository.save(conferenceRoom));
    }

    ConferenceRoomDto getConferenceRoomById(String id) {
        return conferenceRoomRepository.findById(id)
                .map(conferenceRoomTransformer::toDto)
                .orElseThrow(() -> {
                    throw new NoSuchElementException("No conference room found!");
                });
    }

    List<ConferenceRoomDto> getAllConferenceRooms(SortType sortType,
                                                  String identifier,
                                                  Integer level,
                                                  Boolean isAvailable,
                                                  Integer numberOfSeats,
                                                  String organization) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues().withIgnoreCase();
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.name()), "name");
        Example<ConferenceRoom> conferenceRoomExample = Example.of(
                new ConferenceRoom(null, identifier, level, isAvailable, numberOfSeats, new Organization(organization)),
                matcher);
        return conferenceRoomRepository.findAll(conferenceRoomExample, sort)
                .stream()
                .map(conferenceRoomTransformer::toDto)
                .collect(Collectors.toList());
    }

    ConferenceRoomDto updateConferenceRoom(String id, ConferenceRoomDto conferenceRoomDto) {
        ConferenceRoom conferenceRoom = conferenceRoomTransformer.fromDto(conferenceRoomDto);
        return conferenceRoomTransformer.toDto(conferenceRoomUpdater.update(id, conferenceRoom));
    }

    ConferenceRoomDto deleteConferenceRoom(String id) {
        ConferenceRoom conferenceRoomToDelete = conferenceRoomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No conference room found!"));
        conferenceRoomRepository.deleteById(id);
        return conferenceRoomTransformer.toDto(conferenceRoomToDelete);
    }
}

