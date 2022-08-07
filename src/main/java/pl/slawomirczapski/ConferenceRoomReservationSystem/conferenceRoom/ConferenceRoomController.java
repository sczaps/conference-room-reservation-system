package pl.slawomirczapski.ConferenceRoomReservationSystem.conferenceRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.slawomirczapski.ConferenceRoomReservationSystem.SortType;

import java.util.List;

@RestController
@RequestMapping("/conferenceRooms")
class ConferenceRoomController {

    private final ConferenceRoomService conferenceRoomService;

    @Autowired
    ConferenceRoomController(ConferenceRoomService conferenceRoomService) {
        this.conferenceRoomService = conferenceRoomService;
    }

    @PostMapping
    ConferenceRoomDto addConferenceRoom(@Validated(value = AddConferenceRoom.class)
                                        @RequestBody ConferenceRoomDto conferenceRoomDto) {
        return conferenceRoomService.addConferenceRoom(conferenceRoomDto);
    }

    @GetMapping("/{id}")
    ConferenceRoomDto getConferenceRoomById(@PathVariable String id) {
        return conferenceRoomService.getConferenceRoomById(id);
    }

    @GetMapping
    List<ConferenceRoomDto> findAllConferenceRooms(@RequestParam(defaultValue = "ASC") SortType sortType,
                                                   @RequestParam(required = false) String identifier,
                                                   @RequestParam(required = false) Integer level,
                                                   @RequestParam(required = false) Boolean isAvailable,
                                                   @RequestParam(required = false) Integer numberOfSeats,
                                                   @RequestParam(required = false) String organization) {
        return conferenceRoomService.getAllConferenceRooms(sortType, identifier, level, isAvailable, numberOfSeats, organization);
    }

    @PutMapping("/{id}")
    ConferenceRoomDto updateConferenceRoomById(@PathVariable String id,
                                               @Validated(value = UpdateConferenceRoom.class) @RequestBody ConferenceRoomDto conferenceRoomDto) {
        return conferenceRoomService.updateConferenceRoom(id, conferenceRoomDto);
    }

    @DeleteMapping("/{id}")
    ConferenceRoomDto deleteConferenceRoom(@PathVariable String id) {
        return conferenceRoomService.deleteConferenceRoom(id);
    }
}
