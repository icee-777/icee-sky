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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
