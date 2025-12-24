package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao,  JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public TokenResponse login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(
                memberRequest.getEmail(),
                memberRequest.getPassword()
        );

        if (member == null) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(accessToken);
    }
}
