package com.plat.platboot.dto;

public record Response<T>(String message, T data) {

    public static <T> Response<T> error(T data) {
        return new Response<>("error", data);
    }

}
