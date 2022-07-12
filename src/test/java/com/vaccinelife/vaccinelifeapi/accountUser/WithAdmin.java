package com.vaccinelife.vaccinelifeapi.accountUser;


import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithMockUser(username = "adminTest", roles = "ADMIN")
@Retention(RetentionPolicy.RUNTIME)
public @interface WithAdmin {
}
