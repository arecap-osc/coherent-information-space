canvasWorker.rasterAssembly = {

    frameCanvas : function ( millis ) {
        var canvas_ = new OffscreenCanvas ( canvasWorker.animationFrame.width ( ) ,
                                canvasWorker.animationFrame.height ( ) ) ;
        canvasWorker.rasterAssembly.assembly ( canvas_ , millis ) ;
        return canvas_ ;
    } ,

    assembly : function ( canvas , millis ) {
        var n = 0 ;
        for ( var i = 0 ; i < canvasWorker.plotter.renders.length ; i ++ ) {
            var render_ = canvasWorker.plotter.renders [ i ] ;
            if ( render_.selected ) {
                for ( var j = 0 ; j < canvasWorker.animationFrame.layers.length ; j ++ ) {
                    var layer_ = canvasWorker.animationFrame.layers[ j ] ;
                    if ( render_.name == layer_.name && canvasWorker.rasterAssembly.drawLayer ( layer_ , millis ) ) {
                        canvas.getContext ( '2d' ).drawImage ( layer_.canvas , 0 , 0 ) ;
                        n ++ ;
                    }
                }
            }
        }
        for ( var i = 0 ; i < canvasWorker.plotter.layers.length ; i ++ ) {
            var pLayer_ = canvasWorker.plotter.layers [ i ] ;
            if ( pLayer_.selected ) {
                for ( var j = 0 ; j < canvasWorker.animationFrame.layers.length ; j ++ ) {
                    var layer_ = canvasWorker.animationFrame.layers [ j ] ;
                    if ( pLayer_.name == layer_.name && canvasWorker.rasterAssembly.drawLayer ( layer_ , millis ) ) {
                        canvas.getContext ( '2d' ).drawImage ( layer_.canvas , 0 , 0 ) ;
                        n ++ ;
                    }
                }
            }
        }

//        if ( millis > 25 ) {
//            console.log ( 'delta: ' + millis ) ;
//            console.log ( 'n: ' + n ) ;
//        }
    } ,

    drawLayer : function ( layer , millis ) {
        var cntAnimationFrames = 0 ;
        if ( layer.totalFrames > 0) {
            var drawings_ =  canvasWorker.frameRequest.renderDrawings ( layer.name ) ;
            for ( var i = 0 ; i < drawings_.length ; i ++) {
                if ( drawings_ [ i ].layer == layer.id) {
                    cntAnimationFrames ++ ;
                }
            }
        }
        return layer.totalFrames == 0 || ( cntAnimationFrames == layer.totalFrames && layer.frame  == canvasWorker.rasterAssembly.frame ( layer , millis ) );
    } ,

    frame : function ( layer , millis ) {
        layer.delta +=  millis ;

        if (  layer.velocity  <= layer.delta   ) {
            layer.delta = 0 ;
        }
        return ( ( layer.delta / ( layer.velocity / layer.totalFrames ) ) | 0 ) + 1 ; ;
    } ,

}