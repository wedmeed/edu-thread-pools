package edu.java.spring.threadpools.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class FeasibilityResult {
    private Boolean feasible;
    private Integer maxFeasibleLatency;
    private String lastFeasibleTaskName;
}
