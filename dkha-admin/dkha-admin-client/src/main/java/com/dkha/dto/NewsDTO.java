

package com.dkha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.dkha.commons.tools.validator.group.AddGroup;
import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.dkha.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * 新闻管理
 */
@Data
@ApiModel(value = "新闻管理")
public class NewsDTO implements Serializable {

    @ApiModelProperty(value = "id")
    @Null(message="{id.null}", groups = AddGroup.class)
    @NotNull(message="{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "标题")
    @NotBlank(message="{news.title.require}", groups = DefaultGroup.class)
    private String title;

    @ApiModelProperty(value = "内容")
    @NotBlank(message="{news.content.require}", groups = DefaultGroup.class)
    private String content;

    @ApiModelProperty(value = "发布时间")
    private Date pubDate;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;

}
