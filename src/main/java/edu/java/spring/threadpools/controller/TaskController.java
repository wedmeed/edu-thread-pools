package edu.java.spring.threadpools.controller;

import edu.java.spring.threadpools.model.FeasibilityResult;
import edu.java.spring.threadpools.model.Task;
import edu.java.spring.threadpools.service.FeasibilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TaskController {

    private final FeasibilityService service;

    public TaskController(FeasibilityService service) {
        this.service = service;
    }

    @PostMapping("/suit/{suit_name}")
    @ResponseStatus(HttpStatus.CREATED)
    public void publishSalary(@PathVariable("suit_name") String suitName, @RequestBody List<Task> taskSuit) {
        service.publishTaskSuit(suitName, taskSuit);
    }

    @GetMapping("/feasibility/{suit_name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FeasibilityResult publishSalary(@PathVariable("suit_name") String suitName) {
        FeasibilityResult res = service.getSuitFeasibility(suitName);
        if (res == null){
            throw new ResultsArentReadyException();
        }
        log.info("Return feasibility {}", res);
        return res;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public class ResultsArentReadyException extends RuntimeException{}


}
