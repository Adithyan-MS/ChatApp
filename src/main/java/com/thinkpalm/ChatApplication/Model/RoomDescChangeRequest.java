package com.thinkpalm.ChatApplication.Model;

import com.thinkpalm.ChatApplication.Validation.BioDescValid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDescChangeRequest {

    @BioDescValid
    private String description;

}
