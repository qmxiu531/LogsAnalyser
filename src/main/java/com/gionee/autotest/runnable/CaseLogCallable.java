package com.gionee.autotest.runnable;

import com.gionee.autotest.process.CaseLogProcess;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.Log;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * 创建Case的Log 多线程执行对象
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-29 11:22:20
 */
public class CaseLogCallable implements Callable {

    private static final String TAG = CaseLogCallable.class.getSimpleName() ;

    private Configuration configuration ;


    public CaseLogCallable(){
        configuration = ConfigurationInjector.configuration() ;
    }

    public Object call() {
        Log.i(TAG , "执行Case任务开始");
        long case_start = System.currentTimeMillis() ;
        CaseLogProcess caseLogProcess = new CaseLogProcess() ;
        Object result = caseLogProcess.analysis(configuration);
        long case_end = System.currentTimeMillis() ;
        Log.i(TAG , "执行Case任务所用时间：" + (case_end -case_start)/1000.0 );
        Log.i(TAG , "执行Case任务完毕");
        return result ;
    }
}
