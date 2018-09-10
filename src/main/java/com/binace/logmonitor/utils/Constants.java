package com.binace.logmonitor.utils;

import com.binace.logmonitor.collectors.CounterWithSet;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class Constants {
    public static final CounterWithSet REGISTER_USER_COUNTER = CounterWithSet.build()
            .name("register_user_number")
            .labelNames("users")
            .help("total registered user numbers")
            .register();

    public static final Gauge USER_LOGIN_NUMBER = Gauge.build()
            .name("user_login_number")
            .help("the login user number")
            .register();

}
