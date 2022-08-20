package pl.slawomirczapski.ConferenceRoomReservationSystem.reservation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    ReservationDto addReservation(@Validated(value = AddReservation.class) @RequestBody ReservationDto reservationDto) {
        return reservationService.addReservation(reservationDto);
    }

    @PutMapping("/{id}")
    ReservationDto updateReservation(@PathVariable String id, @Validated(value = UpdateReservation.class) @RequestBody ReservationDto reservationDto) {
        return reservationService.updateReservation(id, reservationDto);
    }
}
