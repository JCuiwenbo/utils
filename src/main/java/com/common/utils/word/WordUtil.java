package com.common.utils.word;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @Author Cui
 * @Description 导出word工具类
 * @Date 2019/6/19 16:15
 **/
public class WordUtil {


    private static Configuration configuration = null;
    private static String templateFolder = null;

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        try {
            // 模板文件位置，自定义修改
            templateFolder = ResourceUtils.getURL("classpath:templete").getPath().replaceFirst("/", "");
            configuration.setDirectoryForTemplateLoading(new File(templateFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WordUtil() {
        throw new AssertionError();
    }

    public static void exportMillCertificateWord(String templeteName, String wordName, HttpServletResponse response, Map map, HttpServletRequest request) throws IOException {

        if (StringUtils.isEmpty(templeteName) || StringUtils.isEmpty(wordName)) {
            // 可加入项目全局异常捕获
            throw new RuntimeException(" existence parameter is empty !");
        }

        Template freemarkerTemplate = configuration.getTemplate(templeteName);
        File file = null;
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            // 调用工具类的createDoc方法生成Word文档
            file = createDoc(map, freemarkerTemplate);
            fin = new FileInputStream(file);

            String fileName = new String(wordName.getBytes("gb2312"), "ISO8859-1");
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".doc");

            out = response.getOutputStream();
            byte[] buffer = new byte[512];
            int bytesToRead = -1;
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (out != null) {
                out.close();
            }
            if (file != null) {
                file.delete();
            }
        }
    }

    private static File createDoc(Map<?, ?> dataMap, Template template) {
        String name = "test.doc";
        File f = new File(name);
        Template t = template;
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            t.process(dataMap, w);
            w.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }
}