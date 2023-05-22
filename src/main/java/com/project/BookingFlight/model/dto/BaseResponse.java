package com.project.BookingFlight.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class BaseResponse {
    private List<String> messages;
}
