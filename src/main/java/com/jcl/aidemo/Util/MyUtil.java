package com.jcl.aidemo.Util;

import io.jsonwebtoken.Claims;

public class MyUtil {
    public Claims checkToken(String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        Claims parseToken = RandomStringGenerator.parseToken(token);
        return parseToken;
    }
}
