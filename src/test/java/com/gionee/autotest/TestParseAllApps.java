package com.gionee.autotest;

import com.gionee.autotest.model.AppModel;
import com.gionee.autotest.util.Log;
import com.gionee.autotest.util.Util;

import java.io.File;
import java.util.List;

/**
 * 测试解析所有的应用文件
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-1 09:35:28
 */
public class TestParseAllApps {

  /*  public void testParseAllApps() throws Exception{
        try {
            List<AppModel> apps = Util.parseAllApps(new File("D:\\logs\\allpackagecycle.txt")) ;

            for(AppModel appModel : apps){
                Log.i("********************************************");
                Log.i("Pkg : " + appModel.getPkg());
                Log.i("AppName :" + appModel.getAppName());
                Log.i("Version : " + appModel.getVersion());
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i("Exception : " + e.getMessage());
        }

    }*/
}
