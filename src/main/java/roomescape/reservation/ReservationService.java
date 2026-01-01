package roomescape.reservation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {

        String reservationName;
        if (reservationRequest.getName() == null) {
            reservationName = member.getName();
        } else {
            reservationName = reservationRequest.getName();
        }

        Reservation reservation = reservationRepository.save(reservationRequest, reservationName);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                                    it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
