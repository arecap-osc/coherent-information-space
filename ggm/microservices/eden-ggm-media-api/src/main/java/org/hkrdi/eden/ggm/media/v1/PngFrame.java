package org.hkrdi.eden.ggm.media.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PngFrame implements Serializable {

    @JsonProperty
    private byte[] connectorsFrame;

    @JsonProperty
    private byte[] aFrame1;

    @JsonProperty
    private byte[] aFrame2;

    @JsonProperty
    private byte[] aFrame3;

    @JsonProperty
    private byte[] aFrame4;

    public byte[] getConnectorsFrame() {
        return connectorsFrame;
    }

    public void setConnectorsFrame(byte[] connectorsFrame) {
        this.connectorsFrame = connectorsFrame;
    }

    public byte[] getaFrame1() {
        return aFrame1;
    }

    public void setaFrame1(byte[] aFrame1) {
        this.aFrame1 = aFrame1;
    }

    public byte[] getaFrame2() {
        return aFrame2;
    }

    public void setaFrame2(byte[] aFrame2) {
        this.aFrame2 = aFrame2;
    }

    public byte[] getaFrame3() {
        return aFrame3;
    }

    public void setaFrame3(byte[] aFrame3) {
        this.aFrame3 = aFrame3;
    }

    public byte[] getaFrame4() {
        return aFrame4;
    }

    public void setaFrame4(byte[] aFrame4) {
        this.aFrame4 = aFrame4;
    }
}
