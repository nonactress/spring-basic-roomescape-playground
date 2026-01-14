package roomescape.Loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Component
public class ProductionDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public ProductionDataLoader(
            MemberRepository memberRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            ReservationRepository reservationRepository
    ) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. 관리자 계정 생성
        if (memberRepository.findByEmail("admin").isEmpty()) {
            Member admin = new Member("admin", "admin", "admin", "ADMIN");
            memberRepository.save(admin);
            System.out.println("관리자 계정이 생성되었습니다.");
        }

        // 2. 테마 데이터 생성
        if (themeRepository.count() == 0) {
            Theme theme1 = new Theme("테마1", "테마1입니다.");
            Theme theme2 = new Theme("테마2", "테마2입니다.");
            Theme theme3 = new Theme("테마3", "테마3입니다.");

            themeRepository.save(theme1);
            themeRepository.save(theme2);
            themeRepository.save(theme3);
            System.out.println("테마 데이터가 생성되었습니다.");
        }

        // 3. 시간 데이터 생성
        if (timeRepository.count() == 0) {
            Time time1 = new Time("10:00");
            Time time2 = new Time("12:00");
            Time time3 = new Time("14:00");
            Time time4 = new Time("16:00");
            Time time5 = new Time("18:00");
            Time time6 = new Time("20:00");

            timeRepository.save(time1);
            timeRepository.save(time2);
            timeRepository.save(time3);
            timeRepository.save(time4);
            timeRepository.save(time5);
            timeRepository.save(time6);
            System.out.println("시간 데이터가 생성되었습니다.");
        }
    }
}
