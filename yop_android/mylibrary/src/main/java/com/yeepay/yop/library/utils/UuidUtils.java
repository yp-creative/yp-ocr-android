package com.yeepay.yop.library.utils;

import java.util.UUID;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/10/17 上午9:41
 */
public class UuidUtils {
    public static String getUUID(){
        String uuid=UUID.randomUUID().toString();
        StringBuilder stringBuilder=new StringBuilder();
        return stringBuilder.append(uuid.substring(0,8)).append(uuid.substring(9,13)).append(uuid.substring(14,18))
                .append(uuid.substring(19,23)).append(uuid.substring(24)).toString();
    }
}
