package org.hkrdi.eden.ggm.media.coherentspace;

import org.aopalliance.intercept.MethodInvocation;
import org.contextualj.lang.annotation.expression.SourceClass;
import org.contextualj.lang.annotation.expression.SourceName;
import org.contextualj.lang.annotation.pointcut.Wrap;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.media.DrawingStyle;
import org.hkrdi.eden.ggm.media.utils.AnimatedGIFWriter;
import org.hkrdi.eden.ggm.service.coherentspace.ie.ApplicationDataParameter;
import org.hkrdi.eden.ggm.service.coherentspace.ie.IeJwtUtil;
import org.springframework.cop.annotation.ContextOriented;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@ContextOriented
@SourceClass(HexavalentNetworkEndpoint.class)
public class HexavalentNetworkRequest {

//    @Autowired
//    private SpatialService spatialService;

	@Wrap
    @SourceName
    public ResponseEntity<byte[]> getHexavalentNetworkBook(MethodInvocation networkFrameRequest) throws Throwable {
    	return getHexavalentNetwork(networkFrameRequest);
    }
    
    @Wrap
    @SourceName
    public ResponseEntity<byte[]> getHexavalentNetwork(MethodInvocation networkFrameRequest) throws Throwable {
        long time = System.currentTimeMillis();
        DrawingStyle drawingStyle = new DrawingStyle((double) networkFrameRequest.getArguments()[8],
                (String) networkFrameRequest.getArguments()[9], (double) networkFrameRequest.getArguments()[10]);
        Point center = new Point((double) networkFrameRequest.getArguments()[11], (double) networkFrameRequest.getArguments()[12]);
        Point bottomLeft = new Point((double) networkFrameRequest.getArguments()[13], (double) networkFrameRequest.getArguments()[14]);
        double scale = (double) networkFrameRequest.getArguments()[15];
        ApplicationDataParameter applicationDataParameter =
                IeJwtUtil.getBean((String) networkFrameRequest.getArguments()[16], ApplicationDataParameter.class);
        Map<String,String> selectedDataMapIds = (Map<String,String>) networkFrameRequest.getArguments()[17];
        String network = ((NettingType) networkFrameRequest.getArguments()[1]).name()+"::"+ networkFrameRequest.getArguments()[2];
        AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        writer.prepareForWrite(os, (int) Math.round(bottomLeft.getX()), (int) Math.round(bottomLeft.getY()));
        for (int frameNo = 1; frameNo<=4; frameNo++) {
            writer.writeFrame(os, HexavalentNetworkMedia
                    .getLayerFramePng(network,
                            (boolean) networkFrameRequest.getArguments()[3], (boolean) networkFrameRequest.getArguments()[4],
                            (boolean) networkFrameRequest.getArguments()[5], (boolean) networkFrameRequest.getArguments()[6],
                            (boolean) networkFrameRequest.getArguments()[7], applicationDataParameter,
                            new DrawingOption(drawingStyle, center, bottomLeft, scale), frameNo, selectedDataMapIds), 300);
        }
        writer.finishWrite(os);
        networkFrameRequest.getArguments()[0] = os.toByteArray();
        System.out.println("Network frame response time"+(System.currentTimeMillis()-time)+"ms");
        return (ResponseEntity<byte[]>) networkFrameRequest.proceed();
    }

}
