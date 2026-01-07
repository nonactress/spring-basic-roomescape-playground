package roomescape.reservation.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Query("SELECT w FROM Waiting w JOIN FETCH w.time JOIN FETCH w.theme WHERE w.member.id = :memberId")
    List<Waiting> findByMemberId(@Param("memberId") Long memberId);

    @Query("""
                SELECT COUNT(w) + 1 
                FROM Waiting w 
                WHERE w.date = :date 
                  AND w.theme.id = :themeId 
                  AND w.time.id = :timeId 
                  AND w.createdAt < :createdAt
            """)
    Long findRank(@Param("date") String date,
                  @Param("themeId") Long themeId,
                  @Param("timeId") Long timeId,
                  @Param("createdAt") java.time.LocalDateTime createdAt);

    boolean existsByMemberIdAndDateAndThemeIdAndTimeId(Long memberId, String date, Long themeId, Long timeId);

    @Query("""
                SELECT new roomescape.reservation.waiting.WaitingWithRank(
                  w, 
                  (SELECT COUNT(w2) FROM Waiting w2 
                   WHERE w2.theme = w.theme 
                     AND w2.date = w.date 
                     AND w2.time = w.time 
                     AND w2.id < w.id)) 
                FROM Waiting w 
                WHERE w.member.id = :memberId
            """)
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);
}
