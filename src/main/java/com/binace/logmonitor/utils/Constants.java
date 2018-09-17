package com.binace.logmonitor.utils;

import com.binace.logmonitor.collectors.CounterWithSet;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class Constants {
    public static final String ACCOUNT_USER_COUNTER_LABEL_REGISTER = "register";
    public static final String ACCOUNT_USER_COUNTER_LABEL_LOGIN = "login";
    public static final CounterWithSet ACCOUNT_USER_COUNTER = CounterWithSet.build()
            .name("account_user")
            .help("account user numbers")
            .register();

    public static final Gauge ACCOUNT_ONLINE_USER_GAUGE = Gauge.build()
            .name("account_user_online")
            .help("the online user number.")
            .register();

}
