package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.domain.vo.dispatch.AssignmentVO;
import com.homeservice.domain.vo.payment.PaymentVO;
import com.homeservice.domain.vo.review.ReviewVO;

import java.util.List;

import lombok.Data;

@Data
public class OrderHistoryVO {

    private List<PaymentVO> payments; // 支付流水列表

    private List<PriceHistoryVO> priceHistory; // 价格历史列表

    private List<AssignmentVO> assignments; // 分配记录列表

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReviewVO review; // 评价信息

    private List<OrderStatusHistoryVO> statusHistory; // 状态历史列表
}
