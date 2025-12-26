package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberService;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberService memberService;

    public ReservationService(ReservationDao reservationDao, MemberService memberService) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest,Member member) {

        String reservationName;
        if(reservationRequest.getName()==null){
            reservationName = member.getName();
        }
        else{
            reservationName = reservationRequest.getName();
        }

        Reservation reservation = reservationDao.save(reservationRequest, reservationName);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
