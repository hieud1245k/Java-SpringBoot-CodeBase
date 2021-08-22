package com.fcn.fanscoin.helper;

import com.fcn.fanscoin.dto.v1.request.BasePaginationReq;
import com.fcn.fanscoin.dto.v1.response.BasePaginationRes;
import com.fcn.fanscoin.dto.v1.response.CursorPaginationRes;
import com.fcn.fanscoin.exception.ValidatorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

public final class PaginationHelper {

    private PaginationHelper() {
    }

    public static BasePaginationRes buildBasePaginationRes(final Page<?> page) {
        return BasePaginationRes.builder()
                                .items(page.toList())
                                .totalItems(page.getTotalElements())
                                .totalPage(page.getTotalPages())
                                .pageSize(page.getSize())
                                .page(page.getNumber() + 1)
                                .build();
    }

    public static CursorPaginationRes buildCursorPaginationRes(final List<?> items,
                                                               final boolean hasNext) {
        return CursorPaginationRes.builder()
                                  .items(items)
                                  .hasNext(hasNext)
                                  .build();
    }

    public static PageRequest generatePageRequest(final BasePaginationReq req) {
        return generatePageRequest(req.getPage(), req.getPageSize(), req.getSortType(), req.getSortColumn());
    }

    public static PageRequest generatePageRequestWithoutSort(final BasePaginationReq req) {
        return PageRequest.of(req.getPage() - 1, req.getPageSize());
    }

    public static PageRequest generatePageRequest(final Integer page,
                                                  final Integer pageSize,
                                                  final String sortType,
                                                  final String sortColumn) {
        return PageRequest.of(page - 1, pageSize, generateSort(sortType, sortColumn));
    }

    private static Sort generateSort(final String sortType, final String sortColumn) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortColumn == null) {
            orders.add(Sort.Order.asc("id"));
            return Sort.by(orders);
        }
        if (sortType.equals(ASC.name())) {
            orders.add(Sort.Order.asc(sortColumn));
        } else if (sortType.equals(DESC.name())) {
            orders.add(Sort.Order.desc(sortColumn));
        } else {
            throw new ValidatorException("Invalid sort request", "sorts");
        }
        return Sort.by(orders);
    }
}
