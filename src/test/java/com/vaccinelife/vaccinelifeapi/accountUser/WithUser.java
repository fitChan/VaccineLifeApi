package com.vaccinelife.vaccinelifeapi.accountUser;


import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithMockUser(username = "cksdntjd", password = "cksdn123",roles = "USER")
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUser {
}
