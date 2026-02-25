package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;

public interface MemberService {
    void join(JoinDto joinDto) throws Exception;
    void joinLawyer(JoinDto joinDto) throws Exception;
    MemberVO login(LoginDto loginDto) throws Exception;
    void modifyInfo(MemberVO memberVO) throws Exception;
    void withdraw(Long memberId) throws Exception;
}