package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int insertMember(MemberVO member);
    int checkLoginId(String loginId);
    MemberVO findByLoginId(String loginId);
    int updateMember(MemberVO member);
    int withdrawMember(Long memberId);
}