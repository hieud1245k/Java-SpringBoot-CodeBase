package com.fcn.fanscoin.dto.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasePaginationRes {

    private Long totalItems;
    private Integer totalPage;
    private Integer pageSize;
    private Integer page;
    private List<?> items;
}
