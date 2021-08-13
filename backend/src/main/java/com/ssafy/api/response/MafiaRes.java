package com.ssafy.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("MafiaResponse")
public class MafiaRes {
    @ApiModelProperty(name="user PK id")
    Long userPid;
    @ApiModelProperty(name="user id")
    String userId;
    @ApiModelProperty(name="user's game role")
    String role;

    public static MafiaRes of(Long userPid, String userId, String role){
        MafiaRes mafiaRes = new MafiaRes();
        mafiaRes.setUserPid(userPid);
        mafiaRes.setUserId(userId);
        mafiaRes.setRole(role);
        return mafiaRes;
    }
}
