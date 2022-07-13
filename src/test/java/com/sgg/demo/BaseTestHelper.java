package com.sgg.demo;

import org.junit.Ignore;

import java.time.LocalDateTime;
import java.util.UUID;

@Ignore
public class BaseTestHelper {
    public static final UUID ID = UUID.fromString("11111111-2222-3333-4444-555555555555");
    public static final LocalDateTime CONST_DATE = LocalDateTime.of(2000, 2, 2, 0, 0, 0);

}
