package edu.zhku.cn.common.qrcode;

import edu.zhku.cn.common.qrcode.BxtpwQRCodeUtil;
import edu.zhku.cn.common.qrcode.LogoConfig;

import java.io.Serializable;

/**
 * 二维码参数配置实体对象
 *
 * @author 李成
 * @time 2016年1月19日 下午2:09:18
 * @version 0.1
 * @since 0.1
 */
public class BxtpwQRCode implements Serializable {

    private static final long serialVersionUID = -1519846408446344229L;

    /**
     * 二维码保存路径
     */
    private String savePath;
    /**
     * 不设置边框大小时图片大小(宽和高)
     */
    private Integer size = 200;
    /**
     * 生成图片类型
     */
    private FormatType formatType;
    /**
     * 二维码内容/相关链接
     */
    private String content;
    /**
     * 白色边框大小,单位px
     */
    private Integer borderSize;
    /**
     * 要添加的logo,为空时则无logo
     */
    private String logoPath;
    /**
     * 生成二维码主体颜色,暂时只支持黑色和红色,默认不传为黑色
     */
    private HexColorType hexColorType = HexColorType.BLACK;
    /**
     * 生成二维码时logo的位置,默认不传为居中
     */
    private LogoPosition logoPosition = LogoPosition.CENTER;
    /**
     * logo图标配置类
     */
    private LogoConfig logoConfig;

    /**
     * 获取二维码保存路径
     *
     * @return the savePath
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * 设置二维码保存路径,并设置formatType(生成图片类型)
     *
     * @param String类型
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
        String formatType = BxtpwQRCodeUtil.getImgFormatType(savePath);
        for (FormatType type : FormatType.values()) {
            if (type.getValue().equals(formatType)) {
                this.formatType = type;
                return;
            }
        }
    }

    /**
     * 获取不设置边框大小时图片大小(宽和高)
     *
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 设置不设置边框大小时图片大小(宽和高)
     *
     * @param Integer类型
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 获取生成图片类型,默认为png
     *
     * @return the formatType
     */
    public FormatType getFormatType() {
        return formatType;
    }

    /**
     * 获取二维码内容/相关链接
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置二维码内容/相关链接
     *
     * @param String类型
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取白色边框大小,单位px
     *
     * @return the borderSize
     */
    public Integer getBorderSize() {
        return borderSize;
    }

    /**
     * 设置白色边框大小,单位px
     *
     * @param borderSize
     */
    public void setBorderSize(Integer borderSize) {
        this.borderSize = borderSize;
    }

    /**
     * 获取要添加的logo
     *
     * @return the logoPath
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * 设置要添加的logo
     *
     * @param String类型
     */
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    /**
     * 获取生成二维码主体颜色,暂时只支持黑色和红色,默认不传为黑色
     *
     * @return the hexColorType
     */
    public HexColorType getHexColorType() {
        return hexColorType;
    }

    /**
     * 设置生成二维码主体颜色,暂时只支持黑色和红色,默认不传为黑色
     *
     * @param HexColorType类型
     */
    public void setHexColorType(HexColorType hexColorType) {
        this.hexColorType = hexColorType;
    }

    /**
     * 获取生成二维码时logo的位置,默认不传为居中
     *
     * @return the logoPosition
     */
    public LogoPosition getLogoPosition() {
        return logoPosition;
    }

    /**
     * 设置生成二维码时logo的位置,默认不传为居中
     *
     * @param LogoPosition类型
     */
    public void setLogoPosition(LogoPosition logoPosition) {
        this.logoPosition = logoPosition;
    }

    /**
     * 获取logo图标配置类
     *
     * @return the logoConfig
     */
    public LogoConfig getLogoConfig() {
        return logoConfig;
    }

    /**
     * 设置logo图标配置类
     *
     * @param LogoConfig类型
     */
    public void setLogoConfig(LogoConfig logoConfig) {
        this.logoConfig = logoConfig;
    }

    /**
     * logo位置枚举类
     *
     * @author 李成
     * @time 2016年1月22日 下午3:21:56
     * @version 0.1
     * @since 0.1
     */
    public enum LogoPosition {
        /**
         * 正中间
         */
        CENTER,
        /**
         * 右下角
         */
        RIGHT_CORNER;
    }

    /**
     * 生成图片格式类型
     *
     * @author 李成
     * @time 2016年1月22日 上午10:29:16
     * @version 0.1
     * @since 0.1
     */
    public enum FormatType {

        /**
         * png格式图片
         */
        PNG("png"),
        /**
         * jpg格式图片
         */
        JPG("jpg"),
        /**
         * jpeg格式图片
         */
        JPEG("jpeg"),
        /**
         * bmp格式图片
         */
        BMP("bmp"),
        /**
         * gif格式图片
         */
        GIF("gif");

        private String value;

        private FormatType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 生成二维码主体颜色,暂时只支持黑色和红色
     *
     * @author 李成
     * @time 2016年1月22日 上午10:32:15
     * @version 0.1
     * @since 0.1
     */
    public enum HexColorType {
        /**
         * 黑色
         */
        BLACK(0xff000000),
        /**
         * 红色
         */
        RED(0xffff0000);

        private int value;

        private HexColorType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
