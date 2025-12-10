package com.icee.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiduResult {
    private Integer status;
    private Address result;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private Location location;
        private Integer precise;
        private Integer confidence;
        private Integer comprehension;
        private String level;

        @JsonProperty("analys_level")  // 添加JSON映射注解
        private String analysLevel;    // 修正字段命名
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private Float lng;
        private Float lat;
    }
}