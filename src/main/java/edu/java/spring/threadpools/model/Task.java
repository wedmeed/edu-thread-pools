package edu.java.spring.threadpools.model;

import lombok.Data;

@Data
public class Task {
    private String name;
    private Integer computation;
    private Integer deadline;
    private Integer period;
    private Integer priority;
    private Integer latency;
    private Integer amount;
}
