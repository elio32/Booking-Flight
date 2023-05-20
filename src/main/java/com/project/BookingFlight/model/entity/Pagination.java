package com.project.BookingFlight.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private Integer pageSize = 5;
    private Integer pageNumber = 0;
    private String sortByProperty = "id";
    private Boolean sortByAscendingOrder = true;
}
