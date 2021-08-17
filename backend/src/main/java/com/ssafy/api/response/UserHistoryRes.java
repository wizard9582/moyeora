package com.ssafy.api.response;

import com.ssafy.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("UserHisotryResponse")
public class UserHistoryRes{
    @ApiModelProperty(name="Win Rate")
    float tWinRate;
    @ApiModelProperty(name="Win")
    int tWin;
    @ApiModelProperty(name="Lose")
    int tLose;

    @ApiModelProperty(name="doctor Win")
    int dWin;
    @ApiModelProperty(name="doctor lose")
    int dLose;
    @ApiModelProperty(name="doctor Total")
    int dTotal;
    @ApiModelProperty(name="doctor Total")
    float dRate;


    @ApiModelProperty(name="Police Win")
    int pWin;
    @ApiModelProperty(name="Police lose")
    int pLose;
    @ApiModelProperty(name="Police Total")
    int pTotal;
    @ApiModelProperty(name="doctor Total")
    float pRate;


    @ApiModelProperty(name="Mafia Win")
    int mWin;
    @ApiModelProperty(name="Mafia lose")
    int mLose;
    @ApiModelProperty(name="Mafia Total")
    int mTotal;
    @ApiModelProperty(name="doctor Total")
    float mRate;

    @ApiModelProperty(name="Citizen Win")
    int cWin;
    @ApiModelProperty(name="Citizen lose")
    int cLose;
    @ApiModelProperty(name="Citizen Total")
    int cTotal;
    @ApiModelProperty(name="doctor Total")
    float cRate;



    public static UserHistoryRes of(float tWinRate, int tWin, int tLose, int dTotal ,int dWin, int dLose, int pTotal ,int pWin, int pLose, int mTotal ,int mWin, int mLose, int cTotal ,int cWin, int cLose, float dRate, float mRate, float pRate, float cRate) {
        UserHistoryRes historyRes = new UserHistoryRes();
        // 전체
        historyRes.setTWinRate(tWinRate);
        historyRes.setTWin(tWin);
        historyRes.setTLose(tLose);
        // 마피아
        historyRes.setMWin(mWin);
        historyRes.setMWin(mLose);
        historyRes.setMTotal(mTotal);
        // 의사
        historyRes.setDWin(dWin);
        historyRes.setDLose(dLose);
        historyRes.setDTotal(dTotal);
        // 경찰
        historyRes.setPWin(pWin);
        historyRes.setPLose(pLose);
        historyRes.setPTotal(pTotal);
        // 시민
        historyRes.setCWin(cWin);
        historyRes.setCLose(cLose);
        historyRes.setCTotal(cTotal);

        return historyRes;
    }
}