package com.carpool.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// ✨ Lombok 적용: @Data 추가
// (@Data는 @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor를 포함)
// (수동 Getter/Setter/생성자 모두 삭제)

@Data
public class LoginRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;
}
