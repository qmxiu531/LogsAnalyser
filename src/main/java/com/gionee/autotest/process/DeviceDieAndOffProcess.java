package com.gionee.autotest.process;

import com.gionee.autotest.model.DeviceDieOffModel;
import com.gionee.autotest.runner.Configuration;
import com.gionee.autotest.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析死机重启的Excel结果文件
 *
 * @author Viking Den
 * @version 1.0
 * @date 2016-3-2 15:58:33
 */
public class DeviceDieAndOffProcess implements Process{

    private static final String TAG = DeviceDieAndOffProcess.class.getSimpleName() ;

    List<DeviceDieOffModel> deviceDieOffs = null ;

    public Object analysis(Configuration configuration) {
        deviceDieOffs = new ArrayList<DeviceDieOffModel>() ;
        try{
            FileInputStream in ;
            HSSFWorkbook wb ;
            //搜索工作区目录下是否有AndroidException.xls文件
            File[] files = Util.listFiles(configuration.getWorkDir() , new DieAndOffResultFileFilter());
            if(files != null && files.length > 0){
                for(File file :files) {
                    if(file == null) return null;
                    Log.i(TAG, "当前解析的死机重启文件名:" + file.getName());
                    in = new FileInputStream(file);
                    wb = new HSSFWorkbook(in);
                    String name ;
                    String project ;
                    DeviceDieOffModel deviceDieOffModel ;

                    for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                        HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                        if(hssfSheet == null || hssfSheet.getSheetName().equals("总表")
                                || hssfSheet.getSheetName().contains("_")){
                            continue ;
                        }
                        Log.i(TAG , "Sheet name : " + hssfSheet.getSheetName());
                        String sheetName = hssfSheet.getSheetName() ;
                        String[] args = sheetName.split("-") ;
                        if(args != null && args.length > 1){
                            name = args[1] ;
                            project = args[0] ;
                            //从第二行开始读取
                            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                                if (hssfRow != null ) {
                                    //读取Exception信息
                                    HSSFCell exception = hssfRow.getCell(0);
                                    //读取出现次数的信息
                                    HSSFCell total = hssfRow.getCell(1);

                                    if(!(getValue(exception).equals("")
                                            && getValue(total).equals(""))){
                                        String[] errorInfo = getFormatErrorInfo(name , getValue(exception)) ;
                                        String[] timeAndPath = getTimeAndPath(getValue(exception)) ;
                                        deviceDieOffModel = new DeviceDieOffModel() ;
                                        deviceDieOffModel.setName(name);
                                        deviceDieOffModel.setProject(project);
                                        try {
                                            deviceDieOffModel.setCount(Integer.parseInt(formatNumberStr(getValue(total))));
                                        }catch (Exception e){
                                            deviceDieOffModel.setCount(1);
                                        }
                                        deviceDieOffModel.setError(errorInfo[0]);
                                        deviceDieOffModel.setDescription(errorInfo[1]);
                                        deviceDieOffModel.setLog(getValue(exception));
                                        if (timeAndPath != null){
                                            Log.i("db 时间 : "+ timeAndPath[0] + " , 路径 ： " + timeAndPath[1]);
                                            deviceDieOffModel.setDbtime(timeAndPath[0]);
                                            deviceDieOffModel.setDbpath(timeAndPath[1]);
                                        }
                                        // 添加去重功能
                                        if (!deviceDieOffs.contains(deviceDieOffModel))
                                            deviceDieOffs.add(deviceDieOffModel) ;
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                Log.i(TAG , "工作区无AndroidException.xls文件");
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i(TAG , "解析死机、重启出现错误 ：" + e.getMessage());
        }
        return deviceDieOffs ;
    }

    private String[] getTimeAndPath(String value) {
        Log.i("解析时间和路径");
        if (value == null){
            return null ;
        }
        String time = null , path = null ;
        String[] lines = value.split("\n") ;
        Log.i("解析时间和路径，log的行数："+lines.length);
        if (lines.length > 0){
            for (String line : lines){
                if (line.contains("url: ")){
                    try {
                        path = line.split("rl:")[1] ;
                    }catch (Exception e){
                        //maybe no path value
                        e.printStackTrace();
                    }

                }else if (line.contains("dbgtime:")){
                    try {
                        time = line.split("time:")[1] ;
                    }catch (Exception e){
                        //maybe no path value
                        e.printStackTrace();
                    }
                }
            }
        }
        return new String[]{time , path} ;
    }

    /**
      * 解析死机重启的Exception log段，截取出出错的title
      *@param
      *@author Viking Den
      *@version 1.0
      *@return
      *@date 2016-03-07 15:07:06
      */
    public String[] getFormatErrorInfo(String name , String exceptionLog){
        String error = null ;
        String description = null ;
        //读取第一行
        String[] lines = exceptionLog.split("\n") ;
        //如果没有换行符和出错信息Log为空的话，指定为N/A
        if(!(lines!= null && lines.length > 0) || exceptionLog.equals("")){
            return new String[]{"N/A" ,"N/A"} ;
        }
        String parseLine = lines[0] ;
//        Log.i("Line : " + parseLine);
        if(name.equals(DieOff.JE.getName())){
//            String shortMsg ;
            String errorName = null ;
            String[] args = parseLine.split(":") ;
            if(args != null && args.length > 1){
                errorName = args[0].trim() ;
//                shortMsg =  args[1] ;
            }else{
                //如果当前行没有：分隔符
                errorName = parseLine ;
            }
            for(Exceptions exception : Exceptions.values()){
                if(exception.getName().equals(errorName)){
                    error = exception.getName() ;
                    description = exception.getDescription() ;
                    break ;
                }
            }
            //如果Error没有被赋值
            if(error == null){
                error = errorName ;
                description = "N/A" ;
            }
            return new String[]{error , description} ;
        }
        //如果不是JE错误，直接返回第一行作为错误和描述
        return new String[]{parseLine , "N/A"} ;
    }

    /**
      *格式化数字字符串
      *@param
      *@author Viking Den
      *@version 1.0
      *@return
      *@date 2016-03-07 15:15:06
      */
    public String formatNumberStr(String text) {
        if(text == null || !text.contains(".")) return text ;
        if(text.equals("")) return "0" ;
        return text.substring(0, text.indexOf("."));
    }

    /**
      *格式化单元格的值
      *@param
      *@author Viking Den
      *@version 1.0
      *@return
      *@date 2016-03-07 15:15:22
      */
    private String getValue(HSSFCell xssfRow) {
         if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
             return String.valueOf(xssfRow.getBooleanCellValue());
         } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
             return String.valueOf(xssfRow.getNumericCellValue());
         } else {
            return xssfRow.getStringCellValue();
        }
   }

}
