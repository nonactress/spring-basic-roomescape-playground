package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
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

        validateNotAlreadyWaiting(member.getId(), request.getDate(), theme.getId(), time.getId());

        if (isReservationAvailable(request.getDate(), theme.getId(), time.getId())) {
            return createReservation(member.getName(), request.getDate(), time, theme, member);
        }

        return createWaiting(member, theme, time, request.getDate());
    }

    @Transactional
    public Long saveByAdmin(ReservationRequest request) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        return createReservation(request.getName(), request.getDate(), time, theme, null);
    }
    
    private void validateNotAlreadyWaiting(Long memberId, String date, Long themeId, Long timeId) {
        if (waitingRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(memberId, date, themeId, timeId)) {
            throw new IllegalArgumentException("이미 대기 신청을 한 타임입니다.");
        }
    }

    private boolean isReservationAvailable(String date, Long themeId, Long timeId) {
        List<Reservation> existing = reservationRepository.findByDateAndThemeIdAndTimeId(date, themeId, timeId);
        return existing.isEmpty();
    }

    private Long createReservation(String name, String date, Time time, Theme theme, Member member) {
        Reservation reservation = new Reservation(name, date, time, theme, member);
        reservationRepository.save(reservation);
        return reservation.getId();
    }

    private Long createWaiting(Member member, Theme theme, Time time, String date) {
        Waiting waiting = new Waiting(member, theme, time, date);
        waitingRepository.save(waiting);
        return waiting.getId();
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
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
            responses.add(new MyReservationResponse(
                    w.getId(),
                    w.getTheme().getName(),
                    w.getDate(),
                    w.getTime().getValue(),
                    wr.getRank() + "번째 예약대기"
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

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
