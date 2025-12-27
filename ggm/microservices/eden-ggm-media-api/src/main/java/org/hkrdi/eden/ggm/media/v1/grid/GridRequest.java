package org.hkrdi.eden.ggm.media.v1.grid;

import org.aopalliance.intercept.MethodInvocation;
import org.contextualj.lang.annotation.expression.SourceClass;
import org.contextualj.lang.annotation.expression.SourceName;
import org.contextualj.lang.annotation.pointcut.Wrap;
import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.media.DrawingStyle;
import org.hkrdi.eden.ggm.media.utils.PngUtil;
import org.hkrdi.eden.ggm.service.geometry.SpatialService;
import org.hkrdi.eden.ggm.service.geometry.SpatialUtil;
import org.hkrdi.eden.ggm.service.semanticmap.MapWordRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cop.annotation.ContextOriented;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;

@ContextOriented
@SourceClass(GridEndpoint.class)
public class GridRequest {

    @Autowired
    private SpatialService spatialService;

    @Autowired
    private MapWordRepositoryService mapWordService;

    @Wrap
    @SourceName("getGridFrame")
    public ResponseEntity<byte[]> wrapGridFrame(MethodInvocation gridkFrameRequest) throws Throwable {
        DrawingStyle drawingStyle = new DrawingStyle((double) gridkFrameRequest.getArguments()[3],
                (String) gridkFrameRequest.getArguments()[4], (double) gridkFrameRequest.getArguments()[5]);
        Point center = new Point((double) gridkFrameRequest.getArguments()[6], (double) gridkFrameRequest.getArguments()[7]);
        Point bottomLeft = new Point((double) gridkFrameRequest.getArguments()[8], (double) gridkFrameRequest.getArguments()[9]);
        double scale = (double) gridkFrameRequest.getArguments()[10];
        gridkFrameRequest.getArguments()[0] = PngUtil
                .getGridFramePng(spatialService.getGrid("SUSTAINABLE_VERTICES::0"), spatialService.getGrid("METABOLIC_VERTICES::0"),
                        new DrawingOption(drawingStyle, center, bottomLeft, scale));
        return (ResponseEntity<byte[]>) gridkFrameRequest.proceed();
    }

    @Wrap
    @SourceName("getGridAddresses")
    public ResponseEntity<byte[]> wrapGridAddresses(MethodInvocation gridAddressesRequest) throws Throwable {
        DrawingStyle drawingStyle = new DrawingStyle((double) gridAddressesRequest.getArguments()[3],
                (String) gridAddressesRequest.getArguments()[4], (double) gridAddressesRequest.getArguments()[5]);
        Point center = new Point((double) gridAddressesRequest.getArguments()[6], (double) gridAddressesRequest.getArguments()[7]);
        Point bottomLeft = new Point((double) gridAddressesRequest.getArguments()[8], (double) gridAddressesRequest.getArguments()[9]);
        double scale = (double) gridAddressesRequest.getArguments()[10];
        String network = ((NettingType) gridAddressesRequest.getArguments()[1]).name()+"::"+ gridAddressesRequest.getArguments()[2];
        gridAddressesRequest.getArguments()[0] = PngUtil
                .getGridAddressesPng(SpatialUtil
                                .getGridPoints(spatialService, "SUSTAINABLE_VERTICES::0", network),
                        new DrawingOption(drawingStyle, center, bottomLeft, scale));
        return (ResponseEntity<byte[]>) gridAddressesRequest.proceed();
    }

    @Wrap
    @SourceName("getGridLetters")
    public ResponseEntity<byte[]> wrapGridLetters(MethodInvocation gridLettersRequest) throws Throwable {
        DrawingStyle drawingStyle = new DrawingStyle((double) gridLettersRequest.getArguments()[2],
                (String) gridLettersRequest.getArguments()[3], (double) gridLettersRequest.getArguments()[4]);
        Point center = new Point((double) gridLettersRequest.getArguments()[5], (double) gridLettersRequest.getArguments()[6]);
        Point bottomLeft = new Point((double) gridLettersRequest.getArguments()[7], (double) gridLettersRequest.getArguments()[8]);
        double scale = (double) gridLettersRequest.getArguments()[9];
        Long semanticMapId = (Long) gridLettersRequest.getArguments()[1];
        gridLettersRequest.getArguments()[0] = PngUtil.getGridLettersPng(mapWordService.findAllBySemanticMapId(semanticMapId), new DrawingOption(drawingStyle, center, bottomLeft, scale));
        return (ResponseEntity<byte[]>) gridLettersRequest.proceed();
    }

}
