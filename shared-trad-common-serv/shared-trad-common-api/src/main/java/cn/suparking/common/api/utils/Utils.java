package cn.suparking.common.api.utils;

public class Utils {

    /**
     * RMB FEN TO YUAN.
     * @param amount  RMB FEN
     * @return float
     */
    public static float rmbFenToYuan(final int amount) {
        return (float) amount / 100;
    }
}
