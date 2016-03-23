package edu.zhku.cn.common.qrcode;

import edu.zhku.cn.common.qrcode.BxtpwQRCodeUtil;
import edu.zhku.cn.common.qrcode.LogoConfig;

import java.io.Serializable;

/**
 * ��ά���������ʵ�����
 *
 * @author ���
 * @time 2016��1��19�� ����2:09:18
 * @version 0.1
 * @since 0.1
 */
public class BxtpwQRCode implements Serializable {

    private static final long serialVersionUID = -1519846408446344229L;

    /**
     * ��ά�뱣��·��
     */
    private String savePath;
    /**
     * �����ñ߿��СʱͼƬ��С(��͸�)
     */
    private Integer size = 200;
    /**
     * ����ͼƬ����
     */
    private FormatType formatType;
    /**
     * ��ά������/�������
     */
    private String content;
    /**
     * ��ɫ�߿��С,��λpx
     */
    private Integer borderSize;
    /**
     * Ҫ��ӵ�logo,Ϊ��ʱ����logo
     */
    private String logoPath;
    /**
     * ���ɶ�ά��������ɫ,��ʱֻ֧�ֺ�ɫ�ͺ�ɫ,Ĭ�ϲ���Ϊ��ɫ
     */
    private HexColorType hexColorType = HexColorType.BLACK;
    /**
     * ���ɶ�ά��ʱlogo��λ��,Ĭ�ϲ���Ϊ����
     */
    private LogoPosition logoPosition = LogoPosition.CENTER;
    /**
     * logoͼ��������
     */
    private LogoConfig logoConfig;

    /**
     * ��ȡ��ά�뱣��·��
     *
     * @return the savePath
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     * ���ö�ά�뱣��·��,������formatType(����ͼƬ����)
     *
     * @param String����
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
     * ��ȡ�����ñ߿��СʱͼƬ��С(��͸�)
     *
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * ���ò����ñ߿��СʱͼƬ��С(��͸�)
     *
     * @param Integer����
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * ��ȡ����ͼƬ����,Ĭ��Ϊpng
     *
     * @return the formatType
     */
    public FormatType getFormatType() {
        return formatType;
    }

    /**
     * ��ȡ��ά������/�������
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * ���ö�ά������/�������
     *
     * @param String����
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * ��ȡ��ɫ�߿��С,��λpx
     *
     * @return the borderSize
     */
    public Integer getBorderSize() {
        return borderSize;
    }

    /**
     * ���ð�ɫ�߿��С,��λpx
     *
     * @param borderSize
     */
    public void setBorderSize(Integer borderSize) {
        this.borderSize = borderSize;
    }

    /**
     * ��ȡҪ��ӵ�logo
     *
     * @return the logoPath
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * ����Ҫ��ӵ�logo
     *
     * @param String����
     */
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    /**
     * ��ȡ���ɶ�ά��������ɫ,��ʱֻ֧�ֺ�ɫ�ͺ�ɫ,Ĭ�ϲ���Ϊ��ɫ
     *
     * @return the hexColorType
     */
    public HexColorType getHexColorType() {
        return hexColorType;
    }

    /**
     * �������ɶ�ά��������ɫ,��ʱֻ֧�ֺ�ɫ�ͺ�ɫ,Ĭ�ϲ���Ϊ��ɫ
     *
     * @param HexColorType����
     */
    public void setHexColorType(HexColorType hexColorType) {
        this.hexColorType = hexColorType;
    }

    /**
     * ��ȡ���ɶ�ά��ʱlogo��λ��,Ĭ�ϲ���Ϊ����
     *
     * @return the logoPosition
     */
    public LogoPosition getLogoPosition() {
        return logoPosition;
    }

    /**
     * �������ɶ�ά��ʱlogo��λ��,Ĭ�ϲ���Ϊ����
     *
     * @param LogoPosition����
     */
    public void setLogoPosition(LogoPosition logoPosition) {
        this.logoPosition = logoPosition;
    }

    /**
     * ��ȡlogoͼ��������
     *
     * @return the logoConfig
     */
    public LogoConfig getLogoConfig() {
        return logoConfig;
    }

    /**
     * ����logoͼ��������
     *
     * @param LogoConfig����
     */
    public void setLogoConfig(LogoConfig logoConfig) {
        this.logoConfig = logoConfig;
    }

    /**
     * logoλ��ö����
     *
     * @author ���
     * @time 2016��1��22�� ����3:21:56
     * @version 0.1
     * @since 0.1
     */
    public enum LogoPosition {
        /**
         * ���м�
         */
        CENTER,
        /**
         * ���½�
         */
        RIGHT_CORNER;
    }

    /**
     * ����ͼƬ��ʽ����
     *
     * @author ���
     * @time 2016��1��22�� ����10:29:16
     * @version 0.1
     * @since 0.1
     */
    public enum FormatType {

        /**
         * png��ʽͼƬ
         */
        PNG("png"),
        /**
         * jpg��ʽͼƬ
         */
        JPG("jpg"),
        /**
         * jpeg��ʽͼƬ
         */
        JPEG("jpeg"),
        /**
         * bmp��ʽͼƬ
         */
        BMP("bmp"),
        /**
         * gif��ʽͼƬ
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
     * ���ɶ�ά��������ɫ,��ʱֻ֧�ֺ�ɫ�ͺ�ɫ
     *
     * @author ���
     * @time 2016��1��22�� ����10:32:15
     * @version 0.1
     * @since 0.1
     */
    public enum HexColorType {
        /**
         * ��ɫ
         */
        BLACK(0xff000000),
        /**
         * ��ɫ
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
