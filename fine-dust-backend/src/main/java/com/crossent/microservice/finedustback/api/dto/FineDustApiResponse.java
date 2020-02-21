package com.crossent.microservice.finedustback.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FineDustApiResponse{
    private FineDustBody body = new FineDustBody();
    private List<FineDustApiItems> items = new ArrayList<>();
}
