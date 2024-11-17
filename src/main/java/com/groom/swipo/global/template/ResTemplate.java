package com.groom.swipo.global.template;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"code","message","data"})
public class ResTemplate<T> {
    int code;
    String message;
    T data;

    // 성공하였을 경우
    public ResTemplate(HttpStatus httpStatus, String message, T data) {
        this.code = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    // 실패하였을 경우
    public ResTemplate(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.message = message;
    }
}
