package com.plat.platdata.auth.jwt;


public record TokenId(String provider,
                      String providerId
) { }