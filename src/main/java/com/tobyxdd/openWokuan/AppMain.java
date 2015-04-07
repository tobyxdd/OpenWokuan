package com.tobyxdd.openWokuan;

/*
OpenWokuan Speedbooster CLI
 */

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.cli.*;
import org.apache.http.client.HttpResponseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class AppMain {

    static private SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] ");

    static public void main(String[] args) {
        log("OpenWokuan by Toby Huang");
        log("An open-source Beijing Unicom Speedbooster.");
        Options options = createOptions();
        try {
            CommandLine argcmd = new GnuParser().parse(options,args);
            String account;
            if(argcmd.hasOption("h"))
            {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("[OpenWokuan]",options,true);
                return;
            }
            if(argcmd.hasOption("a"))
            {
                account=argcmd.getOptionValue("a");
            }else
            {
                log("No account is specified in the parameters!");
                //log("Enter your account ID -");
                //Scanner scanner=new Scanner(System.in);
                //account=scanner.nextLine();
                account="100000000000";
            }
            log("Retrieving account information...");
            SpeedInfo speedInfo = new SpeedInfo(account);
            printSpeedInfo(speedInfo);
            if(argcmd.hasOption("i"))return;
            SpeedBooster speedBooster = new SpeedBooster(speedInfo);
            if(argcmd.hasOption("x")&&(speedInfo.isBoosting()))
            {
                log("Stop boosting...");
                speedBooster.stopBoost();
                log("Stopped.");
                speedInfo.refreshInfo();
                printSpeedInfo(speedInfo);
                return;
            }
            boolean bs;
            if(!speedInfo.isBoosting()) {
                log("Boosting...");
                bs = speedBooster.boost();
            }else {
                log("Already boosted!");
                bs = true;
            }
            if(bs)
                log("Boosting completed.");
            else
                log("An error occurred.");
            speedInfo.refreshInfo();
            printSpeedInfo(speedInfo);
            if(bs&&(!argcmd.hasOption("n")))
            {
                //15min
                while(true) {
                    log("Sending heartbeat...");
                    if (!speedBooster.heartbeat()) log("Heartbeat error (NOT fatal).");
                    log("Waiting 15min for the next heartbeat packet...");
                    Thread.sleep(15 * 60 * 1000);
                }
            }
        } catch (ParseException e) {
            log("Bad arguments.");
            log(e.getLocalizedMessage());
        } catch (InterruptedException e) {
            log("Thread interrupted.");
            log(e.getLocalizedMessage());
        } catch (UnirestException e) {
            log("Network error.");
            log(e.getLocalizedMessage());
        } catch (HttpResponseException e) {
            log("API error.");
            log(e.getLocalizedMessage());
        }
    }

    static private void printSpeedInfo(SpeedInfo speedInfo)
    {
        log("---------------");
        log("Account ID - "+speedInfo.getAccID());
        log("Boost Stat - "+(speedInfo.isBoosting()?"* TRUE *":"* FALSE *"));
        log("Hours Left - "+speedInfo.getHoursLeft());
        log("Old Speed  - "+speedInfo.getOldSpeed()+(speedInfo.getOldSpeed()==512?" Kbps":" Mbps"));
        log("New Speed  - "+speedInfo.getUpSpeed()+" Mbps");
        log("---------------");
    }

    static private void log(String strLog)
    {
        String timeStamp = df.format(Calendar.getInstance().getTime());
        System.out.println(timeStamp + strLog);
    }

    static private Options createOptions()
    {
        Options options=new Options();
        options.addOption("h","help",false,"Show this help message.");
        options.addOption("a","account",true,"Specify the account ID.");
        options.addOption("n","no-renewal",false,"Disable the automatic renewal.");
        options.addOption("i","info",false,"Just print the account status.");
        options.addOption("x","stop",false,"Stop the speed boosting.");
        return options;
    }
}
