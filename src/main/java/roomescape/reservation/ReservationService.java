package roomescape.reservation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
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

    @Transactional
    public ReservationResponse save(ReservationRequest request, Member member) {
        String name = resolveName(request, member);

        // 2. 실제 객체 조회 (핵심!): ID 숫자가 아닌 '영속 상태의 객체'를 찾아옵니다.
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        // 3. 찾아온 객체들을 연결하여 생성 (Transient 에러 해결)
        Reservation reservation = new Reservation(name, request.getDate(), time, theme);

        // 4. 저장 (JPA가 @JoinColumn을 통해 자동으로 FK를 채워줍니다)
        reservationRepository.save(reservation);

        return ReservationResponse.from(reservation);
    }

    private String resolveName(ReservationRequest request, Member member) {
        if (request.getName() != null && !request.getName().isBlank()) {
            return request.getName();
        }
        return member.getName();
    }


    public Reservation save(ReservationRequest request) {
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다. ID: " + request.getTime()));

        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다. ID: " + request.getTheme()));



        Reservation reservation = new Reservation(
                request.getName(),
                request.getDate(),
                time,
                theme
        );

        reservationRepository.save(reservation);
        return reservation;
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
