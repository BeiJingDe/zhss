package com.ylxx.cloud.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caixiaopeng
 * @ClassName DeleteDTO
 * Description: TODO
 * @date 2021-01-24 17:50:54
 */
@Data
public class DeleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键数组")
    private List<String> ids;

}
