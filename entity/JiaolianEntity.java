package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 教练
 *
 * @author 
 * @email
 */
@TableName("jiaolian")
public class JiaolianEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public JiaolianEntity() {

	}

	public JiaolianEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")

    private Integer id;


    /**
     * 教练编号
     */
    @TableField(value = "jiaolian_uuid_number")

    private String jiaolianUuidNumber;


    /**
     * 教练姓名
     */
    @TableField(value = "jiaolian_name")

    private String jiaolianName;


    /**
     * 教练手机号
     */
    @TableField(value = "jiaolian_phone")

    private String jiaolianPhone;


    /**
     * 教练身份证号
     */
    @TableField(value = "jiaolian_id_number")

    private String jiaolianIdNumber;


    /**
     * 教练头像
     */
    @TableField(value = "jiaolian_photo")

    private String jiaolianPhoto;


    /**
     * 性别
     */
    @TableField(value = "sex_types")

    private Integer sexTypes;


    /**
     * 教练擅长
     */
    @TableField(value = "jiaolian_shanchang")

    private String jiaolianShanchang;


    /**
     * 任职时长
     */
    @TableField(value = "jiaolian_shichang")

    private String jiaolianShichang;


    /**
     * 教练介绍
     */
    @TableField(value = "jiaolian_content")

    private String jiaolianContent;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 设置：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 获取：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 设置：教练编号
	 */
    public String getJiaolianUuidNumber() {
        return jiaolianUuidNumber;
    }
    /**
	 * 获取：教练编号
	 */

    public void setJiaolianUuidNumber(String jiaolianUuidNumber) {
        this.jiaolianUuidNumber = jiaolianUuidNumber;
    }
    /**
	 * 设置：教练姓名
	 */
    public String getJiaolianName() {
        return jiaolianName;
    }
    /**
	 * 获取：教练姓名
	 */

    public void setJiaolianName(String jiaolianName) {
        this.jiaolianName = jiaolianName;
    }
    /**
	 * 设置：教练手机号
	 */
    public String getJiaolianPhone() {
        return jiaolianPhone;
    }
    /**
	 * 获取：教练手机号
	 */

    public void setJiaolianPhone(String jiaolianPhone) {
        this.jiaolianPhone = jiaolianPhone;
    }
    /**
	 * 设置：教练身份证号
	 */
    public String getJiaolianIdNumber() {
        return jiaolianIdNumber;
    }
    /**
	 * 获取：教练身份证号
	 */

    public void setJiaolianIdNumber(String jiaolianIdNumber) {
        this.jiaolianIdNumber = jiaolianIdNumber;
    }
    /**
	 * 设置：教练头像
	 */
    public String getJiaolianPhoto() {
        return jiaolianPhoto;
    }
    /**
	 * 获取：教练头像
	 */

    public void setJiaolianPhoto(String jiaolianPhoto) {
        this.jiaolianPhoto = jiaolianPhoto;
    }
    /**
	 * 设置：性别
	 */
    public Integer getSexTypes() {
        return sexTypes;
    }
    /**
	 * 获取：性别
	 */

    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    /**
	 * 设置：教练擅长
	 */
    public String getJiaolianShanchang() {
        return jiaolianShanchang;
    }
    /**
	 * 获取：教练擅长
	 */

    public void setJiaolianShanchang(String jiaolianShanchang) {
        this.jiaolianShanchang = jiaolianShanchang;
    }
    /**
	 * 设置：任职时长
	 */
    public String getJiaolianShichang() {
        return jiaolianShichang;
    }
    /**
	 * 获取：任职时长
	 */

    public void setJiaolianShichang(String jiaolianShichang) {
        this.jiaolianShichang = jiaolianShichang;
    }
    /**
	 * 设置：教练介绍
	 */
    public String getJiaolianContent() {
        return jiaolianContent;
    }
    /**
	 * 获取：教练介绍
	 */

    public void setJiaolianContent(String jiaolianContent) {
        this.jiaolianContent = jiaolianContent;
    }
    /**
	 * 设置：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 获取：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Jiaolian{" +
            "id=" + id +
            ", jiaolianUuidNumber=" + jiaolianUuidNumber +
            ", jiaolianName=" + jiaolianName +
            ", jiaolianPhone=" + jiaolianPhone +
            ", jiaolianIdNumber=" + jiaolianIdNumber +
            ", jiaolianPhoto=" + jiaolianPhoto +
            ", sexTypes=" + sexTypes +
            ", jiaolianShanchang=" + jiaolianShanchang +
            ", jiaolianShichang=" + jiaolianShichang +
            ", jiaolianContent=" + jiaolianContent +
            ", createTime=" + createTime +
        "}";
    }
}
