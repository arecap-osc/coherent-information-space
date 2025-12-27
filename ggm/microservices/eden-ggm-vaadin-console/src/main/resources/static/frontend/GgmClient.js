self.onmessage = function ( event ) {
    if(event.data.applicationId != undefined) {
        applicationId = event.data.applicationId ;
        offscreenDrawing.name = event.data.name ;
        offscreenDrawing.selected = event.data.show ;
        offscreenDrawing.style = event.data.style ;
        offscreenDrawing.transform = event.data.transform ;
        offscreenDrawing.boundingBox = event.data.boundingBox ;
        setTimeout(requestLayer, 0);
        return ;
    }
    if(event.data.config != undefined) {
        offscreenDrawing.dataUrl = event.data.dataUrl ;
        if ( event.data.config == 'layer' ) {
            return ;
        }
    }

    offscreenDrawing.name = event.data.name;
    offscreenDrawing.selected = event.data.selected;
    offscreenDrawing.style = event.data.style ;
    offscreenDrawing.transform = event.data.transform ;
    offscreenDrawing.boundingBox = event.data.boundingBox ;
    setTimeout(requestNetwork, 0);
}

var applicationId = 0 ;

var frameMessages = [ ] ;

var offscreenDrawing = {

       dataUrl : null,


        name : null ,

        selected : false ,

        layer : null ,

        bound : null ,

        velocity : 0 ,

        frame : null ,

        totalFrames : null ,

        transform : {
            scale : 1 ,
            x : 0 ,
            y : 0
        } ,

        boundingBox : {
            width: 0 ,
            height: 0
        } ,

        style : {
            pen : 1 ,
            color : '#ffffff' ,
            alpha : 1
        } ,

        imageData : null ,

    } ;

function fetchFrameBitmap ( message ) {
    frameMessages.push ( message ) ;
    if ( frameMessages.length == 4 ) {
        self.postMessage ( frameMessages ) ;
        frameMessages = [ ] ;
    }
}

function requestNetwork() {
    var url = [ ] ;
    for ( var i = 1 ; i < 5 ; i ++ ) {
         url.push ( offscreenDrawing.dataUrl + 'network_frame/' +
                        offscreenDrawing.name.split ( '::' ) [ 0 ] + '/'  +  offscreenDrawing.name.split ( '::' ) [ 1 ] + '/' + i +
                        '/' + getDrawingRequest ( ) ) ;
    }
    fetch(url[0], { mode: 'cors' })
                       .then(response => response.blob())
                       .then(blob => createImageBitmap(blob))
                       .then(bitmap => {
                           fetchFrameBitmap ( {
                                name : offscreenDrawing.name ,
                                layer : 'ANIMATION_FRAME' ,
                                bound : 1 ,
                                frame : 1 ,
                                totalFrames : 4 ,
                                imageData :  bitmap
                            }  );
                       });
    fetch(url[1], { mode: 'cors' })
                       .then(response => response.blob())
                       .then(blob => createImageBitmap(blob))
                       .then(bitmap => {
                           fetchFrameBitmap ( {
                                name : offscreenDrawing.name ,
                                layer : 'ANIMATION_FRAME' ,
                                bound : 1 ,
                                frame : 2 ,
                                totalFrames : 4 ,
                                imageData :  bitmap
                            }  );
                       });
    fetch(url[2], { mode: 'cors' })
                       .then(response => response.blob())
                       .then(blob => createImageBitmap(blob))
                       .then(bitmap => {
                           fetchFrameBitmap ( {
                                name : offscreenDrawing.name ,
                                layer : 'ANIMATION_FRAME' ,
                                bound : 1 ,
                                frame : 3 ,
                                totalFrames : 4 ,
                                imageData :  bitmap
                            }  );
                       });
    fetch(url[3], { mode: 'cors' })
                       .then(response => response.blob())
                       .then(blob => createImageBitmap(blob))
                       .then(bitmap => {
                           fetchFrameBitmap ( {
                                name : offscreenDrawing.name ,
                                layer : 'ANIMATION_FRAME' ,
                                bound : 1 ,
                                frame : 4 ,
                                totalFrames : 4 ,
                                imageData :  bitmap
                            }  );
                       });

}

function fetchBitmap ( message ) {
    self.postMessage ( message ) ;
}


function requestLayer ( ) {
    if(gridId != 0) {
        if ( offscreenDrawing.selected ) {
            var url = offscreenDrawing.dataUrl + 'grid/' + offscreenDrawing.name + '/' + gridId + '/' + getDrawingRequest ( ) ;
            fetch(url, { mode: 'cors' })
                               .then(response => response.blob())
                               .then(blob => createImageBitmap(blob))
                               .then(bitmap => {
                                   fetchBitmap ( {
                                        name : offscreenDrawing.name ,
                                        bound : 1 ,
                                        noFrame : true ,
                                        frame : 0 ,
                                        totalFrames : 0 ,
                                        imageData :  bitmap
                                    }  );
                               });
        } else {
            fetchBitmap ( {
                name : layer ,
                noFrame : true ,
            }  );
        }
    }
}

function getDrawingRequest ( ) {
    return offscreenDrawing.style.pen.toFixed(2) +
            '/' + offscreenDrawing.style.color.replace ( '#' , '' ) +
            '/' + offscreenDrawing.style.alpha.toFixed(2) + '/' + offscreenDrawing.transform.x.toFixed(2) +
            '/' +  offscreenDrawing.transform.y.toFixed(2) + '/' + offscreenDrawing.boundingBox.width +
            '/' + offscreenDrawing.boundingBox.height + '/' + ( offscreenDrawing.transform.scale / 150000 ).toFixed(7) ;
}

function base64toBlob(base64Data, contentType) {
    contentType = contentType || '';
    var sliceSize = 1024;
    var byteCharacters = atob(base64Data);
    var bytesLength = byteCharacters.length;
    var slicesCount = Math.ceil(bytesLength / sliceSize);
    var byteArrays = new Array(slicesCount);

    for (var sliceIndex = 0; sliceIndex < slicesCount; ++sliceIndex) {
        var begin = sliceIndex * sliceSize;
        var end = Math.min(begin + sliceSize, bytesLength);

        var bytes = new Array(end - begin);
        for (var offset = begin, i = 0; offset < end; ++i, ++offset) {
            bytes[i] = byteCharacters[offset].charCodeAt(0);
        }
        byteArrays[sliceIndex] = new Uint8Array(bytes);
    }
    return new Blob(byteArrays, { type: contentType });
}

