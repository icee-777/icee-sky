package com.icee.service;

import com.icee.vo.OrderReportVO;
import com.icee.vo.SalesTop10ReportVO;
import com.icee.vo.TurnoverReportVO;
import com.icee.vo.UserReportVO;

import java.time.LocalDate;
import java.util.Date;

public interface ReportService {
    /**
     * 统计订单数据
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计营业数据
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计用户数据
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计销量排名
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end);
}
