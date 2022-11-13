package ru.kolesnikov.simplechat.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class UriComponentsBuilderUtil {

    private static final String HTTP_LOCALHOST = "http://localhost";

    public static UriComponentsBuilder builder() {
        return UriComponentsBuilder
                .fromHttpUrl(HTTP_LOCALHOST);
    }
}
