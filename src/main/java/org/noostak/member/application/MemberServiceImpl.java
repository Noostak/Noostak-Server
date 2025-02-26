package org.noostak.member.application;

import org.noostak.infra.KeyAndUrl;
import org.noostak.infra.S3DirectoryPath;
import org.noostak.infra.S3Service;
import org.noostak.member.domain.Member;
import org.noostak.member.domain.MemberRepository;
import org.noostak.member.domain.vo.MemberName;
import org.noostak.member.domain.vo.MemberProfileImageKey;
import org.noostak.auth.dto.SignUpRequest;
import org.noostak.member.dto.GetProfileResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final S3Service s3Service;

    public MemberServiceImpl(MemberRepository memberRepository,
                             @Qualifier("dev") S3Service s3Service) {
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
    }

    @Override
    @Transactional
    public Member createMember(SignUpRequest request) {
        KeyAndUrl keyAndUrl = saveProfileImage(request.getMemberProfileImage());
        Member newMember = createMember(request, keyAndUrl);

        return memberRepository.save(newMember);
    }


    private Member createMember(SignUpRequest request, KeyAndUrl keyAndUrl) {
        return Member.of(
                MemberName.from(request.getMemberName()),
                MemberProfileImageKey.from(keyAndUrl.getKey())
        );
    }

    private KeyAndUrl saveProfileImage(MultipartFile file) {
        return s3Service.uploadImage(S3DirectoryPath.MEMBER, file);
    }

    @Override
    public GetProfileResponse fetchMember(Long memberId) {
        Member member = memberRepository.getById(memberId);

        String memberName = member.getName().value();
        String imageUrl = s3Service.getImageUrl(member.getKey().value());

        return GetProfileResponse.of(memberName,imageUrl);
    }

    @Override
    public void updateMember() {

    }

    @Override
    public void deleteMember() {

    }
}
