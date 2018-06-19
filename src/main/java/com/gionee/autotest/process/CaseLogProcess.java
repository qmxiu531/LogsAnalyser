package com.gionee.autotest.process;

import com.gionee.autotest.html.HtmlReporter;
import com.gionee.autotest.model.CaseInfo;
import com.gionee.autotest.model.CaseModel;
import com.gionee.autotest.model.NAModel;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.runner.ConfigurationInjector;
import com.gionee.autotest.util.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

/**
 * 处理Case Log
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-2-27 14:26:00
 */
public class CaseLogProcess implements Process{

    private static final String TAG = CaseLogProcess.class.getSimpleName() ;

    /**
     * Case执行时的Log TAG
     */
    private String CASE_TAG ;

    /**
     * 全局的失败Case
     */
    List<CaseModel> failedCases ;

    /**
     * 用于保存失败截取的数据信息
     */
    Map<String , List<String>> photos ;

    /**
     * 保存case的统计信息
     */
    CaseInfo caseInfo ;

    /**
     * 统计NA的原因
     */
    List<NAModel> naModels ;

    File outDir ;

    private List<String> caseCount ;


    boolean isFailed = false ;
    boolean isCutting = false ;
    boolean isCuttingProcedure = false ;
    boolean isCuttingErrorStack = false ;

    boolean isNA = false ;
    NAModel naModel = null ;
    boolean hasNA = false ;
    String result = null ;

    String name = null ;
    String shortMsg = null ;
    String caseSTMid = null ;
    StringBuilder caseProcedure = null ;
    StringBuilder caseErrorStack = null ;
    FileWriter fileWriter = null ;
    File curCaseFile = null ;

    public Object analysis(Configuration configuration) {
        Log.i(TAG , "进入analysis的处理逻辑");
        //添加对TAG的适配处理
        CASE_TAG = configuration.getTag() ;
        String line = null ;
        try {
            caseInfo = new CaseInfo() ;
            naModels = new ArrayList<NAModel>() ;
            failedCases = new ArrayList<CaseModel>() ;
            outDir = configuration.getOutDir() ;
            File[] files = configuration.getLogFiles() ;
            photos = parseFailPhotos(configuration.getWorkDir() ,configuration.getBuildId() , configuration.getOutDir()) ;
            BufferedReader in = null;
            BufferedInputStream fis = null ;
            String caseName = null ;
            caseCount = new ArrayList<String>() ;
            for(File file : files){
                Log.i(TAG, "当前解析的文件名:" + file.getName());
                fis = new BufferedInputStream(new FileInputStream(file));
                in = new BufferedReader(new InputStreamReader(fis,"utf-8"), Constant.BUFFER_SIZE);

                while((line = in.readLine()) != null){
                    //如果当前行包含有CASE_START_TAG，进入case处理逻辑
                    if(line.contains(CASE_TAG)
                            && line.contains(Constant.CASE_START_TAG)){
//                            Log.i(TAG , line);
                        caseName = handleStart(line);
                        Log.i("caseName : " + caseName) ;
                    }else if (line.contains(CASE_TAG)
                            && line.contains(Constant.CASE_NA_TAG)){
                            isNA = true ;
                    }/*else if (line.contains(CASE_TAG)
                            && line.contains(Constant.CASE_END_TAG)){

                    }*/else {
                        if (line.contains(CASE_TAG) && line.contains(Constant.N_A_RESULT_TAG)
                                && !hasNA){
                            naModel = parseNaModel(caseName ,line) ;
                            if (naModel != null){
                                hasNA = true ;
                            }
                        }
                        //判断是否在截取 ， 如果是在截取，则添加当前行到sb
                        if (isCutting){
                            if (line.contains(CASE_TAG)){

                                if (line.contains(": Result:")){
                                    try{
                                        result = line.split(": Result:")[1] ;
//                                        Log.i(TAG , "result xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx: " + result);
                                    }catch (Exception e){
                                        //ignore
                                    }
                                }
                                if (line.contains(Constant.CASE_FAIL_TAG)){
                                    //设置Case为失败
                                    isFailed = true ;
                                    isCuttingProcedure = false ;
                                    isCuttingErrorStack = true ;
                                } else if (line.contains(Constant.CASE_ERROR_MSG_TAG)){
                                    String[] args = line.split(Constant.CASE_ERROR_MSG_TAG) ;
                                    if(args != null && args.length > 1){
                                        shortMsg = args[1] ;
                                    }
                                } else if (line.contains(Constant.CASE_STM_CASE_ID)){
                                    String[] args = line.split(Constant.CASE_STM_CASE_ID) ;
                                    if(args != null && args.length > 1){
                                        caseSTMid = args[1] ;
                                    }
                                } else if (line.contains(Constant.CASE_PROCEDURE_START_TAG)){
                                    isCuttingProcedure = true ;
                                } else if (line.contains(Constant.CASE_PROCEDURE_END_TAG)){
                                    isCuttingProcedure = false ;
                                } else if(line.contains(Constant.CASE_ERROR_STACK_TRACE_END_TAG)){
                                    isCuttingErrorStack = false ;
                                }
                                //截取Case执行步骤的Log
                                else if(isCuttingProcedure){
                                    if (!(line.contains(Constant.D_LOG_MASHMALLOW) ||
                                            line.contains(Constant.D_LOG_MASHMALLOW_BELOW))){
                                        //先截取,只包含后面的文字,且过滤Pass的字段
                                        String[] args = line.split("\\): ") ;
                                        if(args != null && args.length > 1){
                                            if(!"Pass".equals(args[1].trim())){
                                                caseProcedure.append(args[1] + Constant.NEW_LINE) ;
                                            }
                                        }
                                    }
                                }
                                //截取Case的出错Log
                                else if(isCuttingErrorStack){
                                    String[] args = line.split("\\): ") ;
                                    if(args != null && args.length > 1){
                                        caseErrorStack.append(args[1] + Constant.NEW_LINE) ;
                                    }
                                }
                            }
                            //截取整个的Case Log
                            fileWriter.write(line + Constant.NEW_LINE) ;
                        }
                    }
                }
            }
            commitLastData("");
            if(in != null){
                in.close();

            }
            if (fis != null){
                fis.close();
            }
        } catch (Exception ex) {
            Log.i(TAG , "Parse case log failed . line text ： " + line);
            Log.i(TAG , "Parse case log failed . message : " + ex.getMessage());
            ex.printStackTrace();

        }

        int errorTimes = ConfigurationInjector.configuration().getErrorTimes() ;
        Log.i("errorTimes  : " + errorTimes);
        Log.i(TAG , "FILTER..........................");
        if (failedCases != null && failedCases.size() > 0){
            for(CaseModel caseModel : failedCases){
                Log.i(TAG , "Case name " + caseModel.getName() +" fail count  : " + caseModel.getCount());
                //TODO CHANGE TO 1
                if (caseModel.getCount() != errorTimes){
                    File file = new File(outDir, caseModel.getName() + ".txt") ;
                    Log.i(TAG , "File path : " + file.getPath());
                    if(file.exists()){
                        Log.i(TAG , "Finally delete file : " + file.getPath());
                        file.delete() ;
                    }
                }
            }
        }
        Log.i("all case size : " + caseCount.size());
        caseInfo.setCaseTotal(caseCount.size());
        caseInfo.setFailedCases(failedCases);

        //处理NA
        parseNAFile() ;

        caseInfo.setNaModels(naModels);
        return caseInfo ;
    }

    private void parseNAFile() {
        Configuration config = ConfigurationInjector.configuration() ;
        File dir_ws = config.getWorkDir() ;
        File con_file = new File(dir_ws.getPath() + File.separator + "condition" + File.separator + "conditionCase.txt") ;
        if (con_file.exists()){
            Log.i(TAG , "conditionCase.txt文件存在");
            String content = getFileContent(con_file) ;
            if (!content.contains(":")){
                Log.i(TAG , "conditionCase.txt无数据");
                return ;
            }
            List<NAModel> fNaModels = new ArrayList<NAModel>() ;
            List<NAModel> naModels_ = new ArrayList<NAModel>() ;
            Log.i(TAG , "conditionCase.txt有数据");
            String[] items = content.split("##") ;
            if (items != null && items.length > 0){
                for (String item : items){
                    if (item.contains(":")){
                        String[] curs = item.split(":") ;
                        String key = curs[0] ;
                        if (!curs[1].equals("[]")){
                            String[] values = curs[1].split("\\[")[1].split("]")[0].split(",") ;
                            Log.i(TAG , "key :　"+ key);
//                        Log.i(TAG , "value :　"+ curs[1]);
                            Log.i(TAG , "value length :　"+ values.length);
                            if (values.length > 0){
                                NAType type = NAType.getType(key) ;
                                if (type != null){
                                    Log.i(TAG , "添加NA数据");
                                    List<String> cases = new ArrayList<String>() ;
                                    for (String value : values){
                                        cases.add(value) ;
                                    }
                                    NAModel naModel = new NAModel(type.getType() , type.getReason()) ;
                                    naModel.setCaseName(cases);

                                    if (naModels.contains(naModel)){
                                        //NA中已包含去除的环境条件
                                        Log.i(TAG , "NA中已包含去除的环境条件");
                                        for (NAModel naModel_ : naModels){
                                            if (naModel_.equals(naModel)){
                                                Log.i("Na cases  : " + naModel_.getCaseName().toString());
                                                fNaModels.add(naModel_) ;
                                                break ;
                                            }
                                        }
                                    }
                                    naModels_.add(naModel) ;
                                }
                            }
                        }

                    }
                }
            }
            /*for (NAModel n : fNaModels){
                Log.i("case xxxxxx : " + n.getCaseName().toString());
            }*/
            HtmlReporter.generateExcludeNAHtml(fNaModels) ;
            if (naModels_ != null && naModels_.size() > 0){
                for (NAModel n : naModels_){
                    handleNAReasons(n);
                }
            }
        }
    }

    private String getFileContent(File file){
        StringBuilder sb = new StringBuilder() ;
        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line ;
            while( (line=in.readLine())!=null )
            {
                sb.append(line) ;
            }
            in.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString() ;
    }

    private void commitLastData(String line ) throws Exception{
        //提交上次的数据
        if (name != null){
            //如果上一条数据没有Result记录，认定为失败
            if (result == null && !isNA){
//                Log.i(TAG , "result is null");
                isFailed = true ;
            }
//            Log.i(TAG , "NAME : " + name + " isFailed : " + isFailed);
            if (naModel != null && isNA){
                handleNAReasons(naModel) ;
            }
            //先判断是否在截取阶段
            if(isCutting){
                //在截取阶段，置截取的标志位为false
                isCutting = false ;
                //添加结束的Log
                fileWriter.write(line + Constant.NEW_LINE) ;
                fileWriter.close();
                //添加一Case失败的数据
                if(isFailed){
                    CaseModel caseModel = new CaseModel() ;
                    caseModel.setName(name);
                    caseModel.setShortMsg(shortMsg);
                    caseModel.setLog(caseErrorStack.toString());
                    //保存STMid
                    if(caseSTMid != null){
                        caseModel.setStmCaseId(caseSTMid);
                    }
                    //保存失败截图
                    if(photos != null && photos.size() > 0){
                        List<String> casePhotos = getFailPhotos(name , photos) ;
                        if(casePhotos != null){
                            caseModel.setFailPhotos(casePhotos);
                        }
                    }
                    //操作步骤添加ErrorMsg
                    if (shortMsg != null){
                        caseProcedure.append("ErrorMsg:" + shortMsg) ;
                    }
                    caseModel.setProcedure(caseProcedure.toString());

                    //添加去重功能
                    if (failedCases.contains(caseModel)){
//                        Log.i(TAG , "Contain case : " + caseModel.getName() );
                        int count ;
                        CaseModel old = null;
                        for(CaseModel model : failedCases){
                            if(model.getName().equals(caseModel.getName())){
                                old = model ;
                                break ;
                            }
                        }
                        if(old != null){
                            count = old.getCount() ;
                            caseModel.setCount(count+1);
                            failedCases.remove(old) ;
                        }
                    }
                    failedCases.add(caseModel) ;
                }else{
                    //如果不是失败的Case，删除Log文件
                    try{
                        curCaseFile.delete() ;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String handleStart(String line) throws Exception{
        commitLastData(line);
        //重置变量值
        name = null ;
        isFailed = false ;

        isNA = false ;
        hasNA = false ;
        naModel = null ;
        result = null ;

        isCuttingProcedure = false ;
        isCuttingErrorStack = false ;

        shortMsg = null ;
        caseSTMid = null ;
        caseProcedure = new StringBuilder() ;
        caseErrorStack = new StringBuilder() ;

        //解析name
        String[] args = line.split(Constant.CASE_START_TAG) ;
        if(args != null && args.length > 1){
            name = args[1] ;
            //BUG FIX for log content :04-05 18:15:50.260 I/gionee.os.autotest(28836): Start:Case_6488104-05 18:15:50.263
            //01-10 11:17:06.015 10971 10994 I gionee.os.autotest: Start:Bluetooth_1000

/*            if (name.length() > 12){
                name = name.substring(0, 10) ;
            }*/
            //判断当前是否包含有Case Name，如果没有 则添加
            if (!caseCount.contains(name)){
                caseCount.add(name) ;
            }
        }
        if (name != null){
            curCaseFile = new File(outDir, name + ".txt") ;
            if (curCaseFile.exists()){
                try{
                    curCaseFile.delete() ;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            fileWriter = new FileWriter(curCaseFile ,true) ;
            //添加开始的Log
            fileWriter.write(line + Constant.NEW_LINE) ;
            //设置isCutting的标志位为true
            isCutting = true ;
        }
        return name ;
    }

    private NAModel parseNaModel(String caseName ,String line){
        String[] args = line.split(Constant.N_A_RESULT_TAG)[1].split(":") ;
        NAModel model = null ;
        if (args != null && args.length > 1) {
            try {
                int type = Integer.parseInt(args[0]);
                String reason = args[1];
                model = new NAModel(type, reason);
                List<String> caseNames = new ArrayList<String>() ;
                caseNames.add(caseName) ;
                model.setCaseName(caseNames);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return model ;
    }

    /**
      *处理NA统计
      *@param
      *@author Viking Den
      *@version 1.0
      *@return
      *@date 2016-04-19 11:10:03
      */
    private void handleNAReasons(NAModel naModel){
        try{
//            I/gionee.os.autotest(14878): N_A_REASON5:网络状况不好，网速过慢
            if (naModels.contains(naModel)){
                Log.i(TAG , "包含NA信息 ："+ naModel.getReason());
                Iterator<NAModel> naModelIterator = naModels.iterator();
                while(naModelIterator.hasNext()){
                    NAModel naModel_ = naModelIterator.next();
                    if (naModel_.equals(naModel)){
                        List<String> caseNames = naModel_.getCaseName() ;
                        caseNames.addAll(naModel.getCaseName()) ;
                        naModel_.setCaseName(caseNames);
//                        naModel_.setCount(naModel_.getCount() + naModel.getCount());
                        break ;
                    }
                }
            }else{
                naModels.add(naModel) ;
            }
        }catch (Exception e){
            //ingore this
            e.printStackTrace();
        }
    }

    /**
     * 通过比对获取失败的Case截图文件
     * @param caseName
     * @param photos
     * @return
     */
    private List<String> getFailPhotos(String caseName , Map<String , List<String>> photos){
        for (Map.Entry<String, List<String>> entry : photos.entrySet()) {
            if(entry.getKey().equals(caseName)){
                return entry.getValue() ;
            }
        }
        return null ;
    }

    /**
      *解析工作区中包含有dongzhou文件夹中的Case截图，保存到Map中
      *@param
      *@author Viking Den
      *@version 1.0
      *@return
      *@date 2016-03-08 14:46:17
      */
    public Map<String,List<String>> parseFailPhotos(File workDir , final String buildId , File outDir) {
        Map<String , List<String>> failPhotos = new HashMap<String, List<String>>() ;
        File[] dongzhouDirs = workDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (dir.isDirectory() && name.startsWith("dongzhou_" + buildId))
                        || (dir.isDirectory() && name.startsWith("dongzhou"));
            }
        }) ;

        String case_name ;
        String file_name ;

        //清空result文件
        File resultDir = new File(outDir , Constant.FAIL_CASE_RESULT_DIR) ;
        if (resultDir.exists()){
            try {
                FileUtils.deleteDirectory(resultDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //创建image_origin文件夹
        File imageOriDir = new File(outDir , Constant.FAIL_PIC_ORIGIN_DIR) ;
        if (imageOriDir.exists()){
            try {
                FileUtils.deleteDirectory(imageOriDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageOriDir.mkdirs() ;

        File imageDir = new File(outDir , Constant.FAIL_PIC_COMPRESS_DIR) ;
        if (imageDir.exists()){
            try {
                FileUtils.deleteDirectory(imageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageDir.mkdirs() ;

        if(dongzhouDirs != null && dongzhouDirs.length > 0){
            for(File dongzhouDir : dongzhouDirs){
                Log.i(TAG , "Dongzhou photo dir : " + dongzhouDir.getName());
                File[] files = dongzhouDir.listFiles(new FailPhotoFileFilter()) ;
                if(files != null && files.length > 0){
                    //给所有的图片根据case名称分类
                    for(File file : files){
                        file_name = file.getName() ;
                        case_name = file_name.split("_")[0] + "_" + file_name.split("_")[1] ;
                        List<String> items ;
                        if(failPhotos.keySet().contains(case_name)){
                            items = failPhotos.get(case_name) ;
                        }else{
                            items = new ArrayList<String>() ;
                        }
                        items.add(file_name) ;
                        failPhotos.put(case_name , items) ;
                    }
                    //拷贝同名失败图片数大于3的文件到result/image_origin文件夹中
                    Log.i(TAG , "拷贝同一失败Case的文件到原始图片文件夹中....");
                    try {
                        if (failPhotos.size() > 0) {
                            for (File file : files) {
                                String key = file.getName().split("_")[0] + "_" + file.getName().split("_")[1];
                                if (failPhotos.containsKey(key)) {
//                                    List<String> values = failPhotos.get(key);
//                                    if (values.size() > 2) {
                                        Log.i(TAG , "Copy file : " + file.getName());
                                        FileUtils.copyFileToDirectory(file, imageOriDir);
//                                    }
                                }
                            }
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            //处理图片的压缩
            Log.i(TAG , "COMPRESSING 1111111....");
            ImageUtil.compressImgDir(imageOriDir.getPath(), imageDir.getPath());
            Log.i(TAG , "COMPRESSING 2222222....");
            //删除Image origin文件夹
            Log.i(TAG , "删除原始的图片文件夹");
            try {
                FileUtils.deleteDirectory(imageOriDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return failPhotos ;
    }
}
