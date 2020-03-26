package edu.java.spring.threadpools.service;

import edu.java.spring.threadpools.model.FeasibilityResult;
import edu.java.spring.threadpools.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeasibilityService {

    private final ConcurrentHashMap<String, List<Task>> suits = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, FeasibilityResult> evaluations = new ConcurrentHashMap<>();


    public void publishTaskSuit(String name, List<Task> suit) {
        suits.putIfAbsent(name, suit);
        FeasibilityResult feasibility = feasibilityTest(suit);
        evaluations.putIfAbsent(name, feasibility);
    }

    public FeasibilityResult getSuitFeasibility(String name) {
        return evaluations.get(name);
    }

    private static FeasibilityResult feasibilityTest(List<Task> suit) {
        FeasibilityResult result = new FeasibilityResult();
        result.setFeasible(true);
        List<Task> suitForCalculation = new ArrayList<>(suit);
        suitForCalculation.sort(Comparator.comparingInt(Task::getPriority));
        for (Task task : suitForCalculation) {
            int latency = task.getComputation();
            List<Task> higherPriorityTasks = suitForCalculation.stream()
                    .filter(tsk -> tsk.getPriority() <= task.getPriority())
                    .collect(Collectors.toList());
            while (true) {
                int newLatency = task.getComputation();
                for (Task hptask : higherPriorityTasks) {
                    int amount = hptask.getAmount();
                    if (Objects.equals(hptask.getName(), task.getName())) {
                        amount--;
                    }
                    for (; amount > 0; amount--) {
                        newLatency += Math.ceil((double) latency / (double) hptask.getPeriod())
                                * hptask.getComputation();
                        if (latency > Integer.MAX_VALUE / 2) {
                            break;
                        }
                    }
                }
                if (latency > Integer.MAX_VALUE / 2) {
                    break;
                }
                if (newLatency == latency) {
                    break;
                } else if (newLatency < latency) {
                    throw new IncorrectAlgorithmException();
                } else {
                    latency = newLatency;
                    task.setLatency(newLatency);
                }
            }
        }
        for (Task task : suitForCalculation) {
            if (task.getLatency() != null
                    && task.getDeadline() > task.getLatency()) {
                result.setMaxFeasibleLatency(task.getLatency());
                result.setLastFeasibleTaskName(task.getName());
            } else {
                result.setFeasible(false);
                break;
            }
        }
        return result;

    }

    public static class IncorrectAlgorithmException extends RuntimeException {
    }

}
