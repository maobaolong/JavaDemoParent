package net.mbl.demo.stateless4j;

import com.github.oxo42.stateless4j.StateMachineConfig;

/**
 * state和trigger注册
 */
public class StateConver {

  static StateMachineConfig<CurrentState, Trigger> config = new StateMachineConfig<>();

  static {
    /**
     * 最初为small状态时
     */
    config.configure(CurrentState.SMALL)
        /**
         * 遇到蘑菇触发-->big状态
         */
        .permit(Trigger.MUSHROOM, CurrentState.BIG)
        /**
         * 花朵触发,-->直接变为可攻击状态
         */
        .ignore(Trigger.FLOWER)
        /**
         * 妖怪触发,死亡状态
         */
        .permit(Trigger.MONSTER, CurrentState.DEAD);

    /**
     * 最初为big状态
     */
    config.configure(CurrentState.BIG)
        /**
         * 蘑菇触发,状态不变,
         * permitReentry方法state变化相同,但具体执行过程有一些区别
         */
        .ignore(Trigger.MUSHROOM)
        /**
         * 花朵触发,-->直接变为可攻击状态
         */
        .permit(Trigger.FLOWER, CurrentState.ATTACH)
        .permit(Trigger.MONSTER, CurrentState.SMALL);

    config.configure(CurrentState.ATTACH)
        .ignore(Trigger.MUSHROOM)
        .ignore(Trigger.FLOWER)
        .permit(Trigger.MONSTER, CurrentState.SMALL);

    config.configure(CurrentState.DEAD)
        .ignore(Trigger.MUSHROOM)
        .ignore(Trigger.FLOWER)
        .ignore(Trigger.MONSTER);

  }
}