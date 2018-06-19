package com.gionee.autotest.html;

/**
 * 用于存放创建HTML文件的标签公用属性
 */
public class HtmlCommon {

    public static final String RESULT_PASS = "通过" ;

    public static final String RESULT_FAIL = "不通过" ;

    /**
     * 得到HTML的样式
     * @return
     */
    public static String getReportStyle(String title ){
        return getReportStyle(title , true) ;
    }

    /**
     * 得到HTML的样式
     * @return
     */
    public static String getReportStyle(String title ,boolean isEmail){
        StringBuilder bf=new StringBuilder();
        bf.append("<HTML>\n");
        bf.append("<HEAD>\n");
        bf.append("<TITLE>AutoTestReport</TITLE>\n");
        bf.append("<META content=\"text/html; charset=utf-8\" http-equiv=Content-Type>\n");
        bf.append("<STYLE type=text/css>\n");
        bf.append("BODY {\n");
        bf.append("FONT: 12px verdana,arial,helvetica\n");
        bf.append("}\n");
        bf.append("TABLE TR TD { FONT-SIZE: 12px }\n");
        bf.append("TABLE TR TH { FONT-SIZE: 12px }\n");
        bf.append("TABLE.details TR TH { BACKGROUND: #a6caf0; FONT-WEIGHT: bold; TEXT-ALIGN: left }\n");
        bf.append("TABLE.details TR TD { BACKGROUND: #eeeee0 }\n");
        bf.append("TABLE.about TR TH {BACKGROUND: #a6caf0; FONT-WEIGHT: bold; TEXT-ALIGN: left}\n");
        bf.append("TABLE.about TR TD {BACKGROUND: #eeeee0}\n");
        bf.append("P {MARGIN-BOTTOM: 1em; MARGIN-TOP: 0.5em; LINE-HEIGHT: 1.5em}\n");
        bf.append("H1 {FONT: bold 165% verdana,arial,helvetica; MARGIN: 0px 0px 5px}\n");
        bf.append("H2 {MARGIN-BOTTOM: 0.5em; FONT: bold 125% verdana,arial,helvetica; MARGIN-TOP: 1em}\n");
        bf.append("H3 {MARGIN-BOTTOM: 0.5em; FONT: bold 115% verdana,arial,helvetica}\n");
        bf.append("H4 {MARGIN-BOTTOM: 0.5em; FONT: bold 100% verdana,arial,helvetica}\n");
        bf.append("H5 {MARGIN-BOTTOM: 0.5em; FONT: bold 100% verdana,arial,helvetica}\n");
        bf.append("H6 {MARGIN-BOTTOM: 0.5em; FONT: bold 100% verdana,arial,helvetica}\n");
        bf.append(".Error {FONT-WEIGHT: bold; COLOR: purple}\n");
        bf.append(".Error {FONT-WEIGHT: bold; COLOR: purple}\n");
        bf.append("TD.P10 {width:10%;}\n");
        bf.append("TD.P15 {width:15%;}\n");
        bf.append("TD.P5 {width:5%;}\n");
        bf.append("TD.P20 {width:20%;}\n");
        bf.append("TD.P25 {width:25%;}\n");
        bf.append("TD.P30 {width:30%;}\n");
        bf.append("TD.P35 {width:35%;}\n");
        bf.append("TD.P40 {width:40%;}\n");
        bf.append("TD.P45 {width:45%;}\n");
        bf.append("</STYLE>\n");
        bf.append("</HEAD>\n") ;
        bf.append("<BODY>\n");
        bf.append("<H1 align=center>"+title+"</H1><BR>\n");
        if(isEmail){
            bf.append("<TABLE width=\"95%\"><TBODY><TR>\n");
            bf.append("<TD align=left>提示:点击表中带下划线数字可以进入详细测试结果页面!</TD>\n");
            bf.append("<TD align=right>测试平台部自动化组</TD></TR></TBODY></TABLE>\n");
            bf.append("<HR align=left SIZE=1 width=\"95%\">\n");
        }
        bf.append("\n");
        return bf.toString();
    }

    /**
     * HTML结束的文本标签组合
     * @return
     */
    public static String getEndHtmlTag(){
        StringBuilder bf=new StringBuilder();
        bf.append("</BODY>\n");
        bf.append("</HTML>\n");
        return bf.toString() ;
    }
}
