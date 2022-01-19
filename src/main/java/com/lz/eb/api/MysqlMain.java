package com.lz.eb.api;


import com.lz.eb.api.mysql.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * 预发布环境回调接口地址：
 https://pre-callback.sinawallent.com/platform/callback
 https://pre-callback.sinawallent.com/platformweb/query
 */
public class MysqlMain {

    public static String package_name = "com.api.model.entity";
    public static String package_name_model = "com.chengyi.user.dao";

    public static String save_path = "/Users/zhy/logs";

    public static String bean_path = "";
    public static String mapper_path = "";
    public static String mapper_xml_path = "";
    public static String service_path = "";
    public static String service_impl_path = "";


    public static String bean_package = "";
    public static String dao_package = "";
    public static String service_package = "";
    public static String service_impl_package = "";

    public static String mysql_url = "jdbc:mysql://localhost:3306";

    public static String pre = "";

    public static String mysql_dbname = "lt_biz";
    public static String mysql_username = "ldd_biz";
    public static String mysql_password = "Hello1234";



    public static void main(String[] args) throws Exception {

        testInsert();
        //testUpdateEntity();
    }

    public static  void  initMember() throws Exception{

        String path = ResourceUtils.getURL("classpath:").getPath();


        System.out.println("========="+path);
        String []a =  path.split("mysqlmain");
        System.out.println(a[0]);
        bean_path = a[0] +"mysqlmain/src/main/java/com/lz/eb/api/entity/member";
        mapper_path = a[0] + "mysqlmain/src/main/java/com/lz/eb/api/dao/member";
        mapper_xml_path = a[0] + "mysqlmain/src/main/resources/mapper/member";
        service_path =  a[0] +"mysqlmain/src/main/java/com/lz/eb/api/service/member";
        service_impl_path = a[0] + "mysqlmain/src/main/java/com/lz/eb/api/service/member/impl";
        initDir(bean_path,mapper_path,mapper_xml_path,service_path,service_impl_path);
        bean_package = "com.lz.eb.api.entity.member";
        dao_package = "com.lz.eb.api.dao.member";
        service_package = "com.lz.eb.api.service.member";
        service_impl_package = "com.lz.eb.api.service.member.impl";
    }




    public static void testInsert() throws Exception {
        String path = ResourceUtils.getURL("classpath:").getPath();
        System.out.println(path);
        String dir = null;
        if (StringUtil.isNotBlank(path)) {
            dir = path.split("target")[0];
        }
        save_path = dir + "src/test/tmp";
        int bool = 1;
        if (bool == 1) {
            initMember();
        }
        System.out.println(save_path);

        List<TablesBean> list = new ArrayList<TablesBean>();

        list.add(new TablesBean("lt_member_raise_record"));

        List<TablesBean> list2 = new ArrayList<TablesBean>();
        Map<String, String> map = MysqlUtil2ShowCreateTable.getComments();

        for (int i = 0; i < list.size(); i++) {
            TablesBean obj = list.get(i);
            String tableName = list.get(i).getTableName();
            List<FieldBean> itemList = MysqlUtil2ShowCreateTable.readTableDetail(tableName);
            obj.setFieldList(itemList);
            obj.setComment(map.get(tableName));
            list2.add(obj);
        }

        for (int i = 0; i < list2.size(); i++) {
            MysqlUtilTable2Bean.printEntity(list2.get(i));
        }

   /*     for (int i = 0; i < list2.size(); i++) {
            MysqlUtilTable2Contoller.printController(list2.get(i));
        }*/

        for (int i = 0; i < list2.size(); i++) {
            MysqlUtilTable2Mapper.printDao(list2.get(i));
        }

        for (int i = 0; i < list2.size(); i++) {
            MysqlUtilTable2Service.printService(list2.get(i));
            MysqlUtilTable2Service.printServiceImpl(list2.get(i));
        }

        for (int i = 0; i < list2.size(); i++) {
            MysqlUtilTable2XML.printXMLForMap(list2.get(i));
        }
    }


    public static void testUpdateEntity() throws Exception {
        String path = ResourceUtils.getURL("classpath:").getPath();
        System.out.println(path);
        String dir = null;
        if (StringUtil.isNotBlank(path)) {
            dir = path.split("target")[0];
        }
        String old = dir + "src/test/tmp";
        save_path = dir + "src/test/tmp";
        System.out.println(save_path);

        int bool = 1;
        if (bool == 1) {
            initMember();
        }

        String entityPath = dir + "src/main/java";
        List<String> resultFileName = new ArrayList<>();
        File file = new File(entityPath);
        ergodic(file, resultFileName);

        Map<String, String> fileNameMap = new HashMap<>();
        for (String fileName : resultFileName) {
            System.out.println(fileName);
        }
        List<TablesBean> list = new ArrayList<TablesBean>();
        list.add(new TablesBean("lt_member_order"));
        list.add(new TablesBean("lt_member_goods"));
        list.add(new TablesBean("lt_member_order_detail"));
        Map<String, String> map = MysqlUtil2ShowCreateTable.getComments();
        for (int i = 0; i < list.size(); i++) {
            TablesBean obj = list.get(i);
            String tableName = list.get(i).getTableName();
            System.out.println("---------" + tableName);
            List<FieldBean> itemList = MysqlUtil2ShowCreateTable.readTableDetail(tableName);
            obj.setFieldList(itemList);
            obj.setComment(map.get(tableName));
            for (String fileName : resultFileName) {
                String className = fileName.replaceAll("\\.java", "");
                Class clazz = Class.forName(className);
                String annTableName = getAnnotationValueByTypeName(clazz, "TableName");
                if (StringUtil.isNotBlank(annTableName) && tableName.equals(annTableName)) {
                    System.out.println("annotationName is = " + annTableName);
                    String fileNames[] = className.split("\\.");
                    String xx = fileNames[fileNames.length - 1];
                    String paxx = className.replace("." + xx, "");
                    System.out.println(paxx);
                    package_name = paxx;
                    save_path = dir + "src/main/java/" + paxx.replaceAll("\\.", "/");
                    System.out.println("----------" + save_path);
                    break;
                } else {
                    save_path = old;
                }
            }
            MysqlUtilTable2Bean.printEntity(obj);
        }

    }

    public static <T> T getAnnotationValueByTypeName(Class type, String name) {
        Annotation[] annotations = type.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (name.equals(getAnnotationName(annotation))) {
                    return getAnnotationValue(annotation);
                }
            }
        }
        return null;
    }


    public static String getAnnotationName(Annotation annotation) {
        String annotionStr = annotation.toString();
        int a = annotionStr.indexOf("(", 0);
        if (a != -1) {
            annotionStr = annotionStr.substring(0, a);
            String strs[] = annotionStr.split("\\.");
            if (strs != null && strs.length > 0) {
                return strs[strs.length - 1];
            }
        }
        return annotionStr;
    }


    public static <T> T getAnnotationValue(Annotation annotation) {
        try {
            Method method = annotation.getClass().getMethod("value");
            if (method != null) {
                T paramName = (T) method.invoke(annotation);
                return paramName;
            }
        } catch (NoSuchMethodException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> ergodic(File file, List<String> resultFileName) {
        File[] files = file.listFiles();
        if (files == null) return resultFileName;// 判断目录下是不是空的
        for (File f : files) {
            if (f.isDirectory()) {// 判断是否文件夹
                ergodic(f, resultFileName);// 调用自身,查找子目录
            } else {
                String path = f.getPath();
                String paths[] = path.split("/src/main/java/");
                String className = paths[1].replaceAll("/", ".");
                resultFileName.add(className);
            }
        }
        return resultFileName;
    }


    public static  void initDir(String ... path ){
        for(String p : path){
            File file = new File(p);
            if(!file.exists()){
                file.mkdirs();
            }
        }
    }

}
