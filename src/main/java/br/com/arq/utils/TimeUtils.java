package br.com.arq.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {

    private static final ZoneId ZONE =
            ZoneId.of("America/Sao_Paulo");

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE);
    }

}
