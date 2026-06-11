package org.ceiba.hu03.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PaginatedResult<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int size;
}
