package com.muyategna.backend.common.service;

import com.muyategna.backend.common.CommonUtil;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.repository.SystemConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminPaginationService {

    private final SystemConfigRepository systemConfigRepository;

    @Autowired
    public AdminPaginationService(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    /**
     * Return a Pageable that represents the pagination of the given parameters.
     *
     * @param page   The page number (1-indexed). If null, 1 is used.
     * @param size   The page size. If null, the default page size is used.
     * @param sortBy The field to sort by. If it is invalid, 'id' is used.
     * @param clazz  The class of the entity that the pagination is for. Used to validate the sort field.
     * @return A Pageable that represents the pagination.
     */
    @NotNull
    public Pageable getPagination(Integer page, Integer size, String sortBy, Class<?> clazz) {

        size = getPageSize(size);

        int safePage = Math.max(0, page - 1);
        int safeSize = Math.max(1, size);
        if (CommonUtil.isSortFieldInvalid(clazz, sortBy)) {
            sortBy = "id";
        }

        return PageRequest.of(safePage, safeSize, Sort.Direction.ASC, sortBy);
    }

    /**
     * Returns a safe sort by field.
     * <p>
     * If the given sort by field is invalid, the method returns 'id'.
     *
     * @param sortBy The sort by field.
     * @param clazz  The class of the entity that the sort by field is for.
     * @return A safe sort by field.
     */
    public String getSafeSortBy(String sortBy, Class<?> clazz) {
        if (CommonUtil.isSortFieldInvalid(clazz, sortBy)) {
            sortBy = "id";
        }
        return sortBy;
    }

    /**
     * Returns a safe page number.
     * <p>
     * If the given page number is 0 or negative, the method returns 0.
     *
     * @param page The page number to be made safe.
     * @return A safe page number.
     */
    public int getSafePage(int page) {
        return Math.max(0, page - 1);
    }

    /**
     * Returns a safe page size.
     * <p>
     * If the given page size is null, the method returns the default page size.
     * If the given page size is less than 1, the method returns 1.
     *
     * @param size The page size to be made safe.
     * @return A safe page size.
     */
    public int getSafeSize(Integer size) {
        size = getPageSize(size);
        return Math.max(1, size);
    }

    /**
     * Returns the page size based on the given page size and the default page size configuration.
     * <p>
     * If the given page size is null, the method returns the default page size configuration. If the default page size configuration is null or not a valid integer,
     * the method returns 10.
     *
     * @param size The page size to be made safe.
     * @return The page size.
     */
    @NotNull
    private Integer getPageSize(Integer size) {
        try {
            if (size == null) {
                size = systemConfigRepository
                        .findByName(Constants.DEFAULT_PAGE_SIZE_NAME)
                        .map(SystemConfig::getValue)
                        .map(Integer::parseInt)
                        .orElse(10);
            }
        } catch (NumberFormatException e) {
            log.warn("Number format exception in {}: {}", this.getClass().getSimpleName(), e.getMessage());
            log.info("Default page size of 10 is used");
            size = 10;
        }

        return size;
    }
}
