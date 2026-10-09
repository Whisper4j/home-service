package com.homeservice.domain.vo.order;

import com.homeservice.domain.vo.attachment.SceneImageVO;
import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OfferVO {

    @ApiId
    private Long id; // 主键ID

    private String skuName; // 规格名称

    private String districtName; // 区县名称

    private String cityCode; // 城市编码

    private Integer durationMinutes; // 服务时长（分钟）

    private OffsetDateTime startTime; // 开始时间

    private OffsetDateTime endTime; // 结束时间

    private OffsetDateTime bufferEndTime; // 缓冲结束时间

    @ApiMoney
    private BigDecimal currentPrice; // 当前价格

    private Integer priceVersion; // 价格版本

    private OffsetDateTime offerDeadline; // 抢单截止时间

    private String description; // 说明

    private String included; // 包含内容

    private String excluded; // 不含内容

    private Boolean customerSuppliesParts; // 是否客户自备配件

    private List<SceneImageVO> sceneImages; // 现场图片列表

    private OffsetDateTime publishedAt; // 发布时间

    private String cityName; // 城市名称
}
