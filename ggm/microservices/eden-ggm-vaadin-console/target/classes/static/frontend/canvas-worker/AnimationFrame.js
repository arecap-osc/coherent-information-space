canvasWorker.animationFrame = {

    draw : true ,

    layers : [ ] ,

    delta : 0 ,

//    render : 0 ,

    frame : function ( ) {
        var curr = new Date ( ).getTime ( ) ;
//        console.log (curr - canvasWorker.animationFrame.delta ) ;
        var canvas_  = canvasWorker.rasterAssembly.frameCanvas ( curr - canvasWorker.animationFrame.delta ) ;
        canvasWorker.animationFrame.delta = curr ;
        return canvas_ ;
    } ,

    raster : function ( ) {
        if ( canvasWorker.animationFrame.draw ) {
            var last = new Date().getTime();
//            canvasWorker.plotter.pause ( ) ;
            for ( var i = 0 ; i < canvasWorker.plotter.renders.length ; i ++ ) {
                var render_ = canvasWorker.plotter.renders [ i ] ;
//                canvasWorker.animationFrame.render ++ ;
                var drawings_ = canvasWorker.frameRequest.renderDrawings ( render_.name ) ;
                for ( var j = 0 ; j < drawings_.length ; j ++ ) {
                    if ( drawings_ [ j ].canvas != null ) {
                        canvasWorker.animationFrame.layer ( drawings_ [ j ] ) ;
                    }
                }
                canvasWorker.animationFrame.draw = false ;
            }
            for ( var i = 0 ; i < canvasWorker.plotter.layers.length ; i ++ ) {
                var layer_ = canvasWorker.plotter.layers [ i ] ;
                var drawings_ = canvasWorker.frameRequest.renderDrawings ( layer_.name ) ;
                for ( var j = 0 ; j < drawings_.length ; j ++ ) {
                    if ( drawings_ [ j ].canvas != null ) {
                        canvasWorker.animationFrame.layer ( drawings_ [ j ] ) ;
                    }
                }
                canvasWorker.animationFrame.draw = false ;
            }
//            canvasWorker.plotter.resume ( ) ;
        }
        setTimeout ( canvasWorker.animationFrame.raster , 10 ) ;
    } ,



    layer : function ( drawing ) {
        var layer_ = canvasWorker.animationFrame.layerConfiguration ( drawing ) ;
            if ( drawing.canvas != null ) {
                layer_.canvas.getContext ( '2d' ).drawImage ( drawing.canvas , 0 , 0 ) ;
            } else {
                layer_.canvas.getContext ( '2d' ).clearRect ( 0 , 0 , layer_.canvas.width , layer_.canvas.height ) ;
            }
        return layer_ ;
    } ,

    layerConfiguration : function ( drawing ) {
        for ( var i = 0 ; i < canvasWorker.animationFrame.layers.length ; i ++ ) {
            var layer_ = canvasWorker.animationFrame.layers [ i ] ;
            if ( drawing.layer == layer_.id && drawing.frame == layer_.frame && drawing.name == layer_.name  ) {
                layer_.canvas.width = canvasWorker.animationFrame.width ( ) ;
                layer_.canvas.height = canvasWorker.animationFrame.height ( ) ;
                layer_.delta = 0 ;
                return layer_ ;
            }
        }
        var layer_ = canvasWorker.animationFrame.initLayer ( drawing ) ;
        canvasWorker.animationFrame.layers.push ( layer_ ) ;
        return layer_ ;
    } ,

    initLayer : function ( drawing ) {
        return {
                     name : drawing.name ,
                     id : drawing.layer ,
                     frame : drawing.frame ,
                     delta : 0 ,
                     totalFrames : drawing.totalFrames ,
                     velocity : drawing.velocity ,
                     canvas : new OffscreenCanvas ( canvasWorker.animationFrame.width ( ) ,
                                                              canvasWorker.animationFrame.height ( ) )
                 } ;
    } ,

    width : function ( ) {
        return canvasWorker.plotter.canvasElement.offsetWidth ;
    } ,

    height : function ( ) {
        return canvasWorker.plotter.canvasElement.offsetHeight ;
    } ,


}