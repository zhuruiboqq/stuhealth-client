/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfirebird;

import static importfirebird.ImportCore.SqlFiled;
import static importfirebird.ImportCore.stringToDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import javax.sql.DataSource;

import com.vastcm.stuhealth.client.AppContext;

/**
 *
 * @author Administrator
 */
public class ImportCore {
    public Connection mysqlconnection = null;  
    public Connection firebirdconnection = null;  
    public static int importcount=0;
    public static int allcount=0;
    public static String DataPath="";
    public static boolean isrun=false;
    public static void Import() throws Exception{
	 if(!isrun){
         isrun=true;  
         Connection fbconn=GetFbConnection();
        Connection mysqlconn=GetMysqlConnection();
        try{
            mysqlconn.setAutoCommit(false);
           
            ArrayList<School> list=GetSchool() ;
            //先测试是否有联网，如果没有联网，则提示连接网络
            //在此处处理得到seccode   list中每个school的seccode都去服务器端取。
            //如果取到了做一个处理，如果没有取到做另外处理
            DeleteAll(mysqlconn);
            ImportSchool(mysqlconn,list);
            ExportTree(fbconn,mysqlconn);
            
            ExportStudent(fbconn,mysqlconn);
            ExportResult(fbconn,mysqlconn);
            
            ExportReport_ZB_Grade(fbconn,mysqlconn);
            ExportReport_ZB_Age(fbconn,mysqlconn);
            ExportReport_TB_Grade(fbconn,mysqlconn);
            ExportReport_TB_Age(fbconn,mysqlconn); 
            mysqlconn.commit();
        }
        catch(SQLException e){
            mysqlconn.rollback();
             throw e;
        }
        catch(ClassNotFoundException e) {
        	mysqlconn.rollback();
            throw e;
        }
	 finally{
            isrun=false;
        }
	}
        
    }
      public static void DeleteAll(Connection mysqlconn) throws SQLException{
//        Statement mysqlstatement=mysqlconn.createStatement();
//        try{
////            mysqlstatement.execute("delete  from Student");
////             mysqlstatement.execute("delete  from Result");
////              mysqlstatement.execute("delete  from Report_ZB_Grade");
////               mysqlstatement.execute("delete  from Report_ZB_Age");
////                mysqlstatement.execute("delete  from Report_TB_Grade");
////                mysqlstatement.execute("delete  from Report_TB_Age");
//                mysqlstatement.execute("delete  from schooltree");
//                 mysqlstatement.execute("delete  from hw_school");
//        }
//        catch(SQLException e){
//            throw e;
//        }
    }
    public static int Count() throws SQLException, ClassNotFoundException{
        Connection firebirdconn=GetFbConnection();
        Statement statement=firebirdconn.createStatement();
        try{
             ResultSet resultSet=statement.executeQuery("select count(*) as scount from student");
             int a1=0;
             if(resultSet.next()){
                 a1=resultSet.getInt("scount");
                allcount+=  resultSet.getInt("scount");
             }
            resultSet=statement.executeQuery("select count(*) as scount from checkdb where tjnd='20131'");
            if(resultSet.next()){
                 a1=resultSet.getInt("scount");
                allcount+=  resultSet.getInt("scount");
            }
            resultSet=statement.executeQuery("select count(*) as scount from TJ_JKHZB_GRADE where tjnd='20131'");
            if(resultSet.next()){
                allcount+=  resultSet.getInt("scount");
                 a1=resultSet.getInt("scount");
            }
            resultSet=statement.executeQuery("select count(*) as scount from TJ_JKHZB_AGE where tjnd='20131'");
            if(resultSet.next()){
                allcount+=  resultSet.getInt("scount");
                a1=resultSet.getInt("scount");
            }
            resultSet=statement.executeQuery("select count(*) as scount from TJ_JKXTB_GRADE where tjnd='20131'");
            if(resultSet.next()){
                allcount+=  resultSet.getInt("scount");
                a1=resultSet.getInt("scount");
            }
            resultSet=statement.executeQuery("select count(*) as scount from TJ_JKXTB_Age where tjnd='20131'");
            if(resultSet.next()){
                allcount+=  resultSet.getInt("scount");
                a1=resultSet.getInt("scount");
            }
            resultSet=statement.executeQuery("select count(*) as scount from cust where js<>5");
             if(resultSet.next()){
                allcount+=  resultSet.getInt("scount");
                a1=resultSet.getInt("scount");
            }
            resultSet=statement.executeQuery("select count(*) as scount from schoolinfo");
             if(resultSet.next()){
                allcount+=  resultSet.getInt("scount");
                a1=resultSet.getInt("scount");
            }
            return allcount;
        }
        catch(SQLException e){
            throw e;
        }
    }
    protected static void ExportStudent(Connection firebirdconn,Connection mysqlconn) throws Exception
    {
       Connection tempconn=GetFbConnection();
        Statement statement=firebirdconn.createStatement();
        Statement tempstatement=tempconn.createStatement();
        Statement mysqlstatement=mysqlconn.createStatement();
        ResultSet resultSet=null;
        ResultSet treeResultSet=null;
        try{
                resultSet=statement.executeQuery("select * from student"); 
        }
        catch(SQLException e){
            throw e;
        }
        Student student=null;
        String classname=null;
        while(resultSet.next())
        {
            student=new Student();
            student.BH=resultSet.getString("BH");
            student.SCHOOLBH=resultSet.getString("SCHOOLBH");
            student.GRADEBH=resultSet.getString("GRADEBH");
            student.CLASSBH=resultSet.getString("CLASSBH");
            student.CLASSName=classname;
            student.RXSJ=resultSet.getString("RXSJ");
            student.XH=resultSet.getString("XH");
            student.XM=resultSet.getString("XM");
            student.XB=resultSet.getString("XB");
            student.CSRQ= resultSet.getDate("CSRQ");
            student.MZ=resultSet.getString("MZ");
            student.ZZMM=resultSet.getString("ZZMM");
            student.SY=resultSet.getString("SY");
            student.XX=resultSet.getString("XX");
            student.SFZ=resultSet.getString("SFZ");
            student.ADDRESS=resultSet.getString("ADDRESS");
            student.FQXM=resultSet.getString("FQXM");
            student.FQDW=resultSet.getString("FQDW");
            student.MQXM=resultSet.getString("MQXM");
            student.MQDW=resultSet.getString("MQDW");
            student.MARK=resultSet.getString("MARK");
            student.XJH=resultSet.getString("XJH");
            student.XMPY=resultSet.getString("XMPY");
             try{
                treeResultSet=tempstatement.executeQuery("select dwmc from Cust where dwbm='"+student.SCHOOLBH+student.CLASSBH+"'");
                 if(treeResultSet.next()){
                   student.CLASSName=treeResultSet.getString("dwmc");
               }
             }
             catch(SQLException e){
                 throw e;
             }
             ImportStudent(mysqlstatement,student);
        }
    }
    protected static void ExportResult(Connection firebirdconn,Connection mysqlconn) throws Exception
    {
        Statement statement=firebirdconn.createStatement();
        Statement mysqlstatement=mysqlconn.createStatement();
        ResultSet resultSet = null;
        try{
            resultSet=statement.executeQuery("select * from checkdb where tjnd='20131'");
        }
        catch(SQLException e){
            throw e;
        }
        Result result;
        while(resultSet.next())
        {
            result=new Result();
            result.TJND=resultSet.getString("TJND"); 
            result.BH=resultSet.getString("BH"); 
            //result.CHECKID= Integer.valueOf(resultSet.getString("CHECKID")).intValue(); 
            result.SCHOOLBH=resultSet.getString("SCHOOLBH");
            result.GRADEBH=resultSet.getString("GRADEBH"); 
            result.CLASSBH=resultSet.getString("CLASSBH"); 
            result.RXSJ=resultSet.getString("RXSJ"); 
            result.XH=resultSet.getString("XH"); 
            result.SCHOOL_NAME=resultSet.getString("SCHOOL_NAME"); 
            result.GRADE_NAME=resultSet.getString("GRADE_NAME"); 
            result.CLASS_NAME=resultSet.getString("CLASS_NAME"); 
            result.XM=resultSet.getString("XM");
            result.XB=resultSet.getString("XB"); 
            result.CSRQ=resultSet.getDate("CSRQ") ; 
            result.MZ=resultSet.getString("MZ"); 
            result.ZZMM=resultSet.getString("ZZMM");
            result.SY=resultSet.getString("SY"); 
            result.TJRQ=resultSet.getDate("TJRQ"); 
            result.NL=resultSet.getInt("NL");
            result.MARK=resultSet.getString("MARK"); 
            result.SG=resultSet.getDouble("SG");
            result.SGDJQ=resultSet.getString("SGDJQ"); 
            result.SGDJD=resultSet.getString("SGDJD"); 
            result.SGBJ=resultSet.getString("SGBJ"); 
            result.TZ=resultSet.getDouble("TZ"); 
            result.TZDJQ=resultSet.getString("TZDJQ"); 
            result.TZDJD=resultSet.getString("TZDJD"); 
            result.TZBJ=resultSet.getString("TZBJ");
            result.YYPJ=resultSet.getString("YYPJ"); 
            result.YYPJX=resultSet.getString("YYPJX"); 
            result.TXPJ=resultSet.getString("TXPJ"); 
            result.FYPJ=resultSet.getString("FYPJ"); 
            result.XW= resultSet.getDouble("XW");  
            result.XWDJQ=resultSet.getString("XWDJQ"); 
            result.XWDJD=resultSet.getString("XWDJD"); 
            result.XWBJ=resultSet.getString("XWBJ"); 
            result.FHL=resultSet.getDouble("FHL");  
            result.FHLDJQ=resultSet.getString("FHLDJQ"); 
            result.FHLDJD=resultSet.getString("FHLDJD"); 
            result.FHLBJ=resultSet.getString("FHLBJ");
            result.WL=resultSet.getDouble("WL");  
            result.WLDJQ=resultSet.getString("WLDJQ"); 
            result.WLDJD=resultSet.getString("WLDJD"); 
            result.WLBJ=resultSet.getString("WLBJ"); 
            result.MB=resultSet.getDouble("MB");  
            result.MBBJ=resultSet.getString("MBBJ"); 
            result.SSY=resultSet.getDouble("SSY");  
            result.SSYBJ=resultSet.getString("SSYBJ");
            result.SZY=resultSet.getDouble("SZY");  
            result.SZYBJ=resultSet.getString("SZYBJ"); 
            result.LSL=resultSet.getDouble("LSL");  
            result.RSL=resultSet.getDouble("RSL");  
            result.QTYB=resultSet.getString("QTYB"); 
            result.LQG=resultSet.getString("LQG"); 
            result.RQG=resultSet.getString("RQG"); 
            result.LSY=resultSet.getString("LSY"); 
            result.RSY=resultSet.getString("RSY"); 
            result.BS=resultSet.getString("BS"); 
            result.TL=resultSet.getString("TL"); 
            result.XJ=resultSet.getString("XJ"); 
            result.BB=resultSet.getString("BB"); 
            result.QSBNUM=resultSet.getInt("QSBNUM"); 
            result.RQB=resultSet.getInt("RQB"); 
            result.RQS=resultSet.getInt("RQS"); 
            result.RQH=resultSet.getInt("RQH");  
            result.HQB=resultSet.getInt("HQB");  
            result.HQS=resultSet.getInt("HQS");; 
            result.HQH=resultSet.getInt("HQH");  
            result.YZB=resultSet.getString("YZB"); 
            result.BTT=resultSet.getString("BTT"); 
            result.XK=resultSet.getString("XK"); 
            result.JZ=resultSet.getString("JZ"); 
            result.SZ=resultSet.getString("SZ"); 
            result.PZ=resultSet.getString("PZ");
            result.XZ=resultSet.getString("XZ"); 
            result.GP=resultSet.getString("GP"); 
            result.FEI=resultSet.getString("FEI"); 
            result.XT=resultSet.getString("XT"); 
            result.XHDB=resultSet.getInt("XHDB");   
            result.FHCL=resultSet.getString("FHCL"); 
            result.PFB=resultSet.getString("PFB"); 
            result.ZG=resultSet.getDouble("ZG");    
            result.ZGDJQ=resultSet.getString("ZGDJQ"); 
            result.ZGDJD=resultSet.getString("ZGDJD"); 
            result.ZGBJ=resultSet.getString("ZGBJ"); 
            result.JK=resultSet.getDouble("JK");    
            result.JKDJQ=resultSet.getString("JKDJQ"); 
            result.JKDJD=resultSet.getString("JKDJD"); 
            result.JKBJ=resultSet.getString("JKBJ"); 
            result.GPK=resultSet.getDouble("GPK");  
            result.GPKDJQ=resultSet.getString("GPKDJQ"); 
            result.GPKDJD=resultSet.getString("GPKDJD"); 
            result.GPKBJ=resultSet.getString("GPKBJ"); 
            result.BJL=resultSet.getDouble("BJL");  
            result.GSTJ=resultSet.getDouble("GSTJ");  
            result.JJX=resultSet.getDouble("JJX");
            result.BMKY=resultSet.getString("BMKY"); 
            result.SJSR=resultSet.getString("SJSR");
            result.WSM=resultSet.getDouble("WSM"); 
            result.WSMDJQ=resultSet.getString("WSMDJQ"); 
            result.WSMDJD=resultSet.getString("WSMDJD"); 
            result.WSMBJ=resultSet.getString("WSMBJ"); 
            result.LDTY=resultSet.getDouble("LDTY"); 
            result.LDTYDJQ=resultSet.getString("LDTYDJQ"); 
            result.LDTYDJD=resultSet.getString("LDTYDJD"); 
            result.LDTYBJ=resultSet.getString("LDTYBJ"); 
            result.LL=resultSet.getDouble("LL");  
            result.LLDJQ=resultSet.getString("LLDJQ"); 
            result.LLDJD=resultSet.getString("LLDJD"); 
            result.LLBJ=resultSet.getString("LLBJ");
            result.NNL=resultSet.getDouble("NNL");  
            result.NNLDJQ=resultSet.getString("NNLDJQ"); 
            result.NNLDJD=resultSet.getString("NNLDJD"); 
            result.NNLBJ=resultSet.getString("NNLBJ"); 
            result.ZWT=resultSet.getDouble("ZWT");      
            result.ZWTDJQ=resultSet.getString("ZWTDJQ"); 
            result.ZWTDJD=resultSet.getString("ZWTDJD"); 
            result.ZWTBJ=resultSet.getString("ZWTBJ");
            
            result.T1=resultSet.getString("T1"); 
            result.T2=resultSet.getString("T2");
            result.T3=resultSet.getString("T3"); 
            result.T4=resultSet.getString("T4"); 
            result.T5=resultSet.getString("T5"); 
            result.T6=resultSet.getString("T6"); 
            result.T7=resultSet.getString("T7"); 
            result.T8=resultSet.getString("T8"); 
            result.T9=resultSet.getString("T9"); 
            result.T10=resultSet.getString("T10"); 
            
            result.SLBH=resultSet.getString("SLBH"); 
            result.MBPJ=resultSet.getString("MBPJ"); 
            result.SSYPJ=resultSet.getString("SSYPJ"); 
            result.SZYPJ=resultSet.getString("SZYPJ"); 
            result.PXPJ=resultSet.getString("PXPJ"); 
            result.SLPJ=resultSet.getString("SLPJ"); 
            result.XJH=resultSet.getString("XJH"); 
            result.YYPJB=resultSet.getString("YYPJB"); 
            result.TJSY=resultSet.getInt("TJSY"); 
            result.MB1=resultSet.getInt("MB1"); 
            result.MB2=resultSet.getInt("MB2");
            result.MB3=resultSet.getInt("MB3");
            result.TJFS=resultSet.getInt("TJFS");
            result.TJPJ=resultSet.getString("TJPJ"); 
            
            result.YYPJH=resultSet.getString("YYPJH"); 
            result.XMPY=resultSet.getString("XMPY"); 
            result.HXP=resultSet.getDouble("HXP"); 
            result.HXPPJ=resultSet.getString("HXPPJ"); 
            result.EB=resultSet.getString("EB"); 
            result.YB=resultSet.getString("YB"); 
            result.TB=resultSet.getString("TB"); 
            result.JB=resultSet.getString("JB"); 
            
            result.PF=resultSet.getString("PF"); 
            result.JZX=resultSet.getString("JZX"); 
            result.LBJ=resultSet.getString("LBJ"); 
            result.JHJS=resultSet.getString("JHJS"); 
            result.GBZAM=resultSet.getDouble("GBZAM");  
            result.DHSA=resultSet.getDouble("DHSA");  
            result.DHSZ=resultSet.getDouble("DHSZ");  
            result.GG=resultSet.getString("GG"); 
            
            result.BMKT=resultSet.getString("BMKT"); 
            result.YGHKT=resultSet.getString("YGHKT"); 
            result.YGEKY=resultSet.getString("YGEKY"); 
            result.YGEKT=resultSet.getString("YGEKT"); 
            result.BLOOD=resultSet.getString("BLOOD"); 
            result.YW=resultSet.getDouble("YW"); 
            result.TW=resultSet.getDouble("TW"); 
            result.CXBH=resultSet.getString("CXBH"); 
            
            result.LJSL=resultSet.getDouble("LJSL"); 
            result.RJSL=resultSet.getDouble("RJSL"); 
            result.JMY=resultSet.getString("JMY"); 
            result.WGFB=resultSet.getString("WGFB"); 
            result.YYB=resultSet.getString("YYB"); 
            result.PEI=resultSet.getString("PEI"); 
            result.LSLS=resultSet.getDouble("LSLS");  
            result.RSLS=resultSet.getDouble("RSLS"); 
            
            result.LJSLS=resultSet.getDouble("LJSLS");  
            result.RJSLS=resultSet.getDouble("RJSLS"); 
            result.RTL=resultSet.getString("RTL"); 
            result.WSZQ=resultSet.getString("WSZQ"); 
            result.RS=resultSet.getString("RS"); 
            result.BXP=resultSet.getDouble("BXP"); 
            result.XXB=resultSet.getDouble("XXB"); 
            result.SFZH=resultSet.getString("SFZH"); 
            
             result.DH=resultSet.getString("DH"); 
            result.ADDRESS=resultSet.getString("ADDRESS"); 
            result.RXYFJZS=resultSet.getString("RXYFJZS"); 
            result.YMA=resultSet.getString("YMA"); 
            result.YMAJY=resultSet.getString("YMAJY"); 
            result.YMB=resultSet.getString("YMB"); 
            result.YMBJY=resultSet.getString("YMBJY"); 
            result.YMC=resultSet.getString("YMC");
            
             result.YMCJY=resultSet.getString("YMCJY"); 
            result.YMD=resultSet.getString("YMD"); 
            result.YMDJY=resultSet.getString("YMDJY"); 
            result.YME=resultSet.getString("YME"); 
            result.YMEJY=resultSet.getString("YMEJY"); 
            result.YMQT=resultSet.getString("YMQT"); 
            result.RXHJZS=resultSet.getString("RXHJZS"); 
            result.JWBS=resultSet.getString("JWBS");
            
            result.QCQFY=resultSet.getInt("QCQFY");
            result.STUDGUID=null; 
            result.SCHOOLID=null; 
            result.STUDNUM=null; 
            result.YMF=resultSet.getString("YMF"); 
            result.YMFJY=resultSet.getString("YMFJY"); 
            ImportResult(mysqlstatement,result);
        }
    }
    protected static void ExportReport_ZB_Grade(Connection firebirdconn,Connection mysqlconn) throws SQLException, ClassNotFoundException
    {
        Statement statement=firebirdconn.createStatement();
        Statement mysqlstatement=mysqlconn.createStatement();
        ResultSet resultSet=null;
        try{
            resultSet=statement.executeQuery("select * from TJ_JKHZB_GRADE where tjnd='20131'");
        }
        catch(SQLException e){ 
            throw e;
        }
        Report_ZB_Grade zbgrade=null;
        while(resultSet.next()){
            zbgrade=new Report_ZB_Grade();
             zbgrade.RID=resultSet.getString("RID");
            zbgrade.TJND=resultSet.getString("TJND");
            zbgrade.GRADEBH=resultSet.getString("GRADEBH");
            zbgrade.XB=resultSet.getString("XB");
            zbgrade.CITYID=resultSet.getString("CITYID");
            zbgrade.DISTRICTID=resultSet.getString("DISTRICTID");
            zbgrade.SCHOOLBH=resultSet.getString("SCHOOLBH");
            zbgrade.SCHOOLTYPE=resultSet.getString("SCHOOLTYPE");
            zbgrade.XS=resultSet.getString("XS");
            zbgrade.SJ=resultSet.getString("SJ");
            zbgrade.H1=resultSet.getString("H1");
            zbgrade.H1B=resultSet.getString("H1B");
            zbgrade.H2=resultSet.getString("H2");
            zbgrade.H2B=resultSet.getString("H2B");
            zbgrade.H3=resultSet.getString("H3");
            zbgrade.H3B=resultSet.getString("H3B");
            zbgrade.H4=resultSet.getString("H4");
            zbgrade.H4B=resultSet.getString("H4B");
             zbgrade.H5=resultSet.getString("H5");
            zbgrade.H5B=resultSet.getString("H5B");
            zbgrade.H6=resultSet.getString("H6");
            zbgrade.H6B=resultSet.getString("H6B");
            zbgrade.H7=resultSet.getString("H7");
            zbgrade.H7B=resultSet.getString("H7B");
            zbgrade.H8=resultSet.getString("H8");
            zbgrade.H8B=resultSet.getString("H8B");
            zbgrade.H9=resultSet.getString("H9");
            zbgrade.H9B=resultSet.getString("H9B");
            zbgrade.H10=resultSet.getString("H10");
            zbgrade.H10B=resultSet.getString("H10B");
            zbgrade.H11=resultSet.getString("H11");
            zbgrade.H11B=resultSet.getString("H11B");
            zbgrade.H12=resultSet.getString("H12");
            zbgrade.H12B=resultSet.getString("H12B");
            zbgrade.H13=resultSet.getString("H13");
            zbgrade.H13B=resultSet.getString("H13B");
            zbgrade.H14=resultSet.getString("H14");
            zbgrade.H14B=resultSet.getString("H14B");
            zbgrade.H15=resultSet.getString("H15");
            zbgrade.H15B=resultSet.getString("H15B");
            zbgrade.H16=resultSet.getString("H16");
            zbgrade.H16B=resultSet.getString("H16B");
            zbgrade.H17=resultSet.getString("H17");
            zbgrade.H17B=resultSet.getString("H17B");
            zbgrade.H18=resultSet.getString("H18");
            zbgrade.H18B=resultSet.getString("H18B");
            zbgrade.H19=resultSet.getString("H19");
            zbgrade.H19B=resultSet.getString("H19B");
            zbgrade.H20=resultSet.getString("H20");
            zbgrade.H20B=resultSet.getString("H20B");
           ImportReport_ZB_Grade(mysqlstatement,zbgrade);
        }
    }
     protected static void ExportReport_ZB_Age(Connection firebirdconn,Connection mysqlconn) throws SQLException, ClassNotFoundException
    {
        Statement statement=firebirdconn.createStatement();
        Statement mysqlstatement=mysqlconn.createStatement();
        ResultSet resultSet=null;
        try{
            resultSet=statement.executeQuery("select * from TJ_JKHZB_AGE where tjnd='20131'");
        }
        catch(SQLException e){
            throw e;
        }
        Report_ZB_Age zbgrade=null;
        while(resultSet.next())
        {
            zbgrade=new Report_ZB_Age();
             zbgrade.RID=resultSet.getString("RID");
            zbgrade.TJND=resultSet.getString("TJND");
            zbgrade.Age=resultSet.getString("Age");
            zbgrade.XB=resultSet.getString("XB");
            zbgrade.CITYID=resultSet.getString("CITYID");
            zbgrade.DISTRICTID=resultSet.getString("DISTRICTID");
            zbgrade.SCHOOLBH=resultSet.getString("SCHOOLBH");
            zbgrade.SCHOOLTYPE=resultSet.getString("SCHOOLTYPE");
            zbgrade.XS=resultSet.getString("XS");
            zbgrade.SJ=resultSet.getString("SJ");
            zbgrade.H1=resultSet.getString("H1");
            zbgrade.H1B=resultSet.getString("H1B");
            zbgrade.H2=resultSet.getString("H2");
            zbgrade.H2B=resultSet.getString("H2B");
            zbgrade.H3=resultSet.getString("H3");
            zbgrade.H3B=resultSet.getString("H3B");
            zbgrade.H4=resultSet.getString("H4");
            zbgrade.H4B=resultSet.getString("H4B");
             zbgrade.H5=resultSet.getString("H5");
            zbgrade.H5B=resultSet.getString("H5B");
            zbgrade.H6=resultSet.getString("H6");
            zbgrade.H6B=resultSet.getString("H6B");
            zbgrade.H7=resultSet.getString("H7");
            zbgrade.H7B=resultSet.getString("H7B");
            zbgrade.H8=resultSet.getString("H8");
            zbgrade.H8B=resultSet.getString("H8B");
            zbgrade.H9=resultSet.getString("H9");
            zbgrade.H9B=resultSet.getString("H9B");
            zbgrade.H10=resultSet.getString("H10");
            zbgrade.H10B=resultSet.getString("H10B");
            zbgrade.H11=resultSet.getString("H11");
            zbgrade.H11B=resultSet.getString("H11B");
            zbgrade.H12=resultSet.getString("H12");
            zbgrade.H12B=resultSet.getString("H12B");
            zbgrade.H13=resultSet.getString("H13");
            zbgrade.H13B=resultSet.getString("H13B");
            zbgrade.H14=resultSet.getString("H14");
            zbgrade.H14B=resultSet.getString("H14B");
            zbgrade.H15=resultSet.getString("H15");
            zbgrade.H15B=resultSet.getString("H15B");
            zbgrade.H16=resultSet.getString("H16");
            zbgrade.H16B=resultSet.getString("H16B");
            zbgrade.H17=resultSet.getString("H17");
            zbgrade.H17B=resultSet.getString("H17B");
            zbgrade.H18=resultSet.getString("H18");
            zbgrade.H18B=resultSet.getString("H18B");
            zbgrade.H19=resultSet.getString("H19");
            zbgrade.H19B=resultSet.getString("H19B");
            zbgrade.H20=resultSet.getString("H20");
            zbgrade.H20B=resultSet.getString("H20B");
           ImportReport_ZB_Age(mysqlstatement,zbgrade);
        }
    }
     protected static void ExportReport_TB_Grade(Connection firebirdconn,Connection mysqlconn) throws SQLException, ClassNotFoundException
    {
        Statement statement=firebirdconn.createStatement();
        Statement mysqlstatement=mysqlconn.createStatement();
        ResultSet resultSet=null;
        try{
            resultSet=statement.executeQuery("select * from TJ_JKXTB_GRADE where tjnd='20131'");
        }
        catch(SQLException e){ 
            throw e;
        }
        Report_TB_Grade zbgrade=null;
        while(resultSet.next())
        {
            zbgrade=new Report_TB_Grade();
             zbgrade.RID=resultSet.getString("RID");
            zbgrade.TJND=resultSet.getString("TJND");
            zbgrade.GradeBH=resultSet.getString("GRADEBH");
            zbgrade.XB=resultSet.getString("XB");
            zbgrade.CITYID=resultSet.getString("CITYID");
            zbgrade.DISTRICTID=resultSet.getString("DISTRICTID");
            zbgrade.SCHOOLBH=resultSet.getString("SCHOOLBH");
            zbgrade.SCHOOLTYPE=resultSet.getString("SCHOOLTYPE");
            zbgrade.XS=resultSet.getString("XS");
            zbgrade.SJ=resultSet.getString("SJ");
            zbgrade.H1=resultSet.getString("H1");
            zbgrade.H1B=resultSet.getString("H1B");
            zbgrade.H2=resultSet.getString("H2");
            zbgrade.H2B=resultSet.getString("H2B");
            zbgrade.H3=resultSet.getString("H3");
            zbgrade.H3B=resultSet.getString("H3B");
            zbgrade.H4=resultSet.getString("H4");
            zbgrade.H4B=resultSet.getString("H4B");
             zbgrade.H5=resultSet.getString("H5");
            zbgrade.H5B=resultSet.getString("H5B");
            zbgrade.H6=resultSet.getString("H6");
            zbgrade.H6B=resultSet.getString("H6B");
            zbgrade.H7=resultSet.getString("H7");
            zbgrade.H7B=resultSet.getString("H7B");
            zbgrade.H8=resultSet.getString("H8");
            zbgrade.H8B=resultSet.getString("H8B");
            zbgrade.H9=resultSet.getString("H9");
            zbgrade.H9B=resultSet.getString("H9B");
            zbgrade.H10=resultSet.getString("H10");
            zbgrade.H10B=resultSet.getString("H10B");
            zbgrade.H11=resultSet.getString("H11");
            zbgrade.H11B=resultSet.getString("H11B");
            zbgrade.H12=resultSet.getString("H12");
            zbgrade.H12B=resultSet.getString("H12B");
            zbgrade.H13=resultSet.getString("H13");
            zbgrade.H13B=resultSet.getString("H13B");
            zbgrade.H14=resultSet.getString("H14");
            zbgrade.H14B=resultSet.getString("H14B");
            zbgrade.H15=resultSet.getString("H15");
            zbgrade.H15B=resultSet.getString("H15B");
            zbgrade.H16=resultSet.getString("H16");
            zbgrade.H16B=resultSet.getString("H16B");
            zbgrade.H17=resultSet.getString("H17");
            zbgrade.H17B=resultSet.getString("H17B");
            zbgrade.H18=resultSet.getString("H18");
            zbgrade.H18B=resultSet.getString("H18B");
            zbgrade.H19=resultSet.getString("H19");
            zbgrade.H19B=resultSet.getString("H19B");
            zbgrade.H20=resultSet.getString("H20");
            zbgrade.H20B=resultSet.getString("H20B");
            ImportReport_TB_Grade(mysqlstatement,zbgrade);
        }
    }
    protected static void ExportReport_TB_Age(Connection firebirdconn,Connection mysqlconn) throws SQLException, ClassNotFoundException
    {
        Statement statement=firebirdconn.createStatement();
        Statement mysqlstatement=mysqlconn.createStatement();
        ResultSet resultSet=null; 
        try{
             resultSet=statement.executeQuery("select * from TJ_JKXTB_Age where tjnd='20131'");
        }
        catch(SQLException e){
            throw e;
        }
        Report_TB_Age zbgrade=null;
        while(resultSet.next()){
            zbgrade=new Report_TB_Age();
             zbgrade.RID=resultSet.getString("RID");
            zbgrade.TJND=resultSet.getString("TJND");
            zbgrade.Age=resultSet.getString("Age");
            zbgrade.XB=resultSet.getString("XB");
            zbgrade.CITYID=resultSet.getString("CITYID");
            zbgrade.DISTRICTID=resultSet.getString("DISTRICTID");
            zbgrade.SCHOOLBH=resultSet.getString("SCHOOLBH");
            zbgrade.SCHOOLTYPE=resultSet.getString("SCHOOLTYPE");
            zbgrade.XS=resultSet.getString("XS");
            zbgrade.SJ=resultSet.getString("SJ");
            zbgrade.H1=resultSet.getString("H1");
            zbgrade.H1B=resultSet.getString("H1B");
            zbgrade.H2=resultSet.getString("H2");
            zbgrade.H2B=resultSet.getString("H2B");
            zbgrade.H3=resultSet.getString("H3");
            zbgrade.H3B=resultSet.getString("H3B");
            zbgrade.H4=resultSet.getString("H4");
            zbgrade.H4B=resultSet.getString("H4B");
             zbgrade.H5=resultSet.getString("H5");
            zbgrade.H5B=resultSet.getString("H5B");
            zbgrade.H6=resultSet.getString("H6");
            zbgrade.H6B=resultSet.getString("H6B");
            zbgrade.H7=resultSet.getString("H7");
            zbgrade.H7B=resultSet.getString("H7B");
            zbgrade.H8=resultSet.getString("H8");
            zbgrade.H8B=resultSet.getString("H8B");
            zbgrade.H9=resultSet.getString("H9");
            zbgrade.H9B=resultSet.getString("H9B");
            zbgrade.H10=resultSet.getString("H10");
            zbgrade.H10B=resultSet.getString("H10B");
            zbgrade.H11=resultSet.getString("H11");
            zbgrade.H11B=resultSet.getString("H11B");
            zbgrade.H12=resultSet.getString("H12");
            zbgrade.H12B=resultSet.getString("H12B");
            zbgrade.H13=resultSet.getString("H13");
            zbgrade.H13B=resultSet.getString("H13B");
            zbgrade.H14=resultSet.getString("H14");
            zbgrade.H14B=resultSet.getString("H14B");
            zbgrade.H15=resultSet.getString("H15");
            zbgrade.H15B=resultSet.getString("H15B");
            zbgrade.H16=resultSet.getString("H16");
            zbgrade.H16B=resultSet.getString("H16B");
            zbgrade.H17=resultSet.getString("H17");
            zbgrade.H17B=resultSet.getString("H17B");
            zbgrade.H18=resultSet.getString("H18");
            zbgrade.H18B=resultSet.getString("H18B");
            zbgrade.H19=resultSet.getString("H19");
            zbgrade.H19B=resultSet.getString("H19B");
            zbgrade.H20=resultSet.getString("H20");
            zbgrade.H20B=resultSet.getString("H20B");
           ImportReport_TB_Age(mysqlstatement,zbgrade);
        }
    }
    protected static void ImportStudent(Statement statement,Student student) throws Exception
    {  
          //Statement statement=connection.createStatement();
            String dateString =student.RXSJ+ "-01-01";
            java.util.Date  enterDate= null;
            if(student.RXSJ!=null&&!"".equals(student.RXSJ)){
                try{
                 enterDate = stringToDate(dateString);
                }
                catch(Exception e){
                    throw e;
                }
           }
            String studentno=null;
            if(student.XH.length()==7){
                studentno=student.XH.substring(3,7);
            }
            String ClassLongNumber=null;
            if(student.SCHOOLBH.length()>7){
                ClassLongNumber=student.SCHOOLBH.substring(0,4)+"00!"+student.SCHOOLBH.substring(0,6)+"!"+student.SCHOOLBH+"!"+student.CLASSBH;
            }
            String sql="insert into Student(id,schoolno,gradeno,classno,classname,enterdate,studentcode,studentno,xh,name,sex,borndate,classlongnumber,nation,zzmm,sy,xx,sfz,address,fqxm,fqdw,mqxm,mqdw,xjh) select "+
                    "'"+java.util.UUID.randomUUID()+ "',"+
//                    SqlFiled(student.BH)+
                    SqlFiled(student.SCHOOLBH)+
                    SqlFiled(student.GRADEBH)+
                    SqlFiled(student.CLASSBH)+
                    SqlFiled(student.CLASSName)+
                    SqlFiled(enterDate)+
                    SqlFiled(student.XH)+
                    SqlFiled(studentno)+
                    SqlFiled(student.XJH)+
                    SqlFiled(student.XM)+
                    SqlFiled(student.XB)+
                    SqlFiled(student.CSRQ)+
                    SqlFiled(ClassLongNumber)+
                    SqlFiled(student.MZ)+
                    SqlFiled(student.ZZMM)+
                    SqlFiled(student.SY)+
                    SqlFiled(student.XX)+
                    SqlFiled(student.SFZ)+
                    SqlFiled(student.ADDRESS)+
                    SqlFiled(student.FQXM)+
                    SqlFiled(student.FQDW)+
                    SqlFiled(student.MQXM)+
                    SqlFiled(student.MQDW)+
                    (student.XJH==null?null:"'"+student.XJH+"'")+
                    " from dual  where not exists(select * from Student where schoolno='"+student.SCHOOLBH+ "' and studentcode='"+student.XH+"')";
           
            try{
                statement.execute(sql);
            }
            catch(SQLException e){
            }
            finally
            {
                importcount++;
            }
    }
    protected static void ImportResult(Statement statement,Result result) throws Exception
    {  
          //Statement statement=connection.createStatement();
            StringBuilder sql = new StringBuilder("insert into Result(");
            String dateString =result.RXSJ+ "-01-01";
            java.util.Date  enterDate= null;
            if(result.RXSJ!=null&&!"".equals(result.RXSJ)){
                try{
                 enterDate = stringToDate(dateString);
                }
                catch(Exception e){
                    throw e;
                }
           }
            String ClassLongNumber=null;
            if(result.SCHOOLBH.length()>7){
                ClassLongNumber=result.SCHOOLBH.substring(0,4)+"00!"+result.SCHOOLBH.substring(0,6)+"!"+result.SCHOOLBH+"!"+result.CLASSBH;
            }
            String SchoolType=null;
            if(result.CLASSBH.length()>1){
                String grodecode=result.CLASSBH.substring(0,1);
                Integer gradeCode = Integer.parseInt(grodecode);
                switch(gradeCode){
                    case 1:
                        SchoolType="小学";
                        break;
                    case 2:
                        SchoolType="初中";
                        break;
                    case 3:
                        SchoolType="高中";
                        break;
                }
            }
            //String sql;
            sql.append("id,");
            sql.append("TJND,");
            sql.append("Term,");
            sql.append("BH,");
            sql.append("SCHOOLBH ,");
            sql.append("GRADEBH ,");
            sql.append("CLASSBH ,");
            sql.append("RXSJ ,");
            sql.append("XH ,");
            sql.append("studentcode ,");
            sql.append("classlongno ,");
            sql.append("SchoolType,");
            sql.append("SCHOOL_NAME ,");
            sql.append("GRADE_NAME ,");
            sql.append("CLASS_NAME ,");
            sql.append("XM ,");
            sql.append("XB ,");
            sql.append("CSRQ ,");
            sql.append("MZ ,");
            sql.append("ZZMM ,");
            sql.append("SY ,");
            sql.append("TJRQ ,");
            sql.append("NL ,");
            sql.append("MARK ,");
            sql.append("SG ,");
            sql.append("SGDJQ ,");
            sql.append("SGDJD ,");
            sql.append("SGBJ ,");
            sql.append("TZ ,");
            sql.append("TZDJQ ,");
            sql.append("TZDJD ,");
            sql.append("TZBJ ,");
            sql.append("YYPJ ,");
            sql.append("YYPJX ,");
            sql.append("TXPJ ,");
            sql.append("FYPJ ,");
            sql.append("XW ,");
            sql.append("XWDJQ ,");
            sql.append("XWDJD ,");
            sql.append("XWBJ ,");
            sql.append("FHL ,");
            sql.append("FHLDJQ ,");
            sql.append("FHLDJD ,");
            sql.append("FHLBJ ,");
            sql.append("WL ,");
            sql.append("WLDJQ ,");
            sql.append("WLDJD ,");
            sql.append("WLBJ ,");
            sql.append("MB ,");
            sql.append("MBBJ ,");
            sql.append("SSY ,");
            sql.append("SSYBJ ,");
            sql.append("SZY ,");
            sql.append("SZYBJ ,");
            sql.append("LSL ,");
            sql.append("RSL ,");
            sql.append("QTYB ,");
            sql.append("LQG ,");
            sql.append("RQG ,");
            sql.append("LSY ,");
            sql.append("RSY ,");
            sql.append("BS ,");
            sql.append("TL ,");
            sql.append("XJ ,");
            sql.append("BB ,");
            sql.append("QSBNUM ,");
            sql.append("RQB ,");
            sql.append("RQS ,");
            sql.append("RQH ,");
            sql.append("HQB ,");
            sql.append("HQS ,");
            sql.append("HQH ,");
            sql.append("YZB ,");
            sql.append("BTT ,");
            sql.append("XK ,");
            sql.append("JZ ,");
            sql.append("SZ ,");
            sql.append("PZ ,");
            sql.append("XZ ,");
            sql.append("GP ,");
            sql.append("FEI ,");
            sql.append("XT ,");
            sql.append("XHDB ,");
            sql.append("FHCL ,");
            sql.append("PFB ,");
            sql.append("ZG ,");
            sql.append("ZGDJQ ,");
            sql.append("ZGDJD ,");
            sql.append("ZGBJ ,");
            sql.append("JK ,");
            sql.append("JKDJQ ,");
            sql.append("JKDJD ,");
            sql.append("JKBJ ,");
            sql.append("GPK ,");
            sql.append("GPKDJQ ,");
            sql.append("GPKDJD ,");
            sql.append("GPKBJ ,");
            sql.append("BJL ,");
            sql.append("GSTJ ,");
            sql.append("JJX ,");
            sql.append("BMKY ,");
            sql.append("SJSR ,");
            sql.append("WSM ,");
            sql.append("WSMDJQ ,");
            sql.append("WSMDJD ,");
            sql.append("WSMBJ ,");
            sql.append("LDTY ,");
            sql.append("LDTYDJQ ,");
            sql.append("LDTYDJD ,");
            sql.append("LDTYBJ ,");
            sql.append("LL ,");
            sql.append("LLDJQ ,");
            sql.append("LLDJD ,");
            sql.append("LLBJ ,");
            sql.append("NNL ,");
            sql.append("NNLDJQ ,");
            sql.append("NNLDJD ,");
            sql.append("NNLBJ ,");
            sql.append("ZWT ,");
            sql.append("ZWTDJQ ,");
            sql.append("ZWTDJD ,");
            sql.append("ZWTBJ ,");
            sql.append("T1Name ,");
            sql.append("T1 ,");
            sql.append("T2Name ,");
            sql.append("T2 ,");
            sql.append("T3Name ,");
            sql.append("T3 ,");
            sql.append("T4Name ,");
            sql.append("T4 ,");
            sql.append("T5Name ,");
            sql.append("T5 ,");
            sql.append("T6Name ,");
            sql.append("T6 ,");
            sql.append("T7Name ,");
            sql.append("T7 ,");
            sql.append("T8Name ,");
            sql.append("T8 ,");
            sql.append("T9Name ,");
            sql.append("T9 ,");
            sql.append("T10Name ,");
            sql.append("T10 ,");
            sql.append("SLBH ,");
            sql.append("MBPJ ,");
            sql.append("SSYPJ ,");
            sql.append("SZYPJ ,");
            sql.append("PXPJ ,");
            sql.append("SLPJ ,");
            sql.append("XJH ,");
            sql.append("YYPJB ,");
            sql.append("TJSY ,");
            sql.append("MB1 ,");
            sql.append("MB2 ,");
            sql.append("MB3 ,");
            sql.append("TJFS ,");
            sql.append("TJPJ ,");
            sql.append("YYPJH ,");
            sql.append("XMPY ,");
            sql.append("HXP ,");
            sql.append("HXPPJ ,");
            sql.append("EB ,");
            sql.append("YB ,");
            sql.append("TB ,");
            sql.append("JB ,");
            sql.append("PF ,");
            sql.append("JZX ,");
            sql.append("LBJ ,");
            sql.append("JHJS ,");
            sql.append("GBZAM ,");
            sql.append("DHSA ,");
            sql.append("DHSZ ,");
            sql.append("GG ,");
            sql.append("BMKT ,");
            sql.append("YGHKT ,");
            sql.append("YGEKY ,");
            sql.append("YGEKT ,");
            sql.append("BLOOD ,");
            sql.append("YW ,");
            sql.append("TW ,");
            sql.append("CXBH ,");
            sql.append("LJSL ,");
            sql.append("RJSL ,");
            sql.append("JMY ,");
            sql.append("WGFB ,");
            sql.append("YYB ,");
            sql.append("PEI ,");
            sql.append("LSLS ,");
            sql.append("RSLS ,");
            sql.append("LJSLS ,");
            sql.append("RJSLS ,");
            sql.append("RTL ,");
            sql.append("WSZQ ,");
            sql.append("RS ,");
            sql.append("BXP ,");
            sql.append("XXB ,");
            sql.append("SFZH ,");
            sql.append("DH ,");
            sql.append("ADDRESS ,");
            sql.append("RXYFJZS ,");
            sql.append("YMA ,");
            sql.append("YMAJY ,");
            sql.append("YMB ,");
            sql.append("YMBJY ,");
            sql.append("YMC ,");
            sql.append("YMCJY ,");
            sql.append("YMD ,");
            sql.append("YMDJY ,");
            sql.append("YME ,");
            sql.append("YMEJY ,");
            sql.append("YMQT ,");
            sql.append("RXHJZS ,");
            sql.append("JWBS ,");
            sql.append("QCQFY ,");
            sql.append("STUDGUID ,");
            sql.append("SCHOOLID ,");
            sql.append("STUDNUM ,");
            sql.append("YMF ,");
            sql.append("YMFJY,");
            sql.append("V1Name ,");
            sql.append("V1 ,");
            sql.append("V2Name ,");
            sql.append("V2 ,");
            sql.append("V3Name ,");
            sql.append("V3 ,");
            sql.append("V4Name ,");
            sql.append("V4 ,");
            sql.append("V5Name ,");
            sql.append("V5 ) select ");
       String tjnd="2000";
       String term="1";
       if(result.TJND.length()==5){
           tjnd=result.TJND.substring(0,4);
           term=result.TJND.substring(4,5);
       }
       sql.append("'").append(java.util.UUID.randomUUID()).append("',");
       sql.append(tjnd).append(",");
       sql.append(term).append(",");
       sql.append(SqlFiled(result.BH));
       sql.append(SqlFiled(result.SCHOOLBH));
       sql.append(SqlFiled(result.GRADEBH));
       sql.append(SqlFiled(result.CLASSBH));
       sql.append(SqlFiled(enterDate));
       sql.append(SqlFiled(result.XH));
       sql.append(SqlFiled(result.XH));
       sql.append(SqlFiled(ClassLongNumber));
       sql.append(SqlFiled(SchoolType));
       sql.append(SqlFiled(result.SCHOOL_NAME));
       sql.append(SqlFiled(result.GRADE_NAME));
       sql.append(SqlFiled(result.CLASS_NAME));
       sql.append(SqlFiled(result.XM));
       sql.append(SqlFiled(result.XB));
       sql.append(SqlFiled(result.CSRQ));
       sql.append(SqlFiled(result.MZ));
       sql.append(SqlFiled(result.ZZMM));
       sql.append(SqlFiled(result.SY));
       sql.append(SqlFiled(result.TJRQ));
       sql.append(SqlFiled(result.NL));
       sql.append(SqlFiled(result.MARK));
       sql.append(SqlFiled(result.SG));
       sql.append(SqlFiled(result.SGDJQ));
       sql.append(SqlFiled(result.SGDJD)); 
       sql.append(SqlFiled(result.SGBJ));
       sql.append(SqlFiled(result.TZ));
       sql.append(SqlFiled(result.TZDJQ));
       sql.append(SqlFiled(result.TZDJD));
       sql.append(SqlFiled(result.TZBJ));
       sql.append(SqlFiled(result.YYPJ));
       sql.append(SqlFiled(result.YYPJX));
       sql.append(SqlFiled(result.TXPJ));
       sql.append(SqlFiled(result.FYPJ));
       sql.append(SqlFiled(result.XW));
       sql.append(SqlFiled(result.XWDJQ));
       sql.append(SqlFiled(result.XWDJD));
       sql.append(SqlFiled(result.XWBJ));
       sql.append(SqlFiled(result.FHL));
       sql.append(SqlFiled(result.FHLDJQ));
       sql.append(SqlFiled(result.FHLDJD));
       sql.append(SqlFiled(result.FHLBJ));
       sql.append(SqlFiled(result.WL));
       sql.append(SqlFiled(result.WLDJQ));
       sql.append(SqlFiled(result.WLDJD));
       sql.append(SqlFiled(result.WLBJ));
       sql.append(SqlFiled(result.MB));
       sql.append(SqlFiled(result.MBBJ));
       sql.append(SqlFiled(result.SSY));
       sql.append(SqlFiled(result.SSYBJ));
       sql.append(SqlFiled(result.SZY));
       sql.append(SqlFiled(result.SZYBJ));
       sql.append(SqlFiled(result.LSL));
       sql.append(SqlFiled(result.RSL));
       sql.append(SqlFiled(result.QTYB));
       sql.append(SqlFiled(result.LQG));
       sql.append(SqlFiled(result.RQG));
       sql.append(SqlFiled(result.LSY));
       sql.append(SqlFiled(result.RSY));
       sql.append(SqlFiled(result.BS));
       sql.append(SqlFiled(result.TL));
       sql.append(SqlFiled(result.XJ));
       sql.append(SqlFiled(result.BB));
       sql.append(SqlFiled(result.QSBNUM)); 
       sql.append(SqlFiled(result.RQB));
       sql.append(SqlFiled(result.RQS));
       sql.append(SqlFiled(result.RQH));
       sql.append(SqlFiled(result.HQB));
       sql.append(SqlFiled(result.HQS));
       sql.append(SqlFiled(result.HQH));
       sql.append(SqlFiled(result.YZB));
       sql.append(SqlFiled(result.BTT));
       sql.append(SqlFiled(result.XK));
       sql.append(SqlFiled(result.JZ));
       sql.append(SqlFiled(result.SZ));
       sql.append(SqlFiled(result.PZ));
       sql.append(SqlFiled(result.XZ));
       sql.append(SqlFiled(result.GP));
       sql.append(SqlFiled(result.FEI));
       sql.append(SqlFiled(result.XT));
       sql.append(SqlFiled(result.XHDB));
       sql.append(SqlFiled(result.FHCL));
       sql.append(SqlFiled(result.PFB));
       sql.append(SqlFiled(result.ZG));
       sql.append(SqlFiled(result.ZGDJQ));
       sql.append(SqlFiled(result.ZGDJD));
       sql.append(SqlFiled(result.ZGBJ));
       sql.append(SqlFiled(result.JK)); 
       sql.append(SqlFiled(result.JKDJQ));
       sql.append(SqlFiled(result.JKDJD));
       sql.append(SqlFiled(result.JKBJ));
       sql.append(SqlFiled(result.GPK));
       sql.append(SqlFiled(result.GPKDJQ));
       sql.append(SqlFiled(result.GPKDJD));
       sql.append(SqlFiled(result.GPKBJ));
       sql.append(SqlFiled(result.BJL));
       sql.append(SqlFiled(result.GSTJ));
       sql.append(SqlFiled(result.JJX));
       sql.append(SqlFiled(result.BMKY));
       sql.append(SqlFiled(result.SJSR));
       sql.append(SqlFiled(result.WSM));
       sql.append(SqlFiled(result.WSMDJQ));
       sql.append(SqlFiled(result.WSMDJD));
       sql.append(SqlFiled(result.WSMBJ));
       sql.append(SqlFiled(result.LDTY));
       sql.append(SqlFiled(result.LDTYDJQ));
       sql.append(SqlFiled(result.LDTYDJD));
       sql.append(SqlFiled(result.LDTYBJ));
       sql.append(SqlFiled(result.LL));
       sql.append(SqlFiled(result.LLDJQ));
       sql.append(SqlFiled(result.LLDJD));
       sql.append(SqlFiled(result.LLBJ));
       sql.append(SqlFiled(result.NNL));
       sql.append(SqlFiled(result.NNLDJQ));
       sql.append(SqlFiled(result.NNLDJD));
       sql.append(SqlFiled(result.NNLBJ));
       sql.append(SqlFiled(result.ZWT));
       sql.append(SqlFiled(result.ZWTDJQ));
       sql.append(SqlFiled(result.ZWTDJD));
       sql.append(SqlFiled(result.ZWTBJ));
               
       sql.append(SqlFiled(result.T1Name));
       sql.append(SqlFiled(result.T1));
       sql.append(SqlFiled(result.T2Name)); 
       sql.append(SqlFiled(result.T2));
       sql.append(SqlFiled(result.T3Name));
       sql.append(SqlFiled(result.T3));
       sql.append(SqlFiled(result.T4Name));
       sql.append(SqlFiled(result.T4));
       sql.append(SqlFiled(result.T5Name));
       sql.append(SqlFiled(result.T5));
       sql.append(SqlFiled(result.T6Name));
       sql.append(SqlFiled(result.T6));
       sql.append(SqlFiled(result.T7Name));
       sql.append(SqlFiled(result.T7));
       sql.append(SqlFiled(result.T8Name));
       sql.append(SqlFiled(result.T8));
       sql.append(SqlFiled(result.T9Name));        
       sql.append(SqlFiled(result.T9));
       sql.append(SqlFiled(result.T10Name));            
       sql.append(SqlFiled(result.T10));
       
       sql.append(SqlFiled(result.SLBH));
       sql.append(SqlFiled(result.MBPJ));
       sql.append(SqlFiled(result.SSYPJ));
       sql.append(SqlFiled(result.SZYPJ));
       sql.append(SqlFiled(result.PXPJ));
       sql.append(SqlFiled(result.SLPJ));
       sql.append(SqlFiled(result.XJH));
       sql.append(SqlFiled(result.YYPJB));
       sql.append(SqlFiled(result.TJSY));
       sql.append(SqlFiled(result.MB1));
       sql.append(SqlFiled(result.MB2));
       sql.append(SqlFiled(result.MB3));
       sql.append(SqlFiled(result.TJFS));
       sql.append(SqlFiled(result.TJPJ));
       
       sql.append(SqlFiled(result.YYPJH));
       sql.append(SqlFiled(result.XMPY));
       sql.append(SqlFiled(result.HXP));
       sql.append(SqlFiled(result.HXPPJ));
       sql.append(SqlFiled(result.EB));
       sql.append(SqlFiled(result.YB));
       sql.append(SqlFiled(result.TB));
       sql.append(SqlFiled(result.JB));
       
       sql.append(SqlFiled(result.PF));
       sql.append(SqlFiled(result.JZX));
       sql.append(SqlFiled(result.LBJ));
       sql.append(SqlFiled(result.JHJS));
       sql.append(SqlFiled(result.GBZAM));
       sql.append(SqlFiled(result.DHSA));
       sql.append(SqlFiled(result.DHSZ));
       sql.append(SqlFiled(result.GG));
       
       sql.append(SqlFiled(result.BMKT));
       sql.append(SqlFiled(result.YGHKT));
       sql.append(SqlFiled(result.YGEKY));
       sql.append(SqlFiled(result.YGEKT));
       sql.append(SqlFiled(result.BLOOD));
       sql.append(SqlFiled(result.YW));
       sql.append(SqlFiled(result.TW));
       sql.append(SqlFiled(result.CXBH));
       
       sql.append(SqlFiled(result.LJSL));
       sql.append(SqlFiled(result.RJSL));
       sql.append(SqlFiled(result.JMY));
       sql.append(SqlFiled(result.WGFB));
       sql.append(SqlFiled(result.YYB));
       sql.append(SqlFiled(result.PEI));
       sql.append(SqlFiled(result.LSLS));
       sql.append(SqlFiled(result.RSLS));
       
       sql.append(SqlFiled(result.LJSLS));
       sql.append(SqlFiled(result.RJSLS));
       sql.append(SqlFiled(result.RTL));
       sql.append(SqlFiled(result.WSZQ));
       sql.append(SqlFiled(result.RS));
       sql.append(SqlFiled(result.BXP));
       sql.append(SqlFiled(result.XXB));
       sql.append(SqlFiled(result.SFZH));
       
       sql.append(SqlFiled(result.DH));
       sql.append(SqlFiled(result.ADDRESS));
       sql.append(SqlFiled(result.RXYFJZS));
       sql.append(SqlFiled(result.YMA));
       sql.append(SqlFiled(result.YMAJY));
       sql.append(SqlFiled(result.YMB));
       sql.append(SqlFiled(result.YMBJY));
       sql.append(SqlFiled(result.YMC));
       
       sql.append(SqlFiled(result.YMCJY));
       sql.append(SqlFiled(result.YMD));
       sql.append(SqlFiled(result.YMDJY));
       sql.append(SqlFiled(result.YME));
       sql.append(SqlFiled(result.YMEJY));
       sql.append(SqlFiled(result.YMQT));
       sql.append(SqlFiled(result.RXHJZS));
       sql.append(SqlFiled(result.JWBS));
       
       sql.append(SqlFiled(result.QCQFY));
       sql.append(SqlFiled(result.STUDGUID));
       sql.append(SqlFiled(result.SCHOOLID));
       sql.append(SqlFiled(result.STUDNUM));
       sql.append(SqlFiled(result.YMF));
       sql.append(SqlFiled(result.YMFJY));
               
       sql.append(SqlFiled(result.V1Name));
       sql.append(SqlFiled(result.V1));
       sql.append(SqlFiled(result.V2Name));
       sql.append(SqlFiled(result.V2));
       sql.append(SqlFiled(result.V3Name));
       sql.append(SqlFiled(result.V3));
       sql.append(SqlFiled(result.V4Name));
       sql.append(SqlFiled(result.V4));
       sql.append(SqlFiled(result.V5Name));
       sql.append(result.V5==null?result.V5:("'"+result.V5+"'"));
        sql.append(" FROM dual where not exists(select * from Result where tjnd=");
        sql.append(tjnd);
        sql.append(" and BH='");
        sql.append(result.BH);
        sql.append("')");
            try{
                statement.execute(sql.toString());
                
            }
            catch(SQLException e){
                sql.delete( 0, sql.length());
            }
            finally{
               sql.delete( 0, sql.length()); 
               importcount++;
            }
    }
    protected static void ImportReport_ZB_Grade(Statement statement,Report_ZB_Grade report) throws ClassNotFoundException, SQLException
    { 
          //Statement statement=connection.createStatement();
            String initsql="delete from Report_ZB_Grade where tjnd="+report.TJND.substring(0, 4)+" and schoolbh="+report.SCHOOLBH+ " and rid="+report.RID+" and grade='"+report.GRADEBH+"'";
            
        
            String sql= "insert into Report_ZB_Grade ("
            + "id,RID,TJND,Term,GRADE,XB,CityId,DISTRICTID,SCHOOLBH,SchoolType,XS,SJ,H1,H1B,H2,H2B,H3,H3B,H4,H4B,H5" +
            ",H5B,H6,H6B,H7,H7B,H8,H8B,H9,H9B,H10,H10B,H11,H11B,H12,H12B,H13,H13B,H14,H14B,H15,H15B,H16,H16B" +
            ",H17,H17B,H18,H18B,H19,H19B,H20,H20B) values("+
                        "'"+java.util.UUID.randomUUID()+"',"+
                        report.RID+","+
                        report.TJND.substring(0, 4)+","+
                        report.TJND.substring(4, 5)+","+
                        "'"+report.GRADEBH+"',"+
                        "'"+report.XB+"',"+
                        report.CITYID+","+
                        report.DISTRICTID+","+
                        report.SCHOOLBH+","+
                        "'"+report.SCHOOLTYPE+"',"+
                        report.XS+","+
                        report.SJ+","+
                        report.H1+","+
                        report.H1B+","+
                        report.H2+","+
                        report.H2B+","+
                        report.H3+","+
                        report.H3B+","+
                        report.H4+","+
                        report.H4B+","+
                         report.H5+","+
                        report.H5B+","+
                        report.H6+","+
                        report.H6B+","+
                        report.H7+","+
                        report.H7B+","+
                        report.H8+","+
                        report.H8B+","+
                        report.H9+","+
                        report.H9B+","+
                        report.H10+","+
                        report.H10B+","+
                        report.H11+","+
                        report.H11B+","+
                        report.H12+","+
                        report.H12B+","+
                        report.H13+","+
                        report.H13B+","+
                        report.H14+","+
                        report.H14B+","+
                        report.H15+","+
                        report.H15B+","+
                        report.H16+","+
                        report.H16B+","+
                        report.H17+","+
                        report.H17B+","+
                        report.H18+","+
                        report.H18B+","+
                        report.H19+","+
                        report.H19B+","+
                        report.H20+","+
                        report.H20B+
                     ")";
            try{
                statement.execute(initsql);
                statement.execute(sql);
            }
            catch(SQLException e){
                throw e;
            }
            finally{
               importcount++; 
            }
    }
    protected static void ImportReport_ZB_Age(Statement statement,Report_ZB_Age report)throws ClassNotFoundException, SQLException
     {
          //Statement statement=connection.createStatement();
           String initsql="delete from Report_ZB_Grade where tjnd="+report.TJND.substring(0, 4)+" and schoolbh="+report.SCHOOLBH+ " and rid="+report.RID+" and grade='"+report.Age+"'";
            String sql = "insert into Report_ZB_Age ("
                       + "id,RID,TJND,Term,Age,XB,CityId,DISTRICTID,SCHOOLBH,SchoolType,XS,SJ,H1,H1B,H2,H2B,H3,H3B,H4,H4B,H5" +
            ",H5B,H6,H6B,H7,H7B,H8,H8B,H9,H9B,H10,H10B,H11,H11B,H12,H12B,H13,H13B,H14,H14B,H15,H15B,H16,H16B" +
            ",H17,H17B,H18,H18B,H19,H19B,H20,H20B) values("+
                        "'"+java.util.UUID.randomUUID()+"',"+
                        report.RID+","+
                        report.TJND.substring(0, 4)+","+
                        report.TJND.substring(4, 5)+","+
                        "'"+report.Age+"',"+
                        "'"+report.XB+"',"+
                        report.CITYID+","+
                        report.DISTRICTID+","+
                        report.SCHOOLBH+","+
                        "'"+report.SCHOOLTYPE+"',"+
                        report.XS+","+
                        report.SJ+","+
                        report.H1+","+
                        report.H1B+","+
                        report.H2+","+
                        report.H2B+","+
                        report.H3+","+
                        report.H3B+","+
                        report.H4+","+
                        report.H4B+","+
                         report.H5+","+
                        report.H5B+","+
                        report.H6+","+
                        report.H6B+","+
                        report.H7+","+
                        report.H7B+","+
                        report.H8+","+
                        report.H8B+","+
                        report.H9+","+
                        report.H9B+","+
                        report.H10+","+
                        report.H10B+","+
                        report.H11+","+
                        report.H11B+","+
                        report.H12+","+
                        report.H12B+","+
                        report.H13+","+
                        report.H13B+","+
                        report.H14+","+
                        report.H14B+","+
                        report.H15+","+
                        report.H15B+","+
                        report.H16+","+
                        report.H16B+","+
                        report.H17+","+
                        report.H17B+","+
                        report.H18+","+
                        report.H18B+","+
                        report.H19+","+
                        report.H19B+","+
                        report.H20+","+
                        report.H20B+
                     ")";
            try{
                statement.execute(initsql);
                statement.execute(sql);
            }
            catch(SQLException e){
                throw e;
            }
            finally{
               importcount++; 
            }
    }
    protected static void ImportReport_TB_Grade(Statement statement,Report_TB_Grade report)throws ClassNotFoundException, SQLException
     {
            //Statement statement=connection.createStatement();
           String initsql="delete from Report_ZB_Grade where tjnd="+report.TJND.substring(0, 4)+" and schoolbh="+report.SCHOOLBH+ " and rid="+report.RID+" and grade='"+report.GradeBH+"'";
            String sql = "insert into Report_TB_Grade ("
                        + "id,RID,TJND,Term,GRADE,XB,CityId,DISTRICTID,SCHOOLBH,SchoolType,XS,SJ,H1,H1B,H2,H2B,H3,H3B,H4,H4B,H5" +
            ",H5B,H6,H6B,H7,H7B,H8,H8B,H9,H9B,H10,H10B,H11,H11B,H12,H12B,H13,H13B,H14,H14B,H15,H15B,H16,H16B" +
            ",H17,H17B,H18,H18B,H19,H19B,H20,H20B) values("+
                        "'"+java.util.UUID.randomUUID()+"',"+
                        report.RID+","+
                        report.TJND.substring(0, 4)+","+
                        report.TJND.substring(4, 5)+","+
                        "'"+report.GradeBH+"',"+
                        "'"+report.XB+"',"+
                        report.CITYID+","+
                        report.DISTRICTID+","+
                        report.SCHOOLBH+","+
                        "'"+report.SCHOOLTYPE+"',"+
                        report.XS+","+
                        report.SJ+","+
                        report.H1+","+
                        report.H1B+","+
                        report.H2+","+
                        report.H2B+","+
                        report.H3+","+
                        report.H3B+","+
                        report.H4+","+
                        report.H4B+","+
                         report.H5+","+
                        report.H5B+","+
                        report.H6+","+
                        report.H6B+","+
                        report.H7+","+
                        report.H7B+","+
                        report.H8+","+
                        report.H8B+","+
                        report.H9+","+
                        report.H9B+","+
                        report.H10+","+
                        report.H10B+","+
                        report.H11+","+
                        report.H11B+","+
                        report.H12+","+
                        report.H12B+","+
                        report.H13+","+
                        report.H13B+","+
                        report.H14+","+
                        report.H14B+","+
                        report.H15+","+
                        report.H15B+","+
                        report.H16+","+
                        report.H16B+","+
                        report.H17+","+
                        report.H17B+","+
                        report.H18+","+
                        report.H18B+","+
                        report.H19+","+
                        report.H19B+","+
                        report.H20+","+
                        report.H20B+
                     ")";
            try{
                statement.execute(initsql);
                statement.execute(sql);
            }
            catch(SQLException e){
                throw e;
            }
            finally{
               importcount++; 
            }
    }
     protected static void ImportReport_TB_Age(Statement statement,Report_TB_Age report)throws ClassNotFoundException, SQLException
     {
          //Statement statement=connection.createStatement();
            String initsql="delete from Report_ZB_Grade where tjnd="+report.TJND.substring(0, 4)+" and schoolbh="+report.SCHOOLBH+ " and rid="+report.RID+" and grade='"+report.Age+"'";
            String sql = "insert into Report_TB_Age("
                          + "id,RID,TJND,Term,Age,XB,CityId,DISTRICTID,SCHOOLBH,SchoolType,XS,SJ,H1,H1B,H2,H2B,H3,H3B,H4,H4B,H5" +
            ",H5B,H6,H6B,H7,H7B,H8,H8B,H9,H9B,H10,H10B,H11,H11B,H12,H12B,H13,H13B,H14,H14B,H15,H15B,H16,H16B" +
            ",H17,H17B,H18,H18B,H19,H19B,H20,H20B) values("+
                        "'"+java.util.UUID.randomUUID()+"',"+
                        report.RID+","+
                        report.TJND.substring(0, 4)+","+
                        report.TJND.substring(4, 5)+","+
                         "'"+report.Age+"',"+
                        "'"+report.XB+"',"+
                        report.CITYID+","+
                        report.DISTRICTID+","+
                        report.SCHOOLBH+","+
                        "'"+report.SCHOOLTYPE+"',"+
                        report.XS+","+
                        report.SJ+","+
                        report.H1+","+
                        report.H1B+","+
                        report.H2+","+
                        report.H2B+","+
                        report.H3+","+
                        report.H3B+","+
                        report.H4+","+
                        report.H4B+","+
                        report.H5+","+
                        report.H5B+","+
                        report.H6+","+
                        report.H6B+","+
                        report.H7+","+
                        report.H7B+","+
                        report.H8+","+
                        report.H8B+","+
                        report.H9+","+
                        report.H9B+","+
                        report.H10+","+
                        report.H10B+","+
                        report.H11+","+
                        report.H11B+","+
                        report.H12+","+
                        report.H12B+","+
                        report.H13+","+
                        report.H13B+","+
                        report.H14+","+
                        report.H14B+","+
                        report.H15+","+
                        report.H15B+","+
                        report.H16+","+
                        report.H16B+","+
                        report.H17+","+
                        report.H17B+","+
                        report.H18+","+
                        report.H18B+","+
                        report.H19+","+
                        report.H19B+","+
                        report.H20+","+
                        report.H20B+
                     ")";
            try{
                statement.execute(initsql);
                statement.execute(sql);
            }
            catch(SQLException e){
                throw e;
            }
            finally{
               importcount++; 
            }
    }
     protected static Connection GetFbConnection() throws ClassNotFoundException, SQLException{
         try{
              Class.forName("org.firebirdsql.jdbc.FBDriver");
              Properties prop = new Properties(); 
              prop.put("charSet", "GBK");
              prop.put("user", "SYSDBA");
              prop.put("password", "masterkey");
              String path = "jdbc:firebirdsql://localhost:3050/"+DataPath+"?lc_ctype=gb2312";
              Connection connection = DriverManager.getConnection(path,prop);
              return connection;
            }
            catch (ClassNotFoundException e) { 
                throw e;
            }
         	catch (SQLException e) {
         		throw e;
         	}
               
     }
     protected static Connection GetMysqlConnection() throws ClassNotFoundException, SQLException{
    	 DataSource ds = AppContext.getBean("dataSource", DataSource.class);
    	 return ds.getConnection();
//         try{
//             Class.forName("com.mysql.jdbc.Driver");  
//            Connection connection = DriverManager  
//                    .getConnection(  
//                            "jdbc:mysql://127.0.0.1:3306/stuhealth_client",  
//                            "root", "stuhealthdoor");  
//            return connection;
//         }
//         catch (ClassNotFoundException e) { 
//                throw e;
//            }
//         catch (SQLException e) {
//        	 throw e;
//         }
     }
     protected static void ExportTree(Connection fbconn,Connection mysqlconn) throws SQLException, ClassNotFoundException
     {
         Statement fbstatement=fbconn.createStatement();
         Statement mysqlstatement=mysqlconn.createStatement();
         String sql="select * from cust";
         ResultSet resultSet= fbstatement.executeQuery(sql);
         ArrayList<Tree> list=new  ArrayList<Tree>();
         Tree school;
         while(resultSet.next()){
             try{
             school=new Tree ();
             school.id=java.util.UUID.randomUUID().toString();
             school.name=resultSet.getString("DWMC");
             school.status="T";
             String js=resultSet.getString("JS");
             //市
             if("2".equals(js)){
                 school.code=resultSet.getString("DWBM")+"00";
                 school.level="1";
                 school.longnumber=resultSet.getString("DWBM")+"00";
                 school.type="10";
                 ImportTree(mysqlstatement,school);
             }
             //区
             else if("3".equals(js)){
                 school.code=resultSet.getString("DWBM");
                 school.level="2";
                 school.longnumber=school.code.substring(0,4)+"00"+"!"+school.code;
                 school.parentcode=school.code.substring(0,4)+"00";
                 school.type="20";
                 ImportTree(mysqlstatement,school);
             }
             //学校
              else if("4".equals(js)){
                 school.code=resultSet.getString("DWBM");
                 school.level="3";
                 school.longnumber=school.code.substring(0,4)+"00"+"!"+school.code.substring(0,6)+"!"+school.code;
                 school.parentcode=school.code.substring(0,6);
                 school.type="30";
                 ImportTree(mysqlstatement,school);
             }
              //班级
             else if("6".equals(js)){
                 school.code=resultSet.getString("DWBM");
                 school.level="0";
                 school.longnumber=school.code.substring(0,4)+"00"+"!"+school.code.substring(0,6)+"!"+school.code.substring(0,19)+"!"+ school.code.substring(19,23);
                 school.parentcode=school.code.substring(0,19);
                 school.type="40";
                 if(school.code != null && school.code.trim().length() > 4) {
                	 school.code = school.code.substring(school.code.length()-4);
                 }
                 ImportTree(mysqlstatement,school);
             }
             }
             catch(SQLException e){
             } 
         }
         
         
     }
     protected static void ImportTree(Statement statement,Tree tree) throws SQLException{
         String sql="insert into schooltree (id,code,level,longnumber,name,parentcode,status,type) select "
                 + "'"+tree.id+"',"
                 + "'"+tree.code+"',"
                 + "'"+tree.level+"',"
                 + "'"+tree.longnumber+"',"
                 + "'"+tree.name+"',"
                 + "'"+tree.parentcode+"',"
                   + "'"+tree.status+"',"
                 + "'"+tree.type+"' FROM dual where not exists(select * from  schooltree where longnumber='"+tree.longnumber+"')";
          try{
                statement.execute(sql);
            }
            catch(SQLException e){
            }
          finally{
               importcount++; 
            }
         
     }
     public static ArrayList<School>  GetSchool() throws SQLException, ClassNotFoundException{
         Connection fbconn=GetFbConnection();
         Statement fbstatement=fbconn.createStatement();
         String sql="select * from schoolinfo";
         ResultSet resultSet= fbstatement.executeQuery(sql);
         ArrayList<School> list=new  ArrayList();
         School school;
         while(resultSet.next()){
             school=new School();
             school.id=java.util.UUID.randomUUID().toString();
             school.schoolcode=resultSet.getString("schoolbh");
             school.name=resultSet.getString("schoolname");
             school.cityid=school.schoolcode.substring(0,4)+"00";
             school.districtcode=school.schoolcode.substring(0,6);
             school.districtname=resultSet.getString("schooldistrict");
             school.schooltype=resultSet.getString("schooltype");
             school.citytown=resultSet.getString("citytown");
             school.address=resultSet.getString("address");
             school.linkman=resultSet.getString("linkman");
             school.officephone=resultSet.getString("officetel");
             school.mobilephone=resultSet.getString("mobilephone");
             String n=resultSet.getString("xz");
             if(resultSet.getString("xz").startsWith("六三"))
                school.schoolsystem="63制";
             else if(resultSet.getString("xz").startsWith("五四"))
                 school.schoolsystem="54制";
             else
                  school.schoolsystem="53制";
             list.add(school);
         }
         return list;
     }
     public static void  ImportSchool(Connection mysqlconn, ArrayList<School> Schools) throws SQLException{
         Statement statement=mysqlconn.createStatement();
         School school;
          for(int i=0;i<Schools.size();i++){
              school=Schools.get(i);
              String sql="insert into hw_school(id,address,cityid,citytown,createdate,districtcode,email,linkman,mobilephone,name,officephone,outdate,permit,"
                      + "remark,schoolcharacter,schooltype,schoollcode,seccode,status,version,cityname,districtname,schoolcode,hostschoolcode,schoolsystem) select "+
                      SqlFiled(school.id)+
                      SqlFiled(school.address)+
                      SqlFiled(school.cityid)+
                      SqlFiled(school.citytown)+
                      SqlFiled(school.creatdate)+
                      SqlFiled(school.districtcode)+
                      SqlFiled(school.email)+
                      SqlFiled(school.linkman)+
                      SqlFiled(school.mobilephone)+
                      SqlFiled(school.name)+
                      SqlFiled(school.officephone)+
                      SqlFiled(school.outdate)+
//                      SqlFiled(school.permit)+
                      SqlFiled("0")+
                      SqlFiled(school.remark)+
                      SqlFiled(school.schoolCharacter)+
                      SqlFiled(school.schooltype)+
                      SqlFiled(school.schoollcode)+
                      SqlFiled(school.seccode)+
                      SqlFiled("T")+
                      SqlFiled("0")+
                      SqlFiled(school.cityname)+
                      SqlFiled(school.districtname)+
                      SqlFiled(school.schoolcode)+
                      SqlFiled(school.hostschoolcode)+
                      "'"+school.schoolsystem+"' FROM dual where not exists(select * from  hw_school where schoollcode='"+school.schoolcode+"')";
                        try{
                         statement.execute(sql);
                     }
                     catch(SQLException e){
                     }
                     finally{
                        importcount++; 
                    }
              
          }/**/
     }
    protected static String SqlFiled(String source){
        return source==null?source+",":("'"+source.toString()+"',");
    }
    protected static String SqlFiled(int source){
        return source==0?(null+","):Integer.toString(source)+",";
    }
    protected static String SqlFiled(double source){
        if(source==0)
           return null+",";
        else
            return Double.toString(source)+",";
    }
    protected static String SqlFiled(Date source){
        return source==null?source+",":("'"+source.toString()+"',");
    }
    protected static String SqlFiled(java.util.Date source){
        return source==null?source+",":("'"+source.toString().trim()+"',");
    } 
     protected static Date stringToDate(String dateString){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		try {  
                     java.util.Date date=bartDateFormat.parse(dateString); 
                     java.sql.Date  sqlDate  =  new java.sql.Date(date.getTime());
		     return sqlDate;
		}  
		catch (Exception ex) {  
		    System.out.println("字符串转换成日期失败");  
		}
		return null;
	}
}
