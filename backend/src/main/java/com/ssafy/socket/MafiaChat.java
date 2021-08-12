package com.ssafy.socket;

import lombok.Getter;
import lombok.Setter;

// round : 라운드
// desc : morning, vote, night
@Getter
@Setter
public class MafiaChat
{
    private int round;
    private String desc;
    private int second;
    private String userId;
}
