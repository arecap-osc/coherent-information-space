package org.hkrdi.eden.ggm.media.v1.ggm;

import org.aopalliance.intercept.MethodInvocation;
import org.contextualj.lang.annotation.expression.SourceClass;
import org.contextualj.lang.annotation.expression.SourceName;
import org.contextualj.lang.annotation.pointcut.Wrap;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.media.DrawingStyle;
import org.hkrdi.eden.ggm.media.utils.AnimatedGIFWriter;
import org.hkrdi.eden.ggm.media.utils.GifUtil;
import org.hkrdi.eden.ggm.media.utils.PngEncoderB;
import org.hkrdi.eden.ggm.media.utils.PngUtil;
import org.hkrdi.eden.ggm.media.v1.PngFrame;
import org.hkrdi.eden.ggm.service.geometry.SpatialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cop.annotation.ContextOriented;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;

@ContextOriented
@SourceClass(GgmEndpoint.class)
@Deprecated
public class GgmRequest {

    @Autowired
    private SpatialService spatialService;

    @Wrap
    @SourceName("getNetwork")
    public ResponseEntity<byte[]> wrapNetwork(MethodInvocation networkRequest) throws Throwable {
        DrawingStyle drawingStyle = new DrawingStyle((double) networkRequest.getArguments()[3],
                (String) networkRequest.getArguments()[4], (double) networkRequest.getArguments()[5]);
        Point center = new Point((double) networkRequest.getArguments()[6], (double) networkRequest.getArguments()[7]);
        Point bottomLeft = new Point((double) networkRequest.getArguments()[8], (double) networkRequest.getArguments()[9]);
        double scale = (double) networkRequest.getArguments()[10];
        String network = ((NettingType) networkRequest.getArguments()[1]).name()+"::"+ networkRequest.getArguments()[2];
        networkRequest.getArguments()[0] = PngUtil
                .getConnectorsPng(spatialService.getAddresses(network), spatialService.getOuterRoutes(network),
                        new DrawingOption(drawingStyle, center, bottomLeft, scale));
        return (ResponseEntity<byte[]>) networkRequest.proceed();
    }

    @Wrap
    @SourceName("getNetworkFrames")
    public ResponseEntity<byte[]> wrapNetworkFrames(MethodInvocation networkFrameRequest) throws Throwable {
        long time = System.currentTimeMillis();
        DrawingStyle drawingStyle = new DrawingStyle((double) networkFrameRequest.getArguments()[3],
                (String) networkFrameRequest.getArguments()[4], (double) networkFrameRequest.getArguments()[5]);
        Point center = new Point((double) networkFrameRequest.getArguments()[6], (double) networkFrameRequest.getArguments()[7]);
        Point bottomLeft = new Point((double) networkFrameRequest.getArguments()[8], (double) networkFrameRequest.getArguments()[9]);
        double scale = (double) networkFrameRequest.getArguments()[10];
        String network = ((NettingType) networkFrameRequest.getArguments()[1]).name()+"::"+ networkFrameRequest.getArguments()[2];
        AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
		writer.prepareForWrite(os, (int) Math.round(bottomLeft.getX()), (int) Math.round(bottomLeft.getY()));
        for (int frameNo = 1; frameNo<=4; frameNo++) {
			writer.writeFrame(os, PngUtil
                    .getAnimationFramePng(spatialService.getAddresses(network), spatialService.getOuterRoutes(network),
                            new DrawingOption(drawingStyle, center, bottomLeft, scale), frameNo), 300);
        }
		writer.finishWrite(os);
		networkFrameRequest.getArguments()[0] = os.toByteArray();
        System.out.println("Network frame response time"+(System.currentTimeMillis()-time)+"ms");
        return (ResponseEntity<byte[]>) networkFrameRequest.proceed();
    }
    
    @Wrap
    @SourceName("getNetworkFrame")
    public ResponseEntity<byte[]> wrapNetworkFrame(MethodInvocation networkFrameRequest) throws Throwable {
        long time = System.currentTimeMillis();
        DrawingStyle drawingStyle = new DrawingStyle((double) networkFrameRequest.getArguments()[4],
                (String) networkFrameRequest.getArguments()[5], (double) networkFrameRequest.getArguments()[6]);
        Point center = new Point((double) networkFrameRequest.getArguments()[7], (double) networkFrameRequest.getArguments()[8]);
        Point bottomLeft = new Point((double) networkFrameRequest.getArguments()[9], (double) networkFrameRequest.getArguments()[10]);
        double scale = (double) networkFrameRequest.getArguments()[11];
        String network = ((NettingType) networkFrameRequest.getArguments()[1]).name()+"::"+ networkFrameRequest.getArguments()[2];
        networkFrameRequest.getArguments()[0] = new PngEncoderB(PngUtil
                .getAnimationFramePng(spatialService.getAddresses(network), spatialService.getOuterRoutes(network),
                        new DrawingOption(drawingStyle, center, bottomLeft, scale), (int) networkFrameRequest.getArguments()[3]), true).pngEncode(true);
        System.out.println("Network frame response time"+(System.currentTimeMillis()-time)+"ms");
        return (ResponseEntity<byte[]>) networkFrameRequest.proceed();
    }


    @Wrap
    @SourceName("getNetworkAnimationFrame")
    public ResponseEntity<byte[]> wrapNetworkAnimationFrame(MethodInvocation networkAnimationFrameRequest) throws Throwable {
        DrawingStyle drawingStyle = new DrawingStyle((double) networkAnimationFrameRequest.getArguments()[3],
                (String) networkAnimationFrameRequest.getArguments()[4], (double) networkAnimationFrameRequest.getArguments()[5]);
        Point center = new Point((double) networkAnimationFrameRequest.getArguments()[6], (double) networkAnimationFrameRequest.getArguments()[7]);
        Point bottomLeft = new Point((double) networkAnimationFrameRequest.getArguments()[8], (double) networkAnimationFrameRequest.getArguments()[9]);
        double scale = (double) networkAnimationFrameRequest.getArguments()[10];
        String network = ((NettingType) networkAnimationFrameRequest.getArguments()[1]).name()+"::"+ networkAnimationFrameRequest.getArguments()[2];
        networkAnimationFrameRequest.getArguments()[0] = GifUtil
                .getNetworkAnimationFrameGif(spatialService.getAddresses(network), spatialService.getOuterRoutes(network),
                        new DrawingOption(drawingStyle, center, bottomLeft, scale));
        return (ResponseEntity<byte[]>) networkAnimationFrameRequest.proceed();
    }

    @Wrap
    @SourceName("getNetworkAnimationFrame2")
    public ResponseEntity<PngFrame> wrapNetworkAnimationFrame2(MethodInvocation networkAnimationFrame2Request) throws Throwable {
        DrawingStyle drawingStyle = new DrawingStyle((double) networkAnimationFrame2Request.getArguments()[3],
                (String) networkAnimationFrame2Request.getArguments()[4], (double) networkAnimationFrame2Request.getArguments()[5]);
        Point center = new Point((double) networkAnimationFrame2Request.getArguments()[6], (double) networkAnimationFrame2Request.getArguments()[7]);
        Point bottomLeft = new Point((double) networkAnimationFrame2Request.getArguments()[8], (double) networkAnimationFrame2Request.getArguments()[9]);
        double scale = (double) networkAnimationFrame2Request.getArguments()[10];
        String network = ((NettingType) networkAnimationFrame2Request.getArguments()[1]).name()+"::"+ networkAnimationFrame2Request.getArguments()[2];
        networkAnimationFrame2Request.getArguments()[0] = GifUtil
                .getNetworkAnimationFrameGif2(spatialService.getAddresses(network), spatialService.getOuterRoutes(network),
                        new DrawingOption(drawingStyle, center, bottomLeft, scale));
        return (ResponseEntity<PngFrame>) networkAnimationFrame2Request.proceed();
    }

}
