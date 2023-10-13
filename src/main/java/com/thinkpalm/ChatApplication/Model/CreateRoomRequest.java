package com.thinkpalm.ChatApplication.Model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateRoomRequest {
    private String name;
    private String desc;
    private String pic;
    private List<Integer> participants;

}
