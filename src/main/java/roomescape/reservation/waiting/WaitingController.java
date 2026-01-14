package roomescape.reservation.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.infrastructure.AuthMember;
import roomescape.member.Member;
import roomescape.reservation.dto.ReservationRequest;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@AuthMember Member member,
                                                  @RequestBody ReservationRequest request) {
        WaitingResponse response = waitingService.save(request, member);
        return ResponseEntity.created(URI.create("/waitings/" + response.id()))
                .body(response);
    }
}
