package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.domain.vo.attachment.SceneImageVO;
import com.homeservice.enums.BookingType;
import com.homeservice.enums.OrderStatus;
import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long customerId; // 客户ID

    @ApiId
    private Long skuId; // 服务规格ID

    private BookingType bookingType; // 预约类型

    private OrderStatus status; // 状态

    private String paymentStatus; // 支付状态

    private String dispatchStatus; // 派单状态

    private ServiceSnapshotVO service; // 服务快照

    private OrderAddressSnapshotVO address; // 地址快照

    private OffsetDateTime startTime; // 开始时间

    private OffsetDateTime endTime; // 结束时间

    private OffsetDateTime bufferEndTime; // 缓冲结束时间

    private OffsetDateTime createdAt; // 创建时间

    private OffsetDateTime paymentDeadline; // 支付截止时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime offerDeadline; // 抢单截止时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime dispatchDeadline; // 派单截止时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime confirmationDeadline; // 确认截止时间

    @ApiMoney
    private BigDecimal currentPrice; // 当前价格

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiMoney
    private BigDecimal dealPrice; // 成交价格

    private Integer priceVersion; // 价格版本

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long workerId; // 服务人员ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String workerName; // 服务人员姓名

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cancellationReason; // 取消原因

    private String remark; // 备注

    private Boolean reviewed; // 是否已评价

    private List<SceneImageVO> sceneImages; // 现场图片列表

    private String contactName; // 联系人

    private String contactPhone; // 联系电话

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime offerPublishedAt; // 优惠发布时间

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime closedAt; // 关闭时间

    private List<String> allowedActions; // 允许操作列表
}
