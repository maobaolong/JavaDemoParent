package net.mbl.demo;

import static org.junit.Assert.assertEquals;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import org.junit.Test;

public class AppTest {

    @Test
    public void testPhoneRings() throws InterruptedException {
        StateMachineConfig<State, Trigger> phoneCallConfig = new StateMachineConfig<>();

        phoneCallConfig.configure(State.OffHook)
                .permit(Trigger.CallDialed, State.Ringing)
                .onEntry(this::enterHangup)
                .onExit(this::exitHangup);

        phoneCallConfig.configure(State.Ringing)
                .permit(Trigger.HungUp, State.OffHook)
                .permit(Trigger.CallConnected, State.Connected)
                .onEntry(this::startRing)
                .onExit(this::stopRing);

        phoneCallConfig.configure(State.Connected)
                .onEntry(this::startCallTimer)
                .onExit(this::stopCallTimer)
                .permit(Trigger.LeftMessage, State.OffHook)
                .permit(Trigger.HungUp, State.OffHook)
                .permit(Trigger.PlacedOnHold, State.OnHold);

        // ...

        StateMachine<State, Trigger> phoneCall = new StateMachine<>(State.OffHook, phoneCallConfig);

        phoneCall.fire(Trigger.CallDialed);
        assertEquals(State.Ringing, phoneCall.getState());
        Thread.sleep(2000);
        phoneCall.fire(Trigger.CallConnected);
        Thread.sleep(2000);
        phoneCall.fire(Trigger.HungUp);

    }

    private void stopCallTimer() {
        System.out.println("stop call");
    }

    private void startCallTimer() {
        System.out.println("start call");
    }


    private void stopRing() {
        System.out.println("stop ring");
    }

    private void startRing() {
        System.out.println("start ring");
    }


    private void exitHangup() {
        System.out.println("exit hang up");
    }

    private void enterHangup() {
        System.out.println("enter hang up");
    }


    private enum State {
        Ringing, Connected, OnHold, OffHook

    }

    private enum Trigger {
        CallDialed, CallConnected, PlacedOnHold, LeftMessage, HungUp

    }
}