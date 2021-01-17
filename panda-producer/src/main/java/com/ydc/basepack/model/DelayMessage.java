package com.ydc.basepack.model;


import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class DelayMessage extends Message {

    private long delay;

    private TimeUnit unit;

}
