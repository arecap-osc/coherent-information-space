package org.hkrdi.eden.ggm.media.v1.grid;

import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/ggm/media/api/v1/grid/")
public class GridEndpoint {

    @RequestMapping(value = "/frame/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getGridFrame(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                               @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                               @PathVariable double x, @PathVariable double y,
                                               @PathVariable double width, @PathVariable double height,
                                               @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/addresses/{nettingType}/{deep}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getGridAddresses(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                                   @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                                   @PathVariable double x, @PathVariable double y,
                                                   @PathVariable double width, @PathVariable double height,
                                                   @PathVariable double scale) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/letters/{semanticGridId}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getGridLetters(byte[] network, @PathVariable Long semanticGridId,
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
