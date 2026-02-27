/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/vo/MemberVO.java
 * 수정내용: TB_MEMBER 테이블의 Not Null 컬럼들에 대한 기본값을 @Builder.Default로 설정했습니다.
 */
package com.edu.springboot.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    private Long memberId;
    private String loginId;
    private String password;
    private String memberType;
    private String name;
    private String phone;
    private String email;

    @Builder.Default
    private String phoneVerified = "N"; // 💡 DB 필수값(NN) 대응

    @Builder.Default
    private String status = "ACTIVE";   // 💡 DB 필수값(NN) 대응

    @Builder.Default
    private String saveIdYn = "N";      // 💡 DB 필수값(NN) 대응

    private Date createdAt;
    private Date updatedAt;
}