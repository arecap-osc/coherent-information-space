canvasWorker.frameRequest = {

    drawings : [ ] ,

    workers : { } ,

    stackCnt : 0 ,

    requests : [ ] ,

    onmessage : function ( event ) {
        canvasWorker.frameRequest.stackCnt -- ;
        var curr = new Date ( ).getTime ( ) ;
        var drawings_ = event.data ;
        for ( var i = 0 ; i < drawings_.length ; i ++ ) {
            if ( drawings_[ i ].noFrame != undefined && drawings_[ i ].noFrame ) {
                canvasWorker.frameRequest.stackCnt ++ ;
            }
            var drawing_ = canvasWorker.frameRequest
                                .eventDrawing ( drawings_ [ i ].name , drawings_ [ i ].layer , drawings_ [ i ].bound , drawings_ [ i ].frame ,
                                 drawings_ [ i ].totalFrames == undefined ? 0 : drawings_ [ i ].totalFrames ) ;

            drawing_.canvas = new OffscreenCanvas ( canvasWorker.animationFrame.width ( ) ,
                                                                       canvasWorker.animationFrame.height ( ) ) ;
            drawing_.canvas.getContext ( '2d' ).drawImage ( drawings_ [ i ].imageData , 0 , 0 ) ;
            canvasWorker.animationFrame.draw = true ;
        }
        console.log ( 'onmessage time out ' + canvasWorker.frameRequest.stackCnt +'\t: ' , (new Date ( ).getTime ( ) - curr ) );
    } ,

    postMessage : function ( ) {
        if ( canvasWorker.frameRequest.stackCnt == 0 ) {
            var noRequest = true ;
            if ( canvasWorker.frameRequest.requests.length > 0 ) {
                for ( var i = 0 ; i < canvasWorker.frameRequest.requests.length; i ++ ) {
                    var name = canvasWorker.frameRequest.requests [ i ];
                    var render_ = canvasWorker.plotter.render ( name ) ;
                    if ( render_.selected ) {
                        canvasWorker.frameRequest.requests.splice ( i , 1 ) ;
                        canvasWorker.frameRequest.requestWorker ( name ) ;
                        noRequest = false;
                        break;
                    }
                }
            }
            if ( noRequest ) {
                for ( var i = canvasWorker.plotter.renders.length - 1 ; i >= 0 ; i -- ) {
                    var render_ = canvasWorker.plotter.renders[ i ] ;
                    if ( render_.selected ) {
                        var drawings_ = canvasWorker.frameRequest.renderDrawings ( render_.name );
                        if ( drawings_.length > 0 && canvasWorker.frameRequest.transform ( drawings_ [ 0 ] ) ) {
                            for ( var j = 0 ; j < drawings_.length ; j ++ ) {
                                var drawing_ = drawings_[ j ];
                                drawing_.name = render_.name ;
                                drawing_.transform = canvasWorker.frameRequest.drawingTransform ( ) ;
                                drawing_.boundingBox = canvasWorker.frameRequest.drawingBoundingBox ( ) ;
                                drawing_.to = 10 ;
                                drawing_.canvas = null ;
                                drawing_.style =  {
                                        pen : render_.style.pen ,
                                        color : render_.style.color ,
                                        alpha : render_.style.alpha
                                    } ;
                                drawing_.interaction = canvasWorker.plotter.interaction ;
                            }
                            canvasWorker.frameRequest.stackCnt ++ ;
                            canvasWorker.frameRequest.workers[ render_.name ].postMessage ( drawings_ [ 0 ] ) ;
                            break;
                        }
                    }
                }
                if (canvasWorker.frameRequest.stackCnt == 0 ) {
                    canvasWorker.plotter.hideTransformViewer ( ) ;
                }
            }
        }
        setTimeout( canvasWorker.frameRequest.postMessage , 5 );
    } ,

    requestWorker : function ( name ) {
        name = '' + name ;
        if ( canvasWorker.frameRequest.stackCnt == 0 ) {
            if ( canvasWorker.frameRequest.workers [ name ] == undefined ) {
                var render_ = canvasWorker.plotter.render ( name ) ;
                canvasWorker.frameRequest.workers [ name ] = new Worker ( canvasWorker.plotter.workerUrl ) ;
                canvasWorker.frameRequest.workers [ name ].onmessage = canvasWorker.frameRequest.onmessage ;
                canvasWorker.frameRequest.stackCnt ++ ;
                canvasWorker.frameRequest.workers [ name ].postMessage (
                        {
                            config : 'config' ,
                            to : 10 ,
                            dataUrl : canvasWorker.plotter.dataUrl ,
                            name: name ,
                            selected : render_.selected ,
                            style : {
                                pen : render_.style.pen ,
                                color : render_.style.color ,
                                alpha : render_.style.alpha
                            } ,
                            transform : canvasWorker.frameRequest.drawingTransform ( ) ,
                            boundingBox : canvasWorker.frameRequest.drawingBoundingBox ( )
                        } ) ;
            }
            return canvasWorker.frameRequest.workers [ name ] ;
        } else {
            canvasWorker.frameRequest.requests.push ( name ) ;
        }
    } ,

    renderDrawings : function ( name ) {
        var drawings_ = [ ] ;
        for ( var i = 0 ; i < canvasWorker.frameRequest.drawings.length ; i ++ ) {
            if ( canvasWorker.frameRequest.drawings[ i ].name == name ) {
                drawings_.push ( canvasWorker.frameRequest.drawings[ i ] ) ;
            }
        }
        return drawings_ ;
    } ,

    eventDrawing : function ( name , layer , bound , frame , totalFrames ) {
        for ( var i = 0 ; i < canvasWorker.frameRequest.drawings.length ; i ++ ) {
            var drawing_ = canvasWorker.frameRequest.drawings[ i ] ;
            if ( drawing_.name == name && drawing_.layer == layer
                    && drawing_.bound == bound && drawing_.frame == frame ) {
                return drawing_ ;
            }
        }
        var render_ = canvasWorker.plotter.render ( name ) ;
        var drawing_ = {
                           name : name ,
                           layer : layer,
                           bound : bound ,
                           frame : frame ,
                           velocity : 1600 ,
                           totalFrames : totalFrames ,
                           transform : canvasWorker.frameRequest.drawingTransform ( ) ,
                           boundingBox : canvasWorker.frameRequest.drawingBoundingBox ( ) ,
                           style : {
                                pen : render_.style.pen ,
                                color : render_.style.color ,
                                alpha : render_.style.alpha
                           } ,
                           canvas : null ,
                       } ;
        canvasWorker.frameRequest.drawings.push ( drawing_ ) ;
        return  drawing_ ;
    } ,

    transform : function ( drawing ) {
        var render_ = canvasWorker.plotter.render ( drawing.name ) ;
        var boundingBox_ = canvasWorker.frameRequest.drawingBoundingBox ( ) ;
        return drawing.transform.scale != canvasWorker.canvasTransform.scale
                || drawing.transform.x != canvasWorker.canvasTransform.x
                || drawing.transform.y != canvasWorker.canvasTransform.y
                || drawing.style.pen != render_.style.pen
                || drawing.style.color != render_.style.color
                || drawing.style.alpha != render_.style.alpha
                || drawing.boundingBox.width != boundingBox_.width
                || drawing.boundingBox.height != boundingBox_.height;
//                || render_.transform ;

    } ,

    drawingTransform : function ( ) {
        return {
                   scale : canvasWorker.canvasTransform.scale ,
                   x : canvasWorker.canvasTransform.x ,
                   y : canvasWorker.canvasTransform.y
               } ;
    } ,

    drawingBoundingBox : function ( ) {
        return {
                    width : canvasWorker.plotter.canvasElement.offsetWidth ,
                    height : canvasWorker.plotter.canvasElement.offsetHeight
               } ;
    } ,

}