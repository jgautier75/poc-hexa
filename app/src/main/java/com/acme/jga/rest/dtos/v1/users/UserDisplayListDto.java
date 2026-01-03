package com.acme.jga.rest.dtos.v1.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class UserDisplayListDto {
    private List<UserDisplayDto> users;
    Integer totalCount;
    Integer pageIndex;
    Integer totalNbPages;
}
