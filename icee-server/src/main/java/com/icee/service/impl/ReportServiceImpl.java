package com.icee.service.impl;

import com.icee.entity.OrderDetail;
import com.icee.entity.Orders;
import com.icee.entity.User;
import com.icee.mapper.OrderDetailMapper;
import com.icee.mapper.OrderMapper;
import com.icee.mapper.UserMapper;
import com.icee.service.ReportService;
import com.icee.vo.OrderReportVO;
import com.icee.vo.SalesTop10ReportVO;
import com.icee.vo.TurnoverReportVO;
import com.icee.vo.UserReportVO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime start=begin.atStartOfDay();
        LocalDateTime endTime=end.atTime(23,59,59);
        List<Orders> orderList=orderMapper.getAllOrder(start,endTime);
        List<Orders> validOrderList=orderMapper.getValidOrder(start,endTime);
        int totalOrderCount=orderList.size();
        int validOrderCount=validOrderList.size();
        double orderCompletionRate=0;
        if (totalOrderCount != 0) {
            orderCompletionRate=(double)validOrderCount/totalOrderCount;
        }

        StringBuilder dateList=new StringBuilder();
        StringBuilder orderCountList=new StringBuilder();
        StringBuilder validOrderCountList=new StringBuilder();
        int o=0,vo=0,olen=orderList.size(),volen=validOrderList.size();
        for(LocalDate i=begin;!i.isEqual(end.plusDays(1));i=i.plusDays(1)){
            dateList.append(i).append(",");
            int orderNum=0,validOrderNum=0;
            while(o<olen&&orderList.get(o).getOrderTime().toLocalDate().isEqual(i)){  //日期相等
                orderNum++;
                o++;
            }
            orderCountList.append(orderNum).append(",");
            while(vo<volen&&validOrderList.get(vo).getOrderTime().toLocalDate().isEqual(i)){  //日期相等
                validOrderNum++;
                vo++;
            }
            validOrderCountList.append(validOrderNum).append(",");
        }
        dateList.deleteCharAt(dateList.length()-1);
        orderCountList.deleteCharAt(orderCountList.length()-1);
        validOrderCountList.deleteCharAt(validOrderCountList.length()-1);
//        StringBuilder dateList=new StringBuilder();
//        StringBuilder orderCountList=new StringBuilder();
//        Set<LocalDate> dateSet=new HashSet<>();
//        int total=0;
//        for(Orders orders : orderList){
//            LocalDate date=orders.getOrderTime().toLocalDate();
//            if(dateSet.isEmpty()){
//                dateSet.add(date);
//                total=1;
//                continue;
//            }
//            if(dateSet.contains(date)){
//                total++;
//            }else{
//                dateSet.add(date);
//                orderCountList.append(total).append(",");
//                total=1;
//            }
//        }
//        orderCountList.append(total);
//        dateList.append(dateSet.toString());
//        dateList.deleteCharAt(0);
//        dateList.deleteCharAt(dateList.length()-1);
//
//        StringBuilder validOrderCountList=new StringBuilder();
//        total=0;
//        for(Orders orders : validOrderList){
//            LocalDate date=orders.getOrderTime().toLocalDate();
//            if(dateSet.isEmpty()){
//                dateSet.add(date);
//                total=1;
//                continue;
//            }
//            if(dateSet.contains(date)){
//                total++;
//            }else{
//                dateSet.add(date);
//                validOrderCountList.append(total).append(",");
//                total=1;
//            }
//        }
//        validOrderCountList.append(total);

        OrderReportVO orderReportVO = OrderReportVO.builder().dateList(dateList.toString())
                .orderCountList(orderCountList.toString())
                .validOrderCountList(validOrderCountList.toString())
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
        return orderReportVO;
    }

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime startTime=begin.atStartOfDay();
        LocalDateTime endTime=end.atTime(23,59,59);
        List<Orders> ordersList=orderMapper.getAllOrder(startTime,endTime);

        StringBuilder dateList=new StringBuilder();
        StringBuilder turnoverList=new StringBuilder();
        int order_pos=0,order_len=ordersList.size();
        BigDecimal money=new BigDecimal(0);
        for(LocalDate i=begin;!i.isEqual(end.plusDays(1));i=i.plusDays(1)){
            dateList.append(i).append(",");
            while(order_pos<order_len&&ordersList.get(order_pos).getOrderTime().toLocalDate().isEqual(i)){  //日期相等
                if(ordersList.get(order_pos).getStatus()==Orders.COMPLETED){
                    //todo BigDecmal的操作方法返回的是新的BigDecimal对象,需要赋值给money
                    money=money.add(ordersList.get(order_pos).getAmount());
                }
                order_pos++;
            }
            turnoverList.append(money).append(",");
            money=money.multiply(BigDecimal.ZERO); //置零
        }
        dateList.deleteCharAt(dateList.length()-1);
        turnoverList.deleteCharAt(turnoverList.length()-1);

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(dateList.toString())
                .turnoverList(turnoverList.toString())
                .build();
        return turnoverReportVO;
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime startTime=begin.atStartOfDay();
        LocalDateTime endTime=end.atTime(23,59,59);
        List<User> userList=userMapper.getAllUser(startTime,endTime);
        Long startUserCount=userMapper.getStartUserCount(startTime);
        StringBuilder dateList=new StringBuilder();
        StringBuilder newUserList=new StringBuilder();
        StringBuilder totalUserList=new StringBuilder();
        Long todayNewUser=0L,totalUser=startUserCount;
        int user_pos=0,user_len=userList.size();
        for(LocalDate i=begin;!i.isEqual(end.plusDays(1));i=i.plusDays(1)){
            dateList.append(i).append(",");
            while(user_pos<user_len&&userList.get(user_pos).getCreateTime().toLocalDate().isEqual(i)){
                todayNewUser++;
                user_pos++;
            }
            totalUser+=todayNewUser;
            newUserList.append(todayNewUser).append(",");
            totalUserList.append(totalUser).append(",");
            todayNewUser=0L;
        }
        dateList.deleteCharAt(dateList.length()-1);
        newUserList.deleteCharAt(newUserList.length()-1);
        totalUserList.deleteCharAt(totalUserList.length()-1);

        UserReportVO userReportVO = UserReportVO.builder()
                .dateList(dateList.toString())
                .newUserList(newUserList.toString())
                .totalUserList(totalUserList.toString())
                .build();
        return userReportVO;
    }

    /**
     * 商品统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime startTime=begin.atStartOfDay();
        LocalDateTime endTime=end.atTime(23,59,59);
        List<Long> orderIdList=orderMapper.getAllOrderId(startTime,endTime);
        List<OrderDetail> orderDetailList=orderDetailMapper.getTop10ByOrderIds(orderIdList);

        StringBuilder nameList=new StringBuilder();
        StringBuilder numberList=new StringBuilder();
        for(OrderDetail od : orderDetailList){
            nameList.append(od.getName()).append(",");
            numberList.append(od.getNumber()).append(",");
        }
        nameList.deleteCharAt(nameList.length()-1);
        numberList.deleteCharAt(numberList.length()-1);

        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(nameList.toString())
                .numberList(numberList.toString())
                .build();
        return salesTop10ReportVO;
    }

    /**
     * @param  response
     * 导出报表
     */
    @Override
    public void export(HttpServletResponse response) throws IOException {
        //准备数据
        List<Orders> ordersList=orderMapper.getAllOrder(LocalDate.now().minusMonths(1).atStartOfDay(),LocalDate.now().atTime(23,59,59));
        List<User> userList=userMapper.getAllUser(LocalDate.now().minusMonths(1).atStartOfDay(),LocalDate.now().atTime(23,59,59));
        int validCount=0,total=ordersList.size(),newUser=0;
        BigDecimal turnOver=new BigDecimal(0);
        for(Orders orders : ordersList){
            if(orders.getStatus()==5){
                turnOver=turnOver.add(orders.getAmount());
                validCount++;
            }
        }
        for(User user : userList){
            if(user.getCreateTime().isAfter(LocalDate.now().minusMonths(1).atStartOfDay())&&user.getCreateTime().isBefore(LocalDateTime.now())){
                newUser++;
            }
        }
        double unitPrice=0.0,orderCompletionRate=0.0;
        if(total!=0){
            orderCompletionRate=(double)validCount/total;
        }
        if(validCount!=0){
            unitPrice=turnOver.divide(new BigDecimal(validCount)).doubleValue();
        }

        FileInputStream in=new FileInputStream(new File("/Users/icee/Downloads/baidu/sky/资料/day12/运营数据报表模板.xlsx"));
        XSSFWorkbook excel=new XSSFWorkbook(in);
        XSSFSheet sheet = excel.getSheet("sheet1");
        int lastRowNum = sheet.getLastRowNum();
        for(int i=3,flag=0;i<lastRowNum;i++){
            XSSFRow row = sheet.getRow(i);
            int cellNum = row.getLastCellNum();
            for(int j=1;j<cellNum;j++){
                XSSFCell cell = row.getCell(j);
                if(cell==null){
                    continue;
                }
                if(cell.getStringCellValue().contains("营业额")){
                    row.getCell(j+1).setCellValue(turnOver.doubleValue());
                    j++;
                }else if(cell.getStringCellValue().contains("订单完成率")){
                    row.getCell(j+1).setCellValue(orderCompletionRate);
                    j++;
                }else if(cell.getStringCellValue().contains("新增用户数")){
                    row.getCell(j+1).setCellValue(newUser);
                    j++;
                }else if(cell.getStringCellValue().contains("有效订单")){
                    row.getCell(j+1).setCellValue(validCount);
                    j++;
                }else if(cell.getStringCellValue().contains("平均客单价")){
                    row.getCell(j+1).setCellValue(unitPrice);
                    j++;
                    flag=1;
                }
            }
            if(flag==1){
                break;
            }
        }

        LocalDate start=LocalDate.now().minusMonths(1);
        LocalDate end=LocalDate.now();
        int rowNum=7;
        for(;!start.isEqual(end);start=start.plusDays(1),rowNum++){
            validCount=0;
            total=0;
            turnOver=new BigDecimal(0);
            unitPrice=0.0;
            orderCompletionRate=0.0;
            newUser=0;
            for(Orders orders : ordersList){
                if(orders.getOrderTime().toLocalDate().isEqual(start)){
                    total++;
                    if(orders.getStatus()==5){
                        turnOver=turnOver.add(orders.getAmount());
                        validCount++;
                    }
                }
            }
            if(total!=0){
                if(validCount!=0){
                    //todo 除不尽时需要指定精度和舍入模式
                    unitPrice=turnOver.divide(new BigDecimal(validCount),2, RoundingMode.HALF_UP).doubleValue();
                }
                orderCompletionRate=validCount/total;
            }
            for(User user : userList){
                if(user.getCreateTime().isAfter(LocalDate.now().atStartOfDay())&&user.getCreateTime().isBefore(LocalDate.now().atTime(23,59,59))){
                    newUser++;
                }
            }
            XSSFRow row = sheet.getRow(rowNum);
            row.getCell(1).setCellValue(start.toString());
            row.getCell(2).setCellValue(turnOver.doubleValue());
            row.getCell(3).setCellValue(validCount);
            row.getCell(4).setCellValue(orderCompletionRate);
            row.getCell(5).setCellValue(unitPrice);
            row.getCell(6).setCellValue(newUser);
        }

        ServletOutputStream out = response.getOutputStream();
        excel.write(out);
        excel.close();
        out.close();
    }
}
