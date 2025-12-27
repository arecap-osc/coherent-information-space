package org.arecap.eden.ia.console.microservice.graph.mvp.mandelbrot.media;

import org.arecap.eden.ia.console.informationalstream.api.ComplexPlane;
import org.arecap.eden.ia.console.media.MediaRenderLayerFactory;
import org.arecap.eden.ia.console.media.MediaRendererLayer;
import org.arecap.eden.ia.console.media.MediaRendererTransform;
import org.arecap.eden.ia.console.media.PNGMediaRendererDrawer;
import org.arecap.eden.ia.console.media.util.Graphics2dUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MandelbrotSetGraphMedia implements MediaRenderLayerFactory, MediaRendererLayer, PNGMediaRendererDrawer {


    private Boolean needRefresh = false;


    @Override
    public List<MediaRendererLayer> getMediaRendererLayers() {
        return Arrays.asList(this);
    }


    @Override
    public void drawContent(Graphics2D graphics2D, MediaRendererTransform mediaRendererTransform) {
        Double xCenterScale = mediaRendererTransform.getCenterX();
        Double yCenterScale = mediaRendererTransform.getCenterY();

        Double xr1 = xCenterScale - mediaRendererTransform.getScale() / 2;
        Double xr2 = xCenterScale + mediaRendererTransform.getScale() / 2;
        Double yr1 = yCenterScale - mediaRendererTransform.getScale() / 2;
        Double yr2 = yCenterScale + mediaRendererTransform.getScale() / 2;

        Double ratio = Math.abs(xr2 - xr1) / Math.abs(yr2-yr1);
        Double sratio = mediaRendererTransform.getWidth()/mediaRendererTransform.getHeight();
        if(sratio > ratio) {
            Double xa = sratio / ratio;
            xr1 *= xa;
            xr2 *= xa;
        } else {
            Double ya = ratio / sratio;
            yr1 *= ya;
            yr2 *= ya;
        }

        Double fx1 = new Double(xr1);
        Double fx2 = new Double(xr2);
        Double fy1 = new Double(yr1);
        Double fy2 = new Double(yr2);

        Double dx = (xr2 - xr1) / ( 0.5 + (mediaRendererTransform.getWidth() -1));
        Double dy = (yr2 - yr1) / ( 0.5 + (mediaRendererTransform.getHeight() -1));
        Double yStep = new Double(dy);
        Double xStep = new Double(dx);


        long t = System.currentTimeMillis();



        //single process
        Double pixelY = 0D;
        Double y = new Double(yr1);
        while (pixelY.compareTo(mediaRendererTransform.getHeight()) < 0) {
            Double pixelX = 0D;
            Double x = new Double(xr1);
            while (pixelX.compareTo(mediaRendererTransform.getWidth()) < 0) {
                Map<Integer, ComplexPlane> equation = MandelbrotSetUtil.iterateEquation(x, y, 10D, 100);
                for(Integer step: equation.keySet()) {
                    ComplexPlane z = equation.get(step);
                    graphics2D.setColor(MandelbrotSetUtil.getColor(100, step, z.getReal(), z.getImaginary()));
                    Graphics2dUtils.drawPoint(graphics2D, pixelX, pixelY, 1D, 1D);
                }
                x += xStep;
                pixelX++;
            }
            y += yStep;
            pixelY++;
        }

        System.out.println("Draw mandelbrot set single process time :\t" + (System.currentTimeMillis() - t) + "\tms");

        //parallel process
//TODO
//        System.out.println("Draw mandelbrot set parallel process time :\t" + (System.currentTimeMillis() - t) + "\tms");


    }

    @Override
    public Boolean isNeedRefresh() {
        return needRefresh;
    }

    @Override
    public void setNeedRefresh(Boolean needRefresh) {
        this.needRefresh = needRefresh;
    }



}
