package com.platform.general.microservice.web.credential.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingContext {

    private final int pageSize;

    private final int pageNumber;
}
