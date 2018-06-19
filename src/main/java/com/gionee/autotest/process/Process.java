package com.gionee.autotest.process;

import com.gionee.autotest.runner.Configuration;

/**
 * 抽象的处理工厂，定义处理的抽象方法
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-27 14:22:20
 */
public interface Process {

    /**
     * 处理要解析的Log
     */
    Object analysis(Configuration configuration) ;

}
