package com.abc.xyzp.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessVO {
    private String token;
    private Integer myChoice;
}
