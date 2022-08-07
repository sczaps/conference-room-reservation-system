package pl.slawomirczapski.ConferenceRoomReservationSystem.conferenceRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.Organization;
import pl.slawomirczapski.ConferenceRoomReservationSystem.organization.OrganizationRepository;

import java.util.NoSuchElementException;

@Component
class ConferenceRoomUpdater {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final OrganizationRepository organizationRepository;

    @Autowired
    public ConferenceRoomUpdater(ConferenceRoomRepository conferenceRoomRepository, OrganizationRepository organizationRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.organizationRepository = organizationRepository;
    }

    ConferenceRoom update(String id, ConferenceRoom conferenceRoom) {
        ConferenceRoom conferenceRoomToUpdate = conferenceRoomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No conference room found!"));
        boolean isNameUpdated = updateName(conferenceRoomToUpdate, conferenceRoom);
        updateAvailability(conferenceRoomToUpdate, conferenceRoom);
        updateIdentifier(conferenceRoomToUpdate, conferenceRoom);
        updateNumberOfSeats(conferenceRoomToUpdate, conferenceRoom);
        updateLevel(conferenceRoomToUpdate, conferenceRoom);
        boolean isOrganizationUpdated = updateOrganization(conferenceRoomToUpdate, conferenceRoom);
        checkIfConferenceRoomIsUnique(conferenceRoomToUpdate, isNameUpdated, isOrganizationUpdated);
        return conferenceRoomRepository.save(conferenceRoomToUpdate);
    }

    private void checkIfConferenceRoomIsUnique(ConferenceRoom conferenceRoomToUpdate,
                                               boolean isNameUpdated,
                                               boolean isOrganizationUpdated) {
        if (!isNameUpdated && !isOrganizationUpdated) {
            return;
        }
        conferenceRoomRepository.findByNameAndOrganization_Name(conferenceRoomToUpdate.getName(),
                        conferenceRoomToUpdate.getOrganization().getName())
                .ifPresent(cr -> {
                    throw new IllegalArgumentException("Conference room already exists!");
                });
    }

    private boolean updateName(ConferenceRoom conferenceRoomToUpdate, ConferenceRoom conferenceRoom) {
        String name = conferenceRoom.getName();
        if (name != null) {
            conferenceRoomToUpdate.setName(name);
            return true;
        }
        return false;
    }

    private void updateAvailability(ConferenceRoom conferenceRoomToUpdate, ConferenceRoom conferenceRoom) {
        Boolean isAvailable = conferenceRoom.getAvailable();
        if (isAvailable != null) {
            conferenceRoomToUpdate.setAvailable(isAvailable);
        }
    }

    private void updateIdentifier(ConferenceRoom conferenceRoomToUpdate, ConferenceRoom conferenceRoom) {
        String identifier = conferenceRoom.getIdentifier();
        if (identifier != null) {
            conferenceRoomToUpdate.setIdentifier(identifier);
        }
    }

    private void updateNumberOfSeats(ConferenceRoom conferenceRoomToUpdate, ConferenceRoom conferenceRoom) {
        Integer numberOfSeats = conferenceRoom.getNumberOfSeats();
        if (numberOfSeats != null) {
            conferenceRoomToUpdate.setNumberOfSeats(numberOfSeats);
        }
    }

    private void updateLevel(ConferenceRoom conferenceRoomToUpdate, ConferenceRoom conferenceRoom) {
        Integer level = conferenceRoom.getLevel();
        if (level != null) {
            conferenceRoomToUpdate.setLevel(level);
        }
    }

    private boolean updateOrganization(ConferenceRoom conferenceRoomToUpdate, ConferenceRoom conferenceRoom) {
        if (conferenceRoom.getOrganization() != null) {
            Organization organizationFromRepo = organizationRepository.findByName(conferenceRoom.getOrganization().getName())
                    .orElseThrow(() -> new NoSuchElementException("No organization found!"));
            conferenceRoomToUpdate.setOrganization(organizationFromRepo);
            return true;
        }
        return false;
    }

}
