package com.uboxol.cloud.pandorasBox.service;

import com.github.liaochong.myexcel.core.DefaultExcelBuilder;
import com.github.liaochong.myexcel.core.DefaultStreamExcelBuilder;
import com.github.liaochong.myexcel.core.annotation.ExcelColumn;
import com.github.liaochong.myexcel.utils.FileExportUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/11/5 14:25
 */
@Slf4j
public class ExcelUtilTest {

    @Getter
    @Setter
    public static class Row {
        @ExcelColumn(title = "名字")
        String name;
        @ExcelColumn(title = "年龄")
        String age;
        @ExcelColumn
        String sex;
        @ExcelColumn
        String fav;
    }

    @Test
    public void test流式导出() {
//        使用流式导出分为三步：

//        1.导出配置

//        DefaultStreamExcelBuilder.getInstance().threadPool(Executors.newFixedThreadPool(10));

//        DefaultStreamExcelBuilder streamExcelBuilder = DefaultStreamExcelBuilder
//            .of(ArtCrowd.class) // 如导出Map类型数据，请使用getInstance()
//            .threadPool(Executors.newFixedThreadPool(10))// 线程池，可选
//            .capacity(10_000)// 容量设定，在主动划分excel使用，可选
//            .start();

//        2.数据追加
//        append参数可为列表，也可为单个数据，建议使用单个数据追加，如Bean、Map

//        streamExcelBuilder.append(data);
//        3.完成构建
//        Workbook workbook = streamExcelBuilder.build();
//        默认情况下，DefaultStreamExcelBuilder关闭所有样式，以最大化提升导出性能，如需样式，请调用hasStyle()方法开启.
//
//            DefaultStreamExcelBuilder默认采用SXSSF模式（内存占用低）导出，该模式下不支持自动列宽.


//        try (DefaultStreamExcelBuilder streamExcelBuilder = DefaultStreamExcelBuilder
//            .of(ArtCrowd.class)
//            .threadPool(Executors.newFixedThreadPool(10))
//            .start()) {
//            // 多线程异步获取数据并追加至excel，join等待线程执行完成
//            List<CompletableFuture> futures = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                CompletableFuture future = CompletableFuture.runAsync(() -> {
//                    List<ArtCrowd> dataList = this.getDataList();
//                    // 数据追加
//                    defaultExcelBuilder.append(dataList);
//                });
//                futures.add(future);
//            }
//            futures.forEach(CompletableFuture::join);
//            // 最终构建
//            Workbook workbook = defaultExcelBuilder.build();
//            AttachmentExportUtil.export(workbook, "艺术生信息", response);
//        }

        try (DefaultStreamExcelBuilder builder = DefaultStreamExcelBuilder.of(Row.class).threadPool(ThreadService.EXCEL_EXECUTOR).capacity(60_000).start()) {

            // 多线程异步获取数据并追加至excel，join等待线程执行完成
            List<CompletableFuture> futures = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                CompletableFuture future = CompletableFuture.runAsync(() -> {
//                    List<ArtCrowd> dataList = this.getDataList();
                    // 数据追加
                    builder.append(getDataList());
                });
                futures.add(future);
            }

            futures.forEach(CompletableFuture::join);
            // 最终构建
            Workbook workbook = builder.build();

//            AttachmentExportUtil.export(workbook, "示例文件", response);

            export(workbook, "示例文件");

//            FileExportUtil.export(workbook, new File("target/示例文件"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMap导出(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("a", "测试A");
        headerMap.put("b", "测试B");

        List<Map<String, Object>> dataMapList = new ArrayList<>();
        Map<String, Object> v1 = new HashMap<>();
        v1.put("a", "数据a1");
        v1.put("b", 3);

        Map<String, Object> v2 = new HashMap<>();
        v2.put("a", "数据a2");
        v2.put("b", 5);

        dataMapList.add(v1);
        dataMapList.add(v2);

        List<String> titles = new ArrayList<>(headerMap.values());
        List<String> orders = new ArrayList<>(headerMap.keySet());
        Workbook workbook = DefaultExcelBuilder.getInstance()
            .sheetName("sheet1")
            .titles(titles)
            .widths(10,20)
            .fieldDisplayOrder(orders)
            .build(dataMapList);
        try {
            FileExportUtil.export(workbook, new File("D:/excel/test.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void export(Workbook workbook, String fileName) {
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

            File file = new File("target/" + fileName);

            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
            }

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
    }

    private List<Map<String, Object>> getDataList0() {

        return Stream.generate(Math::random).limit(50).map(v -> {

            HashMap<String, Object> map = new HashMap<>();
            map.put("这是列一", v);
            map.put("这是列二", v);
            return map;
        }).collect(Collectors.toList());
    }

    private List<Row> getDataList() {

        return Stream.generate(Math::random).limit(50).map(v -> {
            Row row = new Row();
            row.setAge(v.toString());
            row.setName("这是列一:" + v);
            row.setSex(v > 0.5 ? "1" : "0");
            row.setFav("这是列二");
            return row;
        }).collect(Collectors.toList());
    }
}
