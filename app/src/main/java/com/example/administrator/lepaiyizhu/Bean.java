package com.example.administrator.lepaiyizhu;

/**
 * Created by Administrator on 2016/9/4 0004.
 */
public class Bean {


    /**
     * pic_480_854 : http://shougong.fn.img-space.com/g1/M00/06/E7/Cg-4q1b56_aIQ5F_AAHXRJiUSeAAAQ2owHc2vAAAddc550.jpg
     * url_480_854 :
     */

    private AndroidBean android;
    /**
     * pic_640_960 : http://shougong.fn.img-space.com/g1/M00/03/A6/Cg-4rFXEK_qIdPo_AADnZ7QwpLgAALKWgFSOIAAAOd_256.jpg
     * url_640_960 :
     * pic_640_1136 :
     * url_640_1136 :
     */

    private IPhoneBean iPhone;
    /**
     * pic_1024_768 :
     * url_1024_768 :
     * pic_2048_1536 :
     * url_2048_1536 :
     */

    private IPadBean iPad;

    public AndroidBean getAndroid() {
        return android;
    }

    public void setAndroid(AndroidBean android) {
        this.android = android;
    }

    public IPhoneBean getIPhone() {
        return iPhone;
    }

    public void setIPhone(IPhoneBean iPhone) {
        this.iPhone = iPhone;
    }

    public IPadBean getIPad() {
        return iPad;
    }

    public void setIPad(IPadBean iPad) {
        this.iPad = iPad;
    }

    public static class AndroidBean {
        private String pic_480_854;
        private String url_480_854;

        public String getPic_480_854() {
            return pic_480_854;
        }

        public void setPic_480_854(String pic_480_854) {
            this.pic_480_854 = pic_480_854;
        }

        public String getUrl_480_854() {
            return url_480_854;
        }

        public void setUrl_480_854(String url_480_854) {
            this.url_480_854 = url_480_854;
        }
    }

    public static class IPhoneBean {
        private String pic_640_960;
        private String url_640_960;
        private String pic_640_1136;
        private String url_640_1136;

        public String getPic_640_960() {
            return pic_640_960;
        }

        public void setPic_640_960(String pic_640_960) {
            this.pic_640_960 = pic_640_960;
        }

        public String getUrl_640_960() {
            return url_640_960;
        }

        public void setUrl_640_960(String url_640_960) {
            this.url_640_960 = url_640_960;
        }

        public String getPic_640_1136() {
            return pic_640_1136;
        }

        public void setPic_640_1136(String pic_640_1136) {
            this.pic_640_1136 = pic_640_1136;
        }

        public String getUrl_640_1136() {
            return url_640_1136;
        }

        public void setUrl_640_1136(String url_640_1136) {
            this.url_640_1136 = url_640_1136;
        }
    }

    public static class IPadBean {
        private String pic_1024_768;
        private String url_1024_768;
        private String pic_2048_1536;
        private String url_2048_1536;

        public String getPic_1024_768() {
            return pic_1024_768;
        }

        public void setPic_1024_768(String pic_1024_768) {
            this.pic_1024_768 = pic_1024_768;
        }

        public String getUrl_1024_768() {
            return url_1024_768;
        }

        public void setUrl_1024_768(String url_1024_768) {
            this.url_1024_768 = url_1024_768;
        }

        public String getPic_2048_1536() {
            return pic_2048_1536;
        }

        public void setPic_2048_1536(String pic_2048_1536) {
            this.pic_2048_1536 = pic_2048_1536;
        }

        public String getUrl_2048_1536() {
            return url_2048_1536;
        }

        public void setUrl_2048_1536(String url_2048_1536) {
            this.url_2048_1536 = url_2048_1536;
        }
    }
}
