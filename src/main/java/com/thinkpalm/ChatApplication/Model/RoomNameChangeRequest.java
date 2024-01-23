package com.thinkpalm.ChatApplication.Model;

import com.thinkpalm.ChatApplication.Validation.RoomNameValid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomNameChangeRequest {

    @RoomNameValid
    private String name;

}
