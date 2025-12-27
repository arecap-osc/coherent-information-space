package org.hkrdi.eden.ggm.media.coherentspace;

import org.hkrdi.eden.ggm.algebraic.netting.NettingType;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/ggm/media/api/v1/coherentspace/")
public class HexavalentNetworkEndpoint {


    //todo endpoint with one jti

    @RequestMapping(value = "layer/{nettingType}/{deep}/{addressIndex}/{trivalentLogic}/{clusterIndex}/{clusterType}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}/{jti}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getHexavalentNetwork(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                                   @PathVariable boolean addressIndex, @PathVariable boolean trivalentLogic,
                                                   @PathVariable boolean clusterIndex, @PathVariable boolean clusterType,
                                                   boolean book,
                                                   @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                                   @PathVariable double x, @PathVariable double y,
                                                   @PathVariable double width, @PathVariable double height,
                                                   @PathVariable double scale, @PathVariable String jti,
                                                   @RequestParam Map<String,String> selectedDataMapIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "layer/{nettingType}/{deep}/{addressIndex}/{trivalentLogic}/{clusterIndex}/{clusterType}/{book}/{pen}/{color}/{alpha}/{x}/{y}/{width}/{height}/{scale}/{jti}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getHexavalentNetworkBook(byte[] network, @PathVariable NettingType nettingType, @PathVariable int deep,
                                                   @PathVariable boolean addressIndex, @PathVariable boolean trivalentLogic,
                                                   @PathVariable boolean clusterIndex, @PathVariable boolean clusterType,
                                                   @PathVariable boolean book,
                                                   @PathVariable double pen, @PathVariable String color, @PathVariable double alpha,
                                                   @PathVariable double x, @PathVariable double y,
                                                   @PathVariable double width, @PathVariable double height,
                                                   @PathVariable double scale, @PathVariable String jti,
                                                   @RequestParam Map<String,String> selectedDataMapIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<byte[]>(network, headers, HttpStatus.OK);
    }
}
