package com.icee;

import org.apache.ibatis.annotations.Select;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

//@SpringBootTest
public class PoiTets {

    public static void writeExcel() throws IOException {
        //创建一个excel对象(内存中)
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建sheet页
        XSSFSheet sheet = excel.createSheet("test");
        //创建行
        XSSFRow row = sheet.createRow(0);
        //创建列
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("name");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(1);
        row1.createCell(1).setCellValue("icee");

        //写入excel
        FileOutputStream out=new FileOutputStream(new File("/Users/icee/Desktop/test.xlsx"));
        excel.write(out);

        out.close();
        excel.close();
    }

    public static void read(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        //读取磁盘上已经存在的excel文件
        XSSFWorkbook excel=new XSSFWorkbook(fileInputStream);

        XSSFSheet sheet = excel.getSheet("test");
        //获取最后一行(有文字)的行号(实际行号-1)
        int rows=sheet.getLastRowNum();
        for(int i=0;i<=rows;i++){
            XSSFRow row = sheet.getRow(i);
            short cellNum = row.getLastCellNum();
            for(int j=0;j<cellNum;j++){
                System.out.print(row.getCell(j)+" ");
            }
            System.out.println();
        }
        excel.close();;
        fileInputStream.close();
    }


    @Test
    public void testExcel() throws IOException {
//        writeExcel();
        read("/Users/icee/Desktop/test.xlsx");
    }
}
