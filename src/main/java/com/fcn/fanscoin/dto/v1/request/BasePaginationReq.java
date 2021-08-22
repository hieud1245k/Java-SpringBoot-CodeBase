package com.fcn.fanscoin.dto.v1.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BasePaginationReq {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer pageSize = 10;

    private String sortType;

    private String sortColumn;
}
