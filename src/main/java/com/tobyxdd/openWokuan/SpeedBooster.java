package com.tobyxdd.openWokuan;

/*
SpeedBooster
Using Wokuan's improvespeed.php / updateforfifteenmin.php API
 */

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Random;

public class SpeedBooster {

    public static final String httpAccept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            httpLang = "en-US,en;q=0.8,zh-Hans-CN;q=0.5,zh-Hans;q=0.3",
            httpUA = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; WOW64; Trident/7.0; .NET4.0E; .NET4.0C; .NET CLR 3.5.30729; .NET CLR 2.0.50727; .NET CLR 3.0.30729)";

    public SpeedInfo getSpeedInfo() {
        return speedInfo;
    }

    public void setSpeedInfo(SpeedInfo speedInfo) {
        this.speedInfo = speedInfo;
    }

    private SpeedInfo speedInfo;

    public SpeedBooster(SpeedInfo argSpdInfo) {
        speedInfo = argSpdInfo;
    }

    public boolean boost() throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://bj.wokuan.cn/web/improvespeed.php?ContractNo={ContractNo}&up={up}&old={old}&round={round}")
                .routeParam("ContractNo", speedInfo.getAccID())
                .routeParam("up", speedInfo.getUpSpeedID())
                .routeParam("old", speedInfo.getOldSpeedID())
                .routeParam("round", String.valueOf(speedInfo.getRanNum()))
                .header("Accept", httpAccept)
                .header("Accept-Language", httpLang)
                .header("User-Agent", httpUA)
                .asString();
        return (response.getStatus() == 200) && (response.getBody().contains("success&00000000"));
    }

    public boolean heartbeat() throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://bj.wokuan.cn/web/updateforfifteenmin.php?Mactxt={CompID}&ADSLTxt={ContractNo}&Tick={Tick}&OEM=")
                .routeParam("CompID", speedInfo.getcID())
                .routeParam("ContractNo", speedInfo.getAccID())
                .routeParam("Tick", String.valueOf(7000000 + new Random().nextInt(10000000)))
                .header("Accept", httpAccept)
                .header("Accept-Language", httpLang)
                .header("User-Agent", httpUA)
                .asString();
        return response.getStatus() == 200;
    }

    public boolean stopBoost() throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://bj.wokuan.cn/web/lowerspeed.php?ContractNo={ContractNo}&round={round}")
                .routeParam("ContractNo", speedInfo.getAccID())
                .routeParam("round", String.valueOf(speedInfo.getRanNum()))
                .header("Accept", httpAccept)
                .header("Accept-Language", httpLang)
                .header("User-Agent", httpUA)
                .asString();
        return (response.getStatus() == 200) && (response.getBody().contains("success&00000000"));
    }

    public void test() {
        System.out.println(speedInfo.getRanNum());
    }
}
