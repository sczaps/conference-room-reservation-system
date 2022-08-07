package pl.slawomirczapski.ConferenceRoomReservationSystem.conferenceRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    List<ConferenceRoomDto> findAllConferenceRooms() {
        return conferenceRoomService.getAllConferenceRooms();
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
