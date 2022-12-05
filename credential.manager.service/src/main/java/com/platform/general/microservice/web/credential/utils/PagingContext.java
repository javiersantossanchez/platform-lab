package com.platform.general.microservice.web.credential.utils;

import lombok.Data;

@Data
public class PagingContext {

    private final int pageSize;

    private final int pageNumber;
}
