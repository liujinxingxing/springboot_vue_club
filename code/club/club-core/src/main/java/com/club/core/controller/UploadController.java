/**
 * @BelongsProject： base
 * @BelongsPackage： com.daysh.base.io.controller
 * @author 阳春白雪 | sample@gmail.com
 * @CreateTime： 2020-02-02 19:34
 *
 */
package com.club.core.controller;

import com.club.base.constant.ErrorCode;
import com.club.base.constant.StringPool;
import com.club.base.exception.FrameworkException;
import com.club.base.helper.ResultHelper;
import com.club.base.util.StringUtil;
import com.club.core.util.ExcelUtil;
import com.club.framework.spring.controller.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基本文件处理类 通用的文件处理
 *
 * @author 阳春白雪 | sample@gmail.com
 * @action 文件处理类
 * @date 2022年2月20日
 * @since 1.0
 */
@RestController
@RequestMapping("io")
public class UploadController extends BaseController {

    @Value("${base.upload.folder}")
    private String folder;
    @Value("${base.upload.path:}")
    private String path;

    private static final SimpleDateFormat DF = new SimpleDateFormat(StringPool.PATTERN_DATE2);

    /**
     * 上传文件 返回访问路径
     *
     * @param file |String|文件
     * @return String|相对路径，返回访问路径
     * @summary 上传文件
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("upload")
    public Object uploadFile(@RequestParam(value = "file") MultipartFile file) {
        String ret = null;
        try {
            String prefix = path;
            File dist = new File(dist());
            if (StringUtil.isEmpty(prefix)) {
                String contextPath = request.getContextPath();
                prefix = "/" + dist.getName();
                if (StringUtil.isNotEmpty(contextPath)) {
                    if(contextPath.startsWith("/")){
                        prefix = String.format("%s/%s", contextPath, dist.getName());
                    }else{
                        prefix = String.format("/%s/%s", contextPath, dist.getName());
                    }
                }
            }
            Date date = new Date();
            String type = getFileType(file);
            String name = String.format("%s/%s", DF.format(date), date.getTime());
            if (StringUtil.isNotEmpty(type)) {
                name = String.format("%s.%s", name, type);
            }
            File resource = new File(dist, name);
            File parent = resource.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (parent.exists()) {
                file.transferTo(resource);
                return ResultHelper.success(String.format("%s/%s", prefix, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultHelper.fail(ErrorCode.NO_FOUND_FAIL);
    }

    public static String getFileType(MultipartFile file) {
        String fileName = HtmlUtils.htmlEscape(file.getOriginalFilename());
        if (StringUtil.isNotEmpty(fileName)) {
            return fileName.substring(fileName.lastIndexOf(StringPool.DOT) + 1);
        }
        return null;
    }


    /**
     * 导入的二维数据 ，详细描述 返回 MAP 数组 数据结构 [{"row0":"","row1":""}]
     *
     * @param file |string|excel文件
     * @return java.lang.Object|map 数据 [{"row0":"","row1":""}]
     * @summary 导入的二维数据
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("import")
    public Object importFile(@RequestParam(value = "file") MultipartFile file) {
        return ResultHelper.success(importData(file));
    }

    /**
     * 下载位置路径的文件
     *
     * @param path |String|文件路径
     * @return void|文件流
     * @summary 下载位置路径的文件
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("download")
    public Object download(@RequestParam(value = "path") String path) {
        File file = new File(dist(), path);
        if (file.exists() && file.isFile()) {
            response.setHeader(StringPool.CONTENT_DISPOSITION, String.format(StringPool.DOWNLOAD_HEAND, file.getName()));
            OutputStream outputStream = null;
            FileInputStream fileInputStream = null;
            try {
                outputStream = response.getOutputStream();
                fileInputStream = new FileInputStream(file);
                if (file.exists()) {
                    IOUtils.copy(fileInputStream, outputStream);
                    outputStream.flush();
                }
            } catch (IOException e) {
                log.error("下载出错路径为:{}", file.getAbsolutePath(), e);
                return ResultHelper.fail(ErrorCode.PARAM_ILLEGAL_FAIL);
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(fileInputStream);
            }
        }
        return ResultHelper.fail(ErrorCode.PARAM_ILLEGAL_FAIL);
    }

    /**
     * 下载jar包里面template的,模板文件文件
     *
     * @param name |String|模板文件的名称
     * @return void|文件流
     * @summary 下载模板的文件
     * @error 错误描述
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("template")
    public Object downLoadTemplate(@RequestParam(value = "name") String name) {
        log.info("开始下载模版,模板名称为:【{}】", name);
        InputStream inputStream = null;
        try {
            String fileName = URLEncoder.encode(name, StringPool.DEFAULT_ENCODING);
            inputStream = this.getClass().getResourceAsStream(String.format(StringPool.TEMPLATE_INDEX, name));
            if (inputStream == null) {
                return ResultHelper.fail(ErrorCode.PARAM_ILLEGAL_FAIL);
            }
            response.setHeader(StringPool.CONTENT_DISPOSITION, String.format(StringPool.DOWNLOAD_HEAND, fileName));
            IOUtils.copyLarge(inputStream, response.getOutputStream(), new byte[1024]);
        } catch (Exception e) {
            log.error("下载模版失败:【{}】{}", name, e);
            return ResultHelper.fail(ErrorCode.PARAM_ILLEGAL_FAIL);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    public String dist() {
        if (StringUtil.isNotEmpty(folder)) {
            return folder;
        }
        return String.format("%s/static", System.getProperty("user.dir"));
    }

    public List<Map<String, String>> importData(MultipartFile file) {
        List<Map<String, String>> ret = new ArrayList<>();
        String rowName = "row";
        try {
            List<List<Object>> resultSet = ExcelUtil.getBankListByExcel(file.getInputStream(), file.getOriginalFilename());
            for (List<Object> objects : resultSet) {
                if (CollectionUtils.isNotEmpty(objects)) {
                    Map<String, String> map = new TreeMap<>();
                    int index = 0;
                    for (Object object : objects) {
                        map.put(String.format("%s%s", rowName, index++), String.valueOf(object));
                    }
                    ret.add(map);
                }
            }
        } catch (Exception e) {
            log.error("处理excel出错{}", e);
            throw new FrameworkException(ErrorCode.NO_FOUND_FAIL);
        }
        return ret;
    }

}
