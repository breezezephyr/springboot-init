package com.sean.init.state;

import com.sean.init.enums.CodeStatus;
import com.sean.init.enums.OptEvent;
import org.apache.poi.ss.formula.functions.Code;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;
import org.squirrelframework.foundation.fsm.annotation.State;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.annotation.States;
import org.squirrelframework.foundation.fsm.annotation.Transit;
import org.squirrelframework.foundation.fsm.annotation.Transitions;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

public class QuickStartSample {


    @Transitions({
            @Transit(from = "INIT", to = "RESERVED", on = "RESERVE", callMethod = "updateCodexStatus"),
            @Transit(from = "INIT", to = "AVAILABLE", on = "ENABLE", callMethod = "updateCodexStatus"),
            @Transit(from = "RESERVED", to = "AVAILABLE", on = "ENABLE", callMethod = "updateCodexStatus"),
            @Transit(from = "AVAILABLE", to = "DISTRIBUTED", on = "ASSIGN", callMethod = "assignCodex"),
            @Transit(from = "DISTRIBUTED", to = "RESERVED", on = "UNBIND", callMethod = "unbindCodex"),
            @Transit(from = "RESERVED", to = "AVAILABLE", on = "RESTART", callMethod = "updateCodexStatus"),
            @Transit(from = "DISTRIBUTED", to = "DISTRIBUTED", on = "TRANSFER", callMethod = "transferCodex")
    })

    @States(
            {@State(name = "DISTRIBUTED", entryCallMethod = "noticeUser")}
    )

    // 2. Define State Machine Class
    @StateMachineParameters(stateType = CodeStatus.class, eventType = OptEvent.class, contextType = Integer.class)
    static class StateMachineSample extends AbstractUntypedStateMachine {
        protected void enableCodex(CodeStatus from, CodeStatus to, OptEvent event, Integer context) {
            System.out.println("Transition from '" + from + "' to '" + to + "' on event '" + event +
                    "' with context '" + context + "'.");
        }

        protected void assignCodex(CodeStatus from, CodeStatus to, OptEvent event, Integer context) {
            System.out.println("assign codex State \'" + to + "\'.");
        }


        protected void noticeUser(CodeStatus from, CodeStatus to, OptEvent event, Integer context) {
            System.out.println("notice User State \'" + to + "\'.");
        }

        @Override
        protected void afterActionInvoked(Object fromState, Object toState, Object event, Object context) {
            OptEvent event1 = (OptEvent) event;
            System.out.println("afterActionInvoked"+event1.getDesc());
        }


        @Override
        protected void afterTransitionCompleted(Object fromState, Object toState, Object event, Object context) {
            System.out.println("记录日志");
        }

        @Override
        protected void afterTransitionEnd(Object fromState, Object toState, Object event, Object context) {
            System.out.println("关闭");
            this.terminate(context);
        }
    }



    public static void main(String[] args) {
        // 3. Build State Transitions
        UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(StateMachineSample.class);
        builder.externalTransition().from(CodeStatus.RESERVED).to(CodeStatus.AVAILABLE).on(OptEvent.ENABLE).callMethod("enableCodex");
        builder.onEntry(CodeStatus.LOCKING).callMethod("assignCode");

        // 4. Use State Machine
        UntypedStateMachine fsm = builder.newStateMachine(CodeStatus.RESERVED);
        fsm.fire(OptEvent.ENABLE, 10);
        fsm.dumpSavedData();

        System.out.println("Current state is " + fsm.getCurrentState());
    }
}