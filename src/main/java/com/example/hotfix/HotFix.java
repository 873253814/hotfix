package com.example.hotfix;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class HotFix {

    public static void main(String[] args) throws Exception {

        String properties = HotFix.class.getClassLoader().getResource("properties").getPath();
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver("D:\\hotfix\\src\\main\\resources\\properties");

        FileAlterationListener fileAlterationListener =new FileAlterationReload();
        //注册监听器
        fileAlterationObserver.addListener(fileAlterationListener);
        FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor();
        //注册观察者
        fileAlterationMonitor.addObserver(fileAlterationObserver);
        //启动监听
        fileAlterationMonitor.start();
        //让主线程别这么快结束。
        Thread.sleep(1000000);

    }

}
