package com.gionee.autotest.runnable;

import com.gionee.autotest.process.AppCrashLogProcess;
import com.gionee.autotest.process.DeviceDieAndOffProcess;
import com.gionee.autotest.process.Process;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Log;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * 死机重启问题的Log解析流程
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 09:52:57
 */
public class DeviceDieOffCallable implements Callable{

    private static final String TAG = DeviceDieOffCallable.class.getSimpleName() ;

    private Configuration configuration ;

    public DeviceDieOffCallable(){
        configuration = ConfigurationInjector.configuration() ;
    }

    public Object call() {
        Log.i(TAG , "执行App 死机重启分析任务开始");
        long case_start = System.currentTimeMillis() ;
        Process deviceDieAndOffProcess = new DeviceDieAndOffProcess() ;
        Object result = deviceDieAndOffProcess.analysis(configuration);
        long case_end = System.currentTimeMillis() ;
        Log.i(TAG , "执行App 死机重启分析所用时间：" + (case_end -case_start) / 1000.0 );
        Log.i(TAG , "执行App 死机重启分析任务完毕");
        return result ;
    }
}
