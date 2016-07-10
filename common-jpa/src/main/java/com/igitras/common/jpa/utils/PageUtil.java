package com.igitras.common.jpa.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Created by mason on 7/10/16.
 */
public abstract class PageUtil {
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Convert the page model to page dto.
     *
     * @param original  model page
     * @param converter converter.
     * @param <T>       model
     * @param <R>       dto
     *
     * @return page of dto
     */
    public static <T, R> Page<R> convert(Page<T> original, Converter<T, R> converter) {
        return original.map(converter);
    }

    /**
     * Generate the first page.
     *
     * @return first page
     */
    public static Pageable firstPage() {
        return new PageRequest(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }
}
