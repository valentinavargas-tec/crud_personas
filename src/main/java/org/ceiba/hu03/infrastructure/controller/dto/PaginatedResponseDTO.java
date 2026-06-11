package org.ceiba.hu03.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponseDTO<T> {

    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
}
