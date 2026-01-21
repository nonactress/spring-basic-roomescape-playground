package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.time JOIN FETCH r.theme WHERE r.member.id = :memberId")
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId AND r.time.id = :timeId")
    List<Reservation> findByDateAndThemeIdAndTimeId(@Param("date") String date,
                                                    @Param("themeId") Long themeId,
                                                    @Param("timeId") Long timeId);
}
