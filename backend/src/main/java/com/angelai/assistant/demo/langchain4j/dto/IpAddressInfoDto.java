package com.angelai.assistant.demo.langchain4j.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAddressInfoDto {
    private String ip;
    private String pro;
    private String proCode;
    private String city;
    private String cityCode;
    private String region;
    private String regionCode;
    private String addr;
    private String regionNames;
    private String err;
}