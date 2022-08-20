package pl.slawomirczapski.ConferenceRoomReservationSystem.reservation;

import org.springframework.stereotype.Component;
import pl.slawomirczapski.ConferenceRoomReservationSystem.conferenceRoom.ConferenceRoom;

@Component
public class ReservationTransformer {

    public ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getReservationName(),
                reservation.getConferenceRoom().getId()
        );
    }

    public Reservation fromDto(ReservationDto reservationDto) {
        return new Reservation(
                reservationDto.getId(),
                reservationDto.getStartDate(),
                reservationDto.getEndDate(),
                reservationDto.getReservationName(),
                new ConferenceRoom(reservationDto.getConferenceRoomId())
        );
    }
}
