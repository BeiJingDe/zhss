package com.ylxx.cloud.netty.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author caixiaopeng
 * @ClassName NettyMessage
 * Description: TODO
 * @date 2021-01-28 15:25:36
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Netty消息发送对象", description="Netty消息发送对象")
public class NettyMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备ID")
    private String deviceId;

    @ApiModelProperty("发送数据，hex格式")
    private List<String> hexBody;

}
