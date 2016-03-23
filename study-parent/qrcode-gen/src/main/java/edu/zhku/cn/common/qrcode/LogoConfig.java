package edu.zhku.cn.common.qrcode;

import java.awt.Color;

/**
 * logo图标配置类
 *
 * @author 李成
 * @time 2016年1月22日 上午11:31:50
 * @version 0.1
 * @since 0.1
 */
public class LogoConfig {

    /**
     * logo默认边框颜色
     */
    public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
    /**
     * logo默认边框宽度
     */
    public static final int DEFAULT_BORDER = 2;
    /**
     * logo默认大小
     */
    public static final int DEFAULT_LOGOPART = 5;

    /**
     * logo边框宽度,默认为2
     */
    private final int border = DEFAULT_BORDER;
    /**
     * logo边框颜色,默认为白色
     */
    private final Color borderColor;
    /**
     * logo大小,默认为生成二维码的1/5
     */
    private final int logoPart;

    /**
     * Creates a default config with on color {@link #BLACK} and off color
     * {@link #WHITE}, generating normal black-on-white barcodes.
     */
    public LogoConfig() {
        this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
    }

    /**
     * 有参构造函数
     *
     * @param borderColor
     *            logo默认边框颜色
     * @param logoPart
     *            logo大小,logo在二维码中所占大小,例如logoPart为5,则为1/5
     */
    public LogoConfig(Color borderColor, int logoPart) {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    /**
     * 获取logo默认边框宽度
     *
     * @return the border
     */
    public int getBorder() {
        return border;
    }

    /**
     * 获取logo默认边框颜色
     *
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * 获取logo大小默认为生成二维码的1/DEFAULT_LOGOPART,默认为1/5
     *
     * @return the logoPart
     */
    public int getLogoPart() {
        return logoPart;
    }

}
