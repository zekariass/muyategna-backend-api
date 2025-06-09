package com.muyategna.backend.common.service;

import com.muyategna.backend.common.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PublicPaginationService {

    @NotNull
    public Pageable getPagination(int page, int size, String sortBy, Class<?> clazz) {
        int safePage = Math.max(0, page - 1);
        int safeSize = Math.max(1, size);
        if (CommonUtil.isSortFieldInvalid(clazz, sortBy)) {
            sortBy = "id";
        }

        return PageRequest.of(safePage, safeSize, Sort.Direction.ASC, sortBy);
    }

    public String getSafeSortBy(String sortBy, Class<?> clazz) {
        if (CommonUtil.isSortFieldInvalid(clazz, sortBy)) {
            sortBy = "id";
        }
        return sortBy;
    }

    public int getSafePage(int page) {
        return Math.max(0, page - 1);
    }

    public int getSafeSize(int size) {
        return Math.max(1, size);
    }
}
