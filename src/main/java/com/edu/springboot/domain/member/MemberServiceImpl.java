package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public void join(JoinDto dto) throws Exception {
        if (memberMapper.checkLoginId(dto.getLoginId()) > 0) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        MemberVO member = new MemberVO();
        member.setLoginId(dto.getLoginId());
        member.setPassword(dto.getPassword()); 
        member.setMemberType("PERSONAL");
        member.setName(dto.getName());
        member.setPhone(dto.getPhone());
        member.setEmail(dto.getEmail());
        
        memberMapper.insertMember(member);
    }

    @Override
    @Transactional
    public void joinLawyer(JoinDto dto) throws Exception {
        if (memberMapper.checkLoginId(dto.getLoginId()) > 0) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        MemberVO member = new MemberVO();
        member.setLoginId(dto.getLoginId());
        member.setPassword(dto.getPassword());
        member.setMemberType("LAWYER");
        member.setName(dto.getName());
        member.setPhone(dto.getPhone());
        member.setEmail(dto.getEmail());
        
        memberMapper.insertMember(member);
    }

    @Override
    public MemberVO login(LoginDto dto) throws Exception {
        MemberVO member = memberMapper.findByLoginId(dto.getLoginId());
        if(member != null && member.getPassword().equals(dto.getPassword())) {
            return member;
        }
        throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    @Override
    public void modifyInfo(MemberVO memberVO) throws Exception {
        memberMapper.updateMember(memberVO);
    }

    @Override
    public void withdraw(Long memberId) throws Exception {
        memberMapper.withdrawMember(memberId);
    }
}