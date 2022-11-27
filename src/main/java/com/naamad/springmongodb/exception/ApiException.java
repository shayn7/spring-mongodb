package com.naamad.springmongodb.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiException {

    private final String message;
    private final LocalDateTime localDateTime;
}
