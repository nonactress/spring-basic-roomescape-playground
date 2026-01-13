package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.waiting.Waiting;
import roomescape.reservation.waiting.WaitingRepository;
import roomescape.reservation.waiting.WaitingWithRank;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public Long saveByMember(ReservationRequest request, Member member) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        if (waitingRepository.existsByMemberAndDateTime(member.getId(), request.getDate(), theme.getId(), time.getId())) {
            throw new IllegalArgumentException("이미 대기 신청을 한 타임입니다.");
        }

        List<Reservation> existing = reservationRepository.findByDateAndThemeId(request.getDate(), theme.getId());

        if (existing.isEmpty()) {
            Reservation reservation = new Reservation(member.getName(), request.getDate(), time, theme, member);
            reservationRepository.save(reservation);
            return reservation.getId();
        }

        Waiting waiting = new Waiting(member, theme, time, request.getDate());
        waitingRepository.save(waiting);
        return waiting.getId();
    }

    @Transactional
    public Long saveByAdmin(ReservationRequest request) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        Reservation reservation = new Reservation(request.getName(), request.getDate(), time, theme);
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    public List<MyReservationResponse> findByMember(Member member) {
        List<MyReservationResponse> responses = new java.util.ArrayList<>(
                reservationRepository.findByMemberId(member.getId()).stream()
                        .map(MyReservationResponse::from)
                        .toList()
        );

        List<WaitingWithRank> waitingsWithRank = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        for (WaitingWithRank wr : waitingsWithRank) {
            Waiting w = wr.getWaiting();
            responses.add(MyReservationResponse.from(w, wr.getRank()
            ));
        }
        return responses;
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
        return reservationRepository.findAll()
                .stream()
                .map(it -> ReservationResponse.from(it))
                .toList();
    }
}
