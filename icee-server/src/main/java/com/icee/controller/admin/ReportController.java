package com.icee.controller.admin;

import com.icee.result.Result;
import com.icee.service.ReportService;
import com.icee.vo.OrderReportVO;
import com.icee.vo.SalesTop10ReportVO;
import com.icee.vo.TurnoverReportVO;
import com.icee.vo.UserReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Tag(name = "数据统计接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 订单数据统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @Operation(summary = "订单数据")
    public Result<OrderReportVO> getOrdersStatistics(@DateTimeFormat(pattern="yyyy-mm-dd") LocalDate begin, @DateTimeFormat(pattern="yyyy-mm-dd")LocalDate end){
        OrderReportVO orderReportVO=reportService.getOrdersStatistics(begin,end);
        return Result.success(orderReportVO);
    }

    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @Operation(summary = "营业额数据")
    public Result<TurnoverReportVO>  getTurnOverStatistics(@DateTimeFormat(pattern="yyyy-mm-dd") LocalDate begin, @DateTimeFormat(pattern="yyyy-mm-dd")LocalDate end){
        TurnoverReportVO turnoverReportVO=reportService.getTurnOverStatistics(begin,end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 用户数据统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @Operation(summary = "用户数据")
    public Result<UserReportVO> getUserStatistics(@DateTimeFormat(pattern="yyyy-mm-dd") LocalDate begin, @DateTimeFormat(pattern="yyyy-mm-dd")LocalDate end){
        UserReportVO userReportVO=reportService.getUserStatistics(begin,end);
        return Result.success(userReportVO);
    }

    /**
     * 销量排名
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    @Operation(summary = "销量前10数据")
    public Result<SalesTop10ReportVO> getTop10Statistics(@DateTimeFormat(pattern="yyyy-mm-dd") LocalDate begin, @DateTimeFormat(pattern="yyyy-mm-dd")LocalDate end){
        SalesTop10ReportVO salesTop10ReportVO=reportService.getTop10Statistics(begin,end);
        return Result.success(salesTop10ReportVO);
    }

//    /**
//     * 数据导出
//     * @return
//     */
//    @GetMapping("/export")
//    @Operation(summary = "导出数据")
//    public Result<Export> getExport(){
//        return Result.success();
//    }


}
