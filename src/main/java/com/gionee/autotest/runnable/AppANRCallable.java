package com.gionee.autotest.runnable;

import com.gionee.autotest.process.AppANRLogProcess;
import com.gionee.autotest.process.AppCrashLogProcess;
import com.gionee.autotest.process.Process;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Log;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * 应用程序类的处理
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 09:52:57
 */
public class AppANRCallable implements Callable {

    private static final String TAG = AppANRCallable.class.getSimpleName() ;

    private Configuration configuration ;

    public AppANRCallable(){
        configuration = ConfigurationInjector.configuration() ;
    }

    public Object call() {
        Log.i(TAG , "执行App ANR分析任务开始");
        long case_start = System.currentTimeMillis() ;
        Process ANRLogProcess = new AppANRLogProcess() ;
        Object result = ANRLogProcess.analysis(configuration);
        long case_end = System.currentTimeMillis() ;
        Log.i(TAG , "执行App ANR分析所用时间：" + (case_end -case_start) / 1000.0 );
        Log.i(TAG , "执行App ANR分析任务完毕");
        return result ;
    }
}
