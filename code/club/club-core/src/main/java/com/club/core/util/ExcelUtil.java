package com.club.core.util;

import com.club.base.exception.FrameworkException;
import com.club.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * execl导出工具类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
public class ExcelUtil {

	private static final String EXCEL_2003 = ".xls";
	private static final String EXCEL_2007 = ".xlsx";

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return dd
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){
        if(wb == null){
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
//        style.setAlignment();
        HSSFCell cell = null;
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

	/**
	 * 功能描述: 设置相应头
	 * @param request
	 * @param response
	 * @param name
	 * void - 返回值类型
	 * @author 阳春白雪 | sample@gmail.com
	 * @date 2022年2月20日
	 */
	public static void resolveDownloadFileNameEncode(HttpServletRequest request, HttpServletResponse response, String name) {
		String fileName = String.format("%s_%s.%s", name, System.currentTimeMillis(),"xls");
		String userAgent = request.getHeader("User-Agent");
		try {
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				fileName = java.net.URLEncoder.encode(fileName, CharEncoding.UTF_8);
			} else {// 非IE浏览器的处理：
				fileName = new String(fileName.getBytes(CharEncoding.UTF_8), CharEncoding.ISO_8859_1);
			}
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/octet-stream;charset=UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FrameworkException(32,"导出数据错误");
		}
	}

	/**
	 * 功能描述: 导出数据
	 * @param request
	 * @param response
	 * @param name
	 * @param title
	 * @param values
	 * @return boolean - 返回值类型
	 * @author 阳春白雪 | sample@gmail.com
	 * @date 2022年2月20日
	 */
	public static boolean exportData(HttpServletRequest request, HttpServletResponse response, String name,String []title,String [][]values){
		try {
			resolveDownloadFileNameEncode(request, response, name);
			OutputStream os = response.getOutputStream();
			HSSFWorkbook wb = getHSSFWorkbook(name, title, values, null);
			wb.write(os);
			os.flush();
			os.close();
			return true;
		} catch (IOException e) {
			log.error("导出数据错误", e);
			throw new FrameworkException(32,"导出数据错误");
		}
	}

	public static List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
		return getBankListByExcel(in, fileName, 1, false);
	}

	public static List<List<String>> getDataListByExcel(InputStream in, String fileName, int startRows, boolean readEmptyCell) throws Exception {
		Workbook work = getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		} else {
			Sheet sheet = null;
			Row row = null;
			List<List<String>> lists = new ArrayList();
			List<String> listKey = new ArrayList();
			List<String> listValue = new ArrayList();

			for(int i = 0; i < work.getNumberOfSheets(); ++i) {
				sheet = work.getSheetAt(i);
				if (sheet != null) {
					for(int j = 2; j <= sheet.getLastRowNum(); ++j) {
						row = sheet.getRow(j);
						if (row != null && j >= startRows) {
							Cell cell0 = row.getCell(0);
							Cell cell1 = row.getCell(1);
							if (cell0 != null && cell1 != null) {
								cell0.setCellType(CellType.STRING);
								cell1.setCellType(CellType.STRING);
								if ((!StringUtil.isBlank(cell0.getStringCellValue()) || readEmptyCell) && (!StringUtil.isBlank(cell1.getStringCellValue()) || readEmptyCell)) {
									listKey.add(String.valueOf(getCellValue(cell0)));
									listValue.add(String.valueOf(getCellValue(cell1)));
								}
							}
						}
					}
				}
			}

			lists.add(listKey);
			lists.add(listValue);
			work.close();
			return lists;
		}
	}

	public static List<List<Object>> getBankListByExcel(InputStream in, String fileName, int startRows, boolean readEmptyCell) throws Exception {
		List<List<Object>> list = null;
		Workbook work = getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		} else {
			Sheet sheet = null;
			Row row = null;
			Cell cell = null;
			list = new ArrayList();

			for(int i = 0; i < work.getNumberOfSheets(); ++i) {
				sheet = work.getSheetAt(i);
				if (sheet != null) {
					for(int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); ++j) {
						row = sheet.getRow(j);
						if (row != null && j >= startRows && (row.getCell(0) != null || readEmptyCell)) {
							List<Object> li = new ArrayList();
							int firstReadCellNum = row.getFirstCellNum();
							if (readEmptyCell) {
								firstReadCellNum = 0;
							}

							for(int y = firstReadCellNum; y < row.getLastCellNum(); ++y) {
								cell = row.getCell(y);
								li.add(getCellValue(cell));
							}

							list.add(li);
						}
					}
				}
			}
			work.close();
			return list;
		}
	}

	public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (".xls".equals(fileType)) {
			wb = new HSSFWorkbook(inStr);
		} else {
			if (!".xlsx".equals(fileType)) {
				throw new Exception("解析的文件格式有误！");
			}

			wb = new XSSFWorkbook(inStr);
		}

		return (Workbook)wb;
	}

	public static Object getCellValue(Cell cell) {
		Object value = null;
		DecimalFormat df = new DecimalFormat("0");
		if (cell != null) {
			switch(cell.getCellType()) {
				case 0:
					value = df.format(cell.getNumericCellValue());
					break;
				case 1:
					value = cell.getRichStringCellValue().getString();
				case 2:
				default:
					break;
				case 3:
					value = "";
			}

			return value;
		} else {
			return "";
		}
	}
}
