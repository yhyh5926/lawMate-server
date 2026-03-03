// src/main/java/com/edu/springboot/domain/member/vo/MemberVO.java
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
    private String memberType; // PERSONAL, LAWYER
    private String name;
    private String phone;
    private String email;
    private String provider; // 💡 추가: LOCAL, GOOGLE

    @Builder.Default
    private String phoneVerified = "N";

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, PENDING(승인대기), WITHDRAWN

    @Builder.Default
    private String saveIdYn = "N";

    private Date createdAt;
    private Date updatedAt;
}