package com.thomas.club.security.service;

import com.thomas.club.entity.ClubMember;
import com.thomas.club.entity.ClubMemberRole;
import com.thomas.club.repository.ClubMemberRepository;
import com.thomas.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    // Google API - OAuth2 를 통해, 소셜 로그인한 USER 정보를 추출할 수 있음     ->   DB 에 저장
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("===========================");
        log.info("userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("clientName: " + clientName);      // Google 출력
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        oAuth2User.getAttributes().forEach((k,v) -> {
            // sub, picture, email, email_verified
            log.info(k + " : " + v);
        });

        log.info("---------------------------");

        String email = null;

        if(clientName.equals("Google")) { email = oAuth2User.getAttribute("email"); }
        log.info("EMAIL: " + email);

        ClubMember clubMember = saveSocialMember(email);
        System.out.println(clubMember.toString());

        log.info("===========================");

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(clubMember.getEmail(),
                                                                    clubMember.getPassword(),
                                                                    true,
                                                                    clubMember.getRoleSet().stream().map(role ->
                                                                         new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()),
                                                                    oAuth2User.getAttributes());

        clubAuthMemberDTO.setName(clubMember.getName());

        return clubAuthMemberDTO;
    }

    private ClubMember saveSocialMember(String email) {

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        if(result.isPresent()) {
            return result.get();
        }

        // 없다면 회원 추가 패스워드는 1111, 이름은 이메일 주소로 저장
        ClubMember clubMember = ClubMember.builder().email(email)
                                                    .name(email)
                                                    .password(passwordEncoder.encode("1111"))
                                                    .fromSocial(true)
                                                    .build();

        clubMember.addMemberRole(ClubMemberRole.USER);
        clubMemberRepository.save(clubMember);

        return clubMember;
    }
}