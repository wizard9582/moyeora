package com.ssafy.api.response;

import com.ssafy.db.entity.Invite;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("InviteResponse")
public class InviteRes {
    @ApiModelProperty(name="Invite PK id")
    Long id;

    @ApiModelProperty(name="Invite fromUser")
    String fromUser;

    @ApiModelProperty(name="Invite toUser")
    String toUser;

    @ApiModelProperty(name="Invite url")
    String url;

    @ApiModelProperty(name="Invite month")
    int month;

    @ApiModelProperty(name="Invite day")
    int day;

    @ApiModelProperty(name="Invite hour")
    int hour;

    @ApiModelProperty(name="Invite min")
    int min;


    public static String toConvertJson(Invite in) {

        String res =
                "{ id :"+in.getId()+","
                + "fromUser :"+in.getFromUser()+","
                +"toUser :"+in.getToUser()+","
                +"url : "+in.getUrl()+","
                +"month :"+in.getMonth()+","
                +"day : "+in.getDay()+","
                +"hour :"+in.getHour()+","
                +"min : "+in.getMin()+"}";
        return res;
    }
}
