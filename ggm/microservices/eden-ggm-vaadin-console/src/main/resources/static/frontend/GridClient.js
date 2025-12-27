self.onmessage = function ( event ) {
    if(event.data.gridId != undefined) {
        gridId = event.data.gridId ;
        offscreenDrawing.name = event.data.name ;
        offscreenDrawing.selected = event.data.show ;
        offscreenDrawing.style = event.data.style ;
        offscreenDrawing.transform = event.data.transform ;
        offscreenDrawing.boundingBox = event.data.boundingBox ;
        setTimeout ( requestLayer , 0) ;
        return ;
    }
    if(event.data.config != undefined) {
        offscreenDrawing.dataUrl = event.data.dataUrl ;
        if ( event.data.config == 'layer' ) {
            return ;
        }
    }
    offscreenDrawing.name = event.data.name ;
    offscreenDrawing.selected = event.data.selected ;
    offscreenDrawing.style = event.data.style ;
    offscreenDrawing.transform = event.data.transform ;
    offscreenDrawing.boundingBox = event.data.boundingBox ;
    setTimeout ( requestNetwork , 0) ;
}

var gridId = 0 ;

var frameMessages = [ ] ;

var addressesMessages = [ ] ;

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
    if ( frameMessages.length == 2 ) {
        var ordered = [ ] ;
        for( var i = 0 ; i < frameMessages.length ; i++ ) {
            if ( frameMessages [ i ].layer == 'GRID_FRAME' ) {
                ordered.push( frameMessages [ i ] ) ;
            }
        }
        for( var i = 0 ; i < frameMessages.length ; i++ ) {
            if ( frameMessages [ i ].layer != 'GRID_FRAME' ) {
                ordered.push( frameMessages [ i ] ) ;
            }
        }
        self.postMessage ( ordered ) ;
        frameMessages = [ ] ;
    }
}


function fetchBitmap ( message ) {
    addressesMessages.push ( message ) ;
    if ( addressesMessages.length == 1 ) {
        self.postMessage ( addressesMessages ) ;
        addressesMessages = [ ] ;
    }
}

function requestNetwork() {
    if ( offscreenDrawing.name == 'SUSTAINABLE_VERTICES::0' ) {
        var frameUrl = offscreenDrawing.dataUrl + 'grid/frame/' + getDrawingRequest ( ) ;
        var sustainableUrl = offscreenDrawing.dataUrl + 'grid/addresses/SUSTAINABLE_VERTICES/0/' + getDrawingRequest ( ) ;
        var metabolicUrl = offscreenDrawing.dataUrl + 'grid/addresses/METABOLIC_VERTICES/0/'  + getDrawingRequest ( ) ;
        fetch(frameUrl, { mode: 'cors' })
                           .then(response => response.blob())
                           .then(blob => createImageBitmap(blob))
                           .then(bitmap => {
                               fetchFrameBitmap ( {
                                    name : offscreenDrawing.name ,
                                    layer : 'GRID_FRAME' ,
                                    bound : 1 ,
                                    frame : 0 ,
                                    totalFrames : 0 ,
                                    imageData :  bitmap
                                }  );
                           });
        fetch(sustainableUrl, { mode: 'cors' })
                           .then(response => response.blob())
                           .then(blob => createImageBitmap(blob))
                           .then(bitmap => {
                               fetchFrameBitmap ( {
                                    name : offscreenDrawing.name ,
                                    layer : 'SUSTAINABLE_GRID_FRAME' ,
                                    bound : 1 ,
                                    frame : 0 ,
                                    totalFrames : 0 ,
                                    imageData :  bitmap
                                }  );
                           });
    } else {
        var url = offscreenDrawing.dataUrl + 'grid/addresses/' + offscreenDrawing.name.split ( '::' ) [ 0 ] +
                        '/'  + offscreenDrawing.name.split ( '::' ) [ 1 ] + '/' + getDrawingRequest ( ) ;
        fetch(url, { mode: 'cors' })
                           .then(response => response.blob())
                           .then(blob => createImageBitmap(blob))
                           .then(bitmap => {
                               fetchBitmap ( {
                                    name : offscreenDrawing.name ,
                                    layer : 'GRID_ADDRESSES' ,
                                    bound : 1 ,
                                    frame : 0 ,
                                    totalFrames : 0 ,
                                    imageData :  bitmap
                                }  );
                           });
    }
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

