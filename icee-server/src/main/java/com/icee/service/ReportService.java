package com.icee.service;

import com.icee.vo.OrderReportVO;
import com.icee.vo.SalesTop10ReportVO;
import com.icee.vo.TurnoverReportVO;
import com.icee.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * @param  response
     * 导出数据
     */
    void export(HttpServletResponse response) throws IOException;
}
