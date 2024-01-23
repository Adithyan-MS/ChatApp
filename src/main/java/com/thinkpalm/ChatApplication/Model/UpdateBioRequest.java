package com.thinkpalm.ChatApplication.Model;

import com.thinkpalm.ChatApplication.Validation.BioDescValid;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateBioRequest {

    @BioDescValid
    private String bio;

}
