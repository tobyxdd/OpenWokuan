OpenWokuan 联通提速客户端Java版
----------
完全模拟[北京联通沃宽PC版](http://wokuan.bbn.com.cn/)所用的接口，兼容全平台，远离官方流氓

    usage: [OpenWokuan] [-a <arg>] [-h] [-i] [-n] [-x]
     -a,--account <arg>   Specify the account ID.
     -h,--help            Show this help message.
     -i,--info            Just print the account status.
     -n,--no-renewal      Disable the automatic renewal.
     -x,--stop            Stop the speed boosting.

此为CLI命令行版，**Android版哪天心情好也会做**

已知Bug

 - 10M用户可能显示为2M，此为联通自身系统问题，且完全**不影响**实际提速功能

一分钟教程
----------
[在此下载最新Release](https://github.com/tobyxdd/OpenWokuan/releases)后解压进入bin，Windows下直接双击OpenWokuan.bat或Linux下OpenWokuan，**此种不带任何参数启动的方式会自动开始提速并按照联通接口15分钟发送一次心跳包保持提速状态，直到用户手动结束程序。**
![s1](http://toby.so/wp-content/uploads/2015/04/s1.png)

可用参数——
 1. -a <帐号> 可指定操作账户（不带则默认为当前连接的）
 2. -h 显示详细参数说明
 3. -i 不提速，仅获取输出当前帐号信息
 4. -n 不发送心跳包（提速后程序立即退出，只能提速15分钟左右）
 5. -x 停止提速