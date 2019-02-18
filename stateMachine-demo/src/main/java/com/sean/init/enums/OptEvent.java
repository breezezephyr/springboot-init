package com.sean.init.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sean.init.model.IEnum;

/**
 *
 *  1. Define State Machine Event
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 2018/10/10 7:34 PM
 */
public enum OptEvent implements IEnum<Byte> {
    RESERVE((byte) 0, "预留"),
    ENABLE((byte) 1, "可用"),
    LOCK((byte) 2, "锁定"),
    ASSIGN((byte) 3, "分配"),
    UNBIND((byte) 4, "解绑"),
    RESTART((byte) 5, "重新启用"),
    TRANSFER((byte) 6, "换号");

    private Byte value;
    private String desc;

    OptEvent(Byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    @JsonValue
    public String getDesc() {
        return desc;
    }


    public boolean isReserve() {
        return this.equals(OptEvent.RESERVE);
    }

    public boolean isEnable() {
        return this.equals(OptEvent.ENABLE);
    }

    public boolean isLock() {
        return this.equals(OptEvent.LOCK);
    }

    public boolean isAssign() {
        return this.equals(OptEvent.ASSIGN);
    }

    public boolean isUnbind() {
        return this.equals(OptEvent.UNBIND);
    }

    public boolean isRestart() {
        return this.equals(OptEvent.RESTART);
    }

    public boolean isTransfer() {
        return this.equals(OptEvent.TRANSFER);
    }
}
