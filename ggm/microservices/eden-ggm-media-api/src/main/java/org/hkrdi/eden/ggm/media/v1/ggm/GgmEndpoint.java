package org.hkrdi.eden.ggm.media.v1.ggm;

import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.hkrdi.eden.ggm.media.v1.PngFrame;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/ggm/media/api/v1/")
@Deprecated
public class GgmEndpoint {


    @RequestMapping(value = "/network/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getNetwork(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                             @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                             @PathVariable double x, @PathVariable double y,
                                             @PathVariable double width, @PathVariable double height,
                                             @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/network_frames/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getNetworkFrames(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                             @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                             @PathVariable double x, @PathVariable double y,
                                             @PathVariable double width, @PathVariable double height,
                                             @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/network_frame/{nettingType}/{deep}/{frame}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getNetworkFrame(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep, @PathVariable int frame,
                                             @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                             @PathVariable double x, @PathVariable double y,
                                             @PathVariable double width, @PathVariable double height,
                                             @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/anetwork/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getNetworkAnimationFrame(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                                  @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                                  @PathVariable double x, @PathVariable double y,
                                                  @PathVariable double width, @PathVariable double height,
                                                  @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/anetwork2/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<PngFrame> getNetworkAnimationFrame2(PngFrame network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                                              @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                                              @PathVariable double x, @PathVariable double y,
                                                              @PathVariable double width, @PathVariable double height,
                                                              @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<PngFrame>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/network_indices/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getNetworkIndices(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                                  @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                                  @PathVariable double x, @PathVariable double y,
                                                  @PathVariable double width, @PathVariable double height,
                                                  @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

}
