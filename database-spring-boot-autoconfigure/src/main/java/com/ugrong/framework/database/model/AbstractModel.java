package com.ugrong.framework.database.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 抽象model
 *
 * @param <PK>
 */
public abstract class AbstractModel<PK extends Serializable> implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id", position = -1)
    @TableId(type = IdType.ASSIGN_ID)
    private PK id;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id", position = 200)
    @TableField(fill = FieldFill.INSERT)
    private PK createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 201)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人id", position = 202)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private PK modifyBy;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间", position = 203)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 租户id，默认为1，暂时不启用
     */
    @ApiModelProperty("租户id，默认为1，暂时不启用")
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private PK tenantId;

    /**
     * 删除标识
     */
    @ApiModelProperty("是否删除：true 表示已经删除")
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Boolean deleted;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PK getCreateBy() {
        return createBy;
    }

    public void setCreateBy(PK createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public PK getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(PK modifyBy) {
        this.modifyBy = modifyBy;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public PK getTenantId() {
        return tenantId;
    }

    public void setTenantId(PK tenantId) {
        this.tenantId = tenantId;
    }
}
