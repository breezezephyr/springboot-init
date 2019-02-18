package com.sean.init.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sean.init.model.IEnum;

/**
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 2018/10/10 7:34 PM
 */
public enum CodeStatus implements IEnum<Byte> {

    INIT((byte) 0, "初始化"),
    RESERVED((byte) 1, "预留"),
    AVAILABLE((byte) 2, "可用"),
    LOCKING((byte) 3, "锁定"), //中间状态，check vom&sale数据相同后 --> distributed
    DISTRIBUTED((byte) 4, "已分配"),
    UNBOUND((byte) 5, "解绑"); //中间状态，check vom&sale数据相同后 --> reserved

    private Byte value;
    private String desc;

    CodeStatus(Byte value, String desc) {
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

    public boolean isInit() {
        return this.equals(CodeStatus.INIT);
    }

    public boolean isReserved() {
        return this.equals(CodeStatus.RESERVED);
    }

    public boolean isAvailable() {
        return this.equals(CodeStatus.AVAILABLE);
    }

    public boolean isDistributed() {
        return this.equals(CodeStatus.DISTRIBUTED);
    }

    public boolean isLocking() {
        return this.equals(CodeStatus.LOCKING);
    }

    public boolean isUnbound() {
        return this.equals(CodeStatus.UNBOUND);
    }
}
