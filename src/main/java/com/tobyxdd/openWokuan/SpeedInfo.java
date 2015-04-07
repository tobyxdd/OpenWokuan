package com.tobyxdd.openWokuan;

/*
SpeedInfo
For reading the current account information.
 */

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.HttpResponseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class SpeedInfo {

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getOldSpeedID() {
        return oldSpeedID;
    }

    public void setOldSpeedID(String oldSpeedID) {
        this.oldSpeedID = oldSpeedID;
    }

    public String getUpSpeedID() {
        return upSpeedID;
    }

    public void setUpSpeedID(String upSpeedID) {
        this.upSpeedID = upSpeedID;
    }

    public String getAccID() {
        return accID;
    }

    public void setAccID(String accID) {
        this.accID = accID;
    }

    public int getOldSpeed() {
        return oldSpeed;
    }

    public void setOldSpeed(int oldSpeed) {
        this.oldSpeed = oldSpeed;
    }

    public int getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(int upSpeed) {
        this.upSpeed = upSpeed;
    }

    public int getRanNum() {
        return ranNum;
    }

    public void setRanNum(int ranNum) {
        this.ranNum = ranNum;
    }

    public float getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(float hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public boolean isBoosting() {
        return isBoosting;
    }

    public void setIsBoosting(boolean isBoosting) {
        this.isBoosting = isBoosting;
    }

    private String actionStatus;
    private String oldSpeedID;
    private String upSpeedID;

    private int oldSpeed,upSpeed,ranNum;
    private float hoursLeft;
    private boolean isBoosting;

    private String rMac = randomMAC();

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    private String cID = genCompID();
    private String accID;

    public SpeedInfo(String argAccID) throws UnirestException,HttpResponseException {
        accID = argAccID;
        refreshInfo();
    }

    public void refreshInfo() throws UnirestException,HttpResponseException
    {
        HttpResponse<String> infoResp = Unirest.get("http://bj.wokuan.cn/web/startenrequest.php?ComputerMac={ComputerMac}&ADSLTxt={ADSLTxt}&Type=2&reqsn={reqsn}&oem=00&ComputerId={ComputerId}")
                .routeParam("ComputerMac", rMac)
                .routeParam("ADSLTxt", accID)
                .routeParam("reqsn",genReqSN())
                .routeParam("ComputerId", cID)
                .header("Accept", SpeedBooster.httpAccept)
                .header("Accept-Language", SpeedBooster.httpLang)
                .header("User-Agent", SpeedBooster.httpUA)
                .asString();
        if(infoResp.getStatus()==200)
        {
            Document respDoc = Jsoup.parse(infoResp.getBody());
            String[] respArgs = respDoc.getElementById("webcode").text().split("&");
            for (String it : respArgs)
            {
                String[] kp = it.split("=");
                if(kp[0].equalsIgnoreCase("ov"))actionStatus=kp[1];
                if(kp[0].equalsIgnoreCase("os"))oldSpeed=Integer.parseInt(kp[1]);
                if(kp[0].equalsIgnoreCase("up"))upSpeed=Integer.parseInt(kp[1]);
                if(kp[0].equalsIgnoreCase("glst"))hoursLeft=Float.parseFloat(kp[1]);
                if(kp[0].equalsIgnoreCase("gus"))upSpeedID=kp[1];
                if(kp[0].equalsIgnoreCase("old"))oldSpeedID=kp[1];
                if(kp[0].equalsIgnoreCase("cn"))accID=kp[1];//*
                if(kp[0].equalsIgnoreCase("stu"))isBoosting=kp[1].equals("1");
                if(kp[0].equalsIgnoreCase("random"))ranNum=Integer.parseInt(kp[1]);
            }
        }else
            throw new HttpResponseException(infoResp.getStatus(),infoResp.getStatusText());
    }

    private String randomMAC(){
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);
        macAddr[0] = (byte)(macAddr[0] & (byte)254);
        StringBuilder sb = new StringBuilder(18);
        for(byte b : macAddr){
            if(sb.length() > 0)
                sb.append("-");
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
    }

    private String genReqSN()
    {
        return "00TF"+df.format(Calendar.getInstance().getTime())+"009262";
    }
    private String genCompID()
    {
        StringBuilder bd = new StringBuilder("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int range = bd.length();
        for (int i = 0; i < 18; i ++) {
            sb.append(bd.charAt(random.nextInt(range)));
        }
        return "BFEBFBFF"+sb.toString();
    }
}
