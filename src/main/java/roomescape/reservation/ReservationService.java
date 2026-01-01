package roomescape.reservation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse saveByMember(ReservationRequest request, Member member) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        Reservation reservation = new Reservation(member.getName(), request.getDate(), time, theme, member);
        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation);
    }

    public ReservationResponse saveByAdmin(ReservationRequest request) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        Reservation reservation = new Reservation(request.getName(), request.getDate(), time, theme);
        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public List<MyReservationResponse> findByMember(Member member) {
        return reservationRepository.findByMemberId(member.getId()).stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    private Theme findTheme(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("테마 없음"));
    }

    private Time findTime(Long id) {
        return timeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시간 없음"));
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
