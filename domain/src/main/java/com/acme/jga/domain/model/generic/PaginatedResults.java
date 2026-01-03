package com.acme.jga.domain.model.generic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public record PaginatedResults<T>(Integer nbResults, Integer nbPages, List<T> results, Integer pageSize,
                                  Integer pageIndex) {
}
