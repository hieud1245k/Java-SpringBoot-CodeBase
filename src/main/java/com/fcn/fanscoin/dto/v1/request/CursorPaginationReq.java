package com.fcn.fanscoin.dto.v1.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class CursorPaginationReq {

    @Builder.Default
    private Long cursor = 0L;

    @Builder.Default
    private boolean prev = true;
}
