package net.mbl.demo;

import com.github.oxo42.stateless4j.StateMachine;

/**
 * Hello world!
 */
public class App {
    private static StateMachine<CurrentState, Trigger> stateMachine =
            new StateMachine<>(CurrentState.SMALL, StateConver.config);

    public static void main(String[] args) {
        stateMachine.fire(Trigger.FLOWER);
        System.out.println("currentState-->" + stateMachine.getState());

    }
}
