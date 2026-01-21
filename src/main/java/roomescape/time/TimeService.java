package roomescape.time;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.reservation.waiting.WaitingRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }


    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    @Transactional
    public Time save(Time time) {
        return timeRepository.save(time);
    }

    @Transactional
    public void deleteById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간대에 예약이 존재하여 삭제할 수 없습니다.");
        }

        if (waitingRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("해당 시간대에 대기 중인 사용자가 있어 삭제할 수 없습니다.");
        }

        // 3. 연관 데이터가 없을 때만 삭제(Soft Delete) 진행
        timeRepository.deleteById(id);
    }
}
