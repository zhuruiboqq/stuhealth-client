package importfirebird;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class FindFile {
    private String fileName = "";
    private String dir = "";
    private Matcher m = null;

    public FindFile(String sdir,String sfilename) throws IOException {
        String f = FindFile.class.getResource("findfile.properties").getFile();
        BufferedReader read = new BufferedReader(new FileReader(f));
        dir = sdir;
        fileName = sfilename;
        Pattern p = Pattern.compile(fileName);
        m = p.matcher("");
    }
    public static void findFiles(String baseDirName, String targetFileName, List<File> fileList) {
        /**
         * 算法简述： 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，
         * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。 队列不空，重复上述操作，队列为空，程序结束，返回结果。
         */
        String tempName = null;
        // 判断目录是否存在
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
        } else {
            String[] filelist = baseDir.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(baseDirName + "\\" + filelist[i]);
                // System.out.println(readfile.getName());
                if (!readfile.isDirectory()) {
                    tempName = readfile.getName();
                    if (wildcardMatch(targetFileName, tempName)) {
                        // 匹配成功，将文件名添加到结果集
                        fileList.add(readfile.getAbsoluteFile());
                    }
                } else if (readfile.isDirectory()) {
                    findFiles(baseDirName + "\\" + filelist[i], targetFileName, fileList);
                }
            }
        }
    }
    private static boolean wildcardMatch(String pattern, String str) {
          if(pattern.toLowerCase().equals(str.toLowerCase()))
              return true;
          else
              return false;
//        int patternLength = pattern.length();
//        int strLength = str.length();
//        int strIndex = 0;
//        char ch;
//        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
//            ch = pattern.charAt(patternIndex);
//            if (ch == '*') {
//                // 通配符星号*表示可以匹配任意多个字符
//                while (strIndex < strLength) {
//                    if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
//                        return true;
//                    }
//                    strIndex++;
//                }
//            } else if (ch == '?') {
//                // 通配符问号?表示匹配任意一个字符
//                strIndex++;
//                if (strIndex > strLength) {
//                    // 表示str中已经没有字符匹配?了。
//                    return false;
//                }
//            } else {
//                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
//                    return false;
//                }
//                strIndex++;
//            }
//        }
//        return (strIndex == strLength);
    }
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    public static String GetOldData() throws IOException{
                String result=null;
                String oldfile=null;
                File sourceFile=new File("C:/Program Files/济南圣力科技有限公司/广东省中小学生健康体检管理系统(体检机构网络版)/Data/HealthData.FDB");
                String CommDir="/Program Files";
                if(!sourceFile.exists()){
                    List files = new ArrayList();
                    for(char c = 'a';c<='z';c++){
                        File f = new File(c+":");
                        if(f.list()!=null){
                            files.add(f);
                        }
                    }
                    List<File> resultList = new ArrayList<File>();
                    for(int i=0;i<files.size();i++){
                        File file = new File(files.get(i)+CommDir);
                        if(file.exists()){
                             FindFile.findFiles(files.get(i)+CommDir, "HealthData.FDB", resultList);
                            if(resultList.size()>0)
                                oldfile=resultList.get(0).getAbsolutePath();
                            if(oldfile!=null&&result==null){
                               // FindFile.copyFile(new File(oldfile),new File("C:\\HealthData.FDB"));
                                //result= "C:\\HealthData.FDB";
                                result= oldfile;
                                return result;
                            }
                        }
                                
                    }
                    return result;
                }
                else{
                    //FindFile.copyFile(sourceFile,new File("C:\\HealthData.FDB"));
                    //result= "C:\\HealthData.FDB";
                    result=sourceFile.getAbsolutePath();
                    return result;
                } 
            }
}
