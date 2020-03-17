package com.uboxol.cloud.pandorasBox.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeansException;

import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.google.common.collect.Lists;
import com.uboxol.cloud.pandorasBox.api.req.RiderPutMeal;
import com.uboxol.cloud.pandorasBox.cfg.MailConfig;
import com.uboxol.cloud.pandorasBox.service.ThreadService;
import com.uboxol.cloud.util.BeanUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class HyfUtils {
	/**
     * 对象属性拷贝
     * 将源对象的属性拷贝到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        try {
            BeanUtils.copyProperties(source, target);
        } catch (BeansException e) {
        	logger.error("BeanUtil property copy  failed :BeansException", e);
        } catch (Exception e) {
        	logger.error("BeanUtil property copy failed:Exception", e);
        }
    }
    
    /**
     * @param input 输入集合
     * @param clzz  输出集合类型
     * @param <E>   输入集合类型
     * @param <T>   输出集合类型
     * @return 返回集合
     */
    public static <E, T> List<T> convertList2List(List<E> input, Class<T> clzz) {
	    List<T> output = Lists.newArrayList();
	    if (CollectionUtils.isNotEmpty(input)) {
	        for (E source : input) {
	            T target = BeanUtils.instantiate(clzz);
	            BeanUtils.copyProperties(source, target);
	            output.add(target);
	        }
	    }
	    return output;
	}
    
    public static String getMonthFirstday(String str) {
    	String firstday = "";
    	try {
	    	SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
			//String str = "2019-02-05";
			Date date = sm.parse(str);
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			cale.add(Calendar.MONTH, 0);
			cale.set(Calendar.DAY_OF_MONTH, 1);
			firstday = sm.format(cale.getTime());
    	} catch (ParseException e) {
			e.printStackTrace();
		}
		return firstday;
    }
    
    public static String getMonthLastday(String str) {
    	String lastday="";
    	try {
	    	SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
			//String str = "2019-02-05";
			Date date = sm.parse(str);
			Calendar cale = Calendar.getInstance();
	    	// 获取前月的最后一天
			cale = Calendar.getInstance();
			cale.setTime(date);
			cale.add(Calendar.MONTH, 1);
			cale.set(Calendar.DAY_OF_MONTH, 0);
			lastday = sm.format(cale.getTime());
    	} catch (ParseException e) {
			e.printStackTrace();
		}
		return lastday;
    }
    
    public static String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(formatter);
    }
    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
    public static String getFormatTime() {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
	    Calendar c = Calendar.getInstance();
        return sf.format(c.getTime());
    }
    
	/*
	 * public static <E> String exportXlsx(List<CustomerComplaint> list) { String
	 * path = ""; try (DefaultStreamExcelBuilder builder =
	 * DefaultStreamExcelBuilder.of(CustomerComplaint.class).threadPool(
	 * ThreadService.EXCEL_EXECUTOR).capacity(60_000).start()) {
	 * List<CompletableFuture> futures = new ArrayList<>(); CompletableFuture future
	 * = CompletableFuture.runAsync(() -> {builder.append(list);});
	 * futures.add(future); futures.forEach(CompletableFuture::join); Workbook
	 * workbook = builder.build(); path = export(workbook,
	 * "客诉列表"+TimeUtils.getToday()); } catch (IOException e) { e.printStackTrace();
	 * } return path; }
	 */
    
    public static <E> String orderDetailExportXlsx(List<RiderPutMeal> list) {
    	String path = "";
	    try (DefaultStreamExcelBuilder builder = DefaultStreamExcelBuilder.of(RiderPutMeal.class).threadPool(ThreadService.EXCEL_EXECUTOR).capacity(60_000).start()) {
	        List<CompletableFuture> futures = new ArrayList<>();
	        CompletableFuture future = CompletableFuture.runAsync(() -> {builder.append(list);});
	        futures.add(future);
	        futures.forEach(CompletableFuture::join);
	        Workbook workbook = builder.build();
	        path = export(workbook, "客诉详情页文件"+TimeUtils.getToday());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return path;
    }
    
    private static String export(Workbook workbook, String fileName) {
    	String path = "";
        try {
            String suffix = ".xlsx";
            if (workbook instanceof HSSFWorkbook) {
                if (fileName.endsWith(suffix)) {
                    fileName = fileName.substring(0, fileName.length() - 1);
                }

                suffix = ".xls";
            }

            if (!fileName.endsWith(suffix)) {
                fileName = fileName + suffix;
            }

            File file = new File(MailConfig.fileUrl +File.separator+ fileName);

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }
            path = file.getAbsolutePath();

        } catch (IOException var11) {
            throw new RuntimeException(var11);
        } finally {
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }

            try {
                workbook.close();
            } catch (IOException var10) {
                var10.printStackTrace();
            }

        }
		return path;
    }
    
    /**
     *list去重
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<String>(list);   
        list.clear();   
        list.addAll(h);   
        return list;   
    }
    
    /**
     *list逗号分隔转成字符串
     */
    public static String listToString(List<String> list) {
    	String uids = "";
    	for(int i=0;i<list.size();i++) {
			if(i==list.size()-1) {
				uids += list.get(i);
			}else {
				uids += list.get(i)+",";
			}
		}
        return uids;   
    }
    
    /**
     *去除list中的null 值
     */
    public static <T> List<T> removeNull(List<? extends T> oldList) {

        // 你没有看错，真的是有 1 行代码就实现了
        oldList.removeAll(Collections.singleton(null)); 
        return (List<T>) oldList;  
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void main(String[] args) {
    	System.out.println(getCurrentDate());
    	System.out.println(getMonthFirstday("2019-11-25"));
    	System.out.println(getMonthLastday("2019-11-25"));
    	List<String> ulist = new ArrayList<String>();
//    	ulist.add("123");
//    	ulist.add("1234");
    	ulist.add(null);
    	ulist.add(null);
    	System.out.println(HyfUtils.listToString(ulist));
    	ulist.removeAll(Collections.singleton(null));
    	System.out.println(ulist+""+ulist.size());
    	
    	//System.out.println(HyfUtils.removeDuplicate(ulist));
    	System.out.println(HyfUtils.listToString(HyfUtils.removeDuplicate(ulist)));
    	
    	HashSet<String> hs = new HashSet<String>(ulist); //此时已经去掉重复的数据保存在hashset中
    	System.out.println(hs);
    	
    	
    	List<Integer> ccStatus = new ArrayList<Integer>();
    	ccStatus.add(9);
    	ccStatus.add(1);
    	if(!ccStatus.contains(1) && ccStatus.contains(9)) {
    		System.out.println("9");
		}else {
			System.out.println("1");
		}
    	
    	 
	}

}
