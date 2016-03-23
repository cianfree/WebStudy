package edu.zhku.cn.common.qrcode;

import java.awt.Color;

/**
 * logoͼ��������
 *
 * @author ���
 * @time 2016��1��22�� ����11:31:50
 * @version 0.1
 * @since 0.1
 */
public class LogoConfig {

    /**
     * logoĬ�ϱ߿���ɫ
     */
    public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
    /**
     * logoĬ�ϱ߿���
     */
    public static final int DEFAULT_BORDER = 2;
    /**
     * logoĬ�ϴ�С
     */
    public static final int DEFAULT_LOGOPART = 5;

    /**
     * logo�߿���,Ĭ��Ϊ2
     */
    private final int border = DEFAULT_BORDER;
    /**
     * logo�߿���ɫ,Ĭ��Ϊ��ɫ
     */
    private final Color borderColor;
    /**
     * logo��С,Ĭ��Ϊ���ɶ�ά���1/5
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
     * �вι��캯��
     *
     * @param borderColor
     *            logoĬ�ϱ߿���ɫ
     * @param logoPart
     *            logo��С,logo�ڶ�ά������ռ��С,����logoPartΪ5,��Ϊ1/5
     */
    public LogoConfig(Color borderColor, int logoPart) {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    /**
     * ��ȡlogoĬ�ϱ߿���
     *
     * @return the border
     */
    public int getBorder() {
        return border;
    }

    /**
     * ��ȡlogoĬ�ϱ߿���ɫ
     *
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * ��ȡlogo��СĬ��Ϊ���ɶ�ά���1/DEFAULT_LOGOPART,Ĭ��Ϊ1/5
     *
     * @return the logoPart
     */
    public int getLogoPart() {
        return logoPart;
    }

}
