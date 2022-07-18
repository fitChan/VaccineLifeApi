package com.vaccinelife.vaccinelifeapi.accountUser;


import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/*TODO SecurityContextHolder 정보를 가져올 수 있을지 확인할 것.*/

@WithMockUser(username = "cksdntjd", password = "cksdn123",roles = "USER")
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUser {
}
