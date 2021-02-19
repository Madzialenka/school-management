package com.madzialenka.schoolmanagement.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PageResponseDTO<T> {
    private List<T> elements;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer numberOfPages;
    private Long numberOfElements;
}
