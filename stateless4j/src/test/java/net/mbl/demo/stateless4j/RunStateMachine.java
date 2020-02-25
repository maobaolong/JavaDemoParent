package net.mbl.demo.stateless4j;

import com.github.oxo42.stateless4j.StateMachine;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 状态机测试类
 */
public class RunStateMachine {

  private static final Logger LOG = LoggerFactory.getLogger(RunStateMachine.class);
  private static StateMachine<CurrentState, Trigger> stateMachine = new StateMachine<>(
      CurrentState.SMALL, StateConver.config);

  @Test
  public void testStateMachine() {
    stateMachine.fire(Trigger.MUSHROOM);
    System.out.println("currentState-->" + stateMachine.getState());
  }
}