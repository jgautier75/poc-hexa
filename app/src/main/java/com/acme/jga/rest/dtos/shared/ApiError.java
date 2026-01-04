package com.acme.jga.rest.dtos.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class ApiError {
    private String scope;
    private String code;
    private String message;
    private int statusCode;
    private List<ApiErrorDetail> details;

    public void addErrorDetail(ApiErrorDetail detail) {
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(detail);
    }
}
