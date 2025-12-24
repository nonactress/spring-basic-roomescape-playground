package roomescape.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody MemberRequest memberRequest,
            HttpServletResponse response) { // 응답 객체를 파라미터로 받습니다.

        // 1. 서비스 호출: 서비스는 토큰 문자열(알맹이)만 반환합니다.
        // (이전 답변에서 리팩토링한대로 TokenResponse DTO를 활용하세요)
        TokenResponse tokenResponse = memberService.login(memberRequest);
        String tokenValue = tokenResponse.accessToken();


        Cookie cookie = new Cookie("token", tokenValue);


        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);


        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
