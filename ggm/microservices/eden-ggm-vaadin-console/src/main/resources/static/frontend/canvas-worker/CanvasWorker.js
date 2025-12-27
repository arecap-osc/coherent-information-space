var canvasWorker = {

    clear : function ( ) {
        canvasWorker.plotter.renders.clear ( );
    } ,

    order : function ( renders ) {
        canvasWorker.plotter.renders = [ ];
        var renders_ = JSON.parse( renders ) ;
        for ( var i = 0 ; i < renders_.length ; i ++ ) {
            canvasWorker.plotter.renders.push ( {
                name : renders_ [ i ].name ,
                selected : renders_ [ i ].selected ,
                style: {
                        pen : renders_ [ i ].pen ,
                        color : renders_ [ i ].color ,
                        alpha : renders_ [ i ].alpha
                    } ,
                requestPathVariable : renders_ [ i ].requestPathVariable ,
                totalFrames : renders_ [ i ].totalFrames == undefined ? 0 : totalFrames ,
                velocity : renders_ [ i ].velocity == undefined ? 0 : velocity
            } );
        }
        canvasWorker.animationFrame.draw = true ;

    } ,

    configureLayer : function ( name , selected , pen , color , alpha , requestPathVariable , totalFrames , velocity ) {
        canvasWorker.animationFrame.draw = true ;
        if ( canvasWorker.plotter.render ( name ) == undefined ) {
            canvasWorker.plotter.renders.push ( {
                name : name ,
                selected : selected ,
                style: {
                        pen : pen ,
                        color : color ,
                        alpha : alpha
                    } ,
                requestPathVariable : requestPathVariable ,
                totalFrames : totalFrames == undefined ? 0 : totalFrames ,
                velocity : velocity == undefined ? 0 : velocity
            } );
            canvasWorker.frameRequest.requestWorker ( name ) ;
        }
        render = canvasWorker.plotter.render ( name ) ;
        render.selected = selected ;
        render.style.pen = pen ;
        render.style.color = color ;
        render.style.alpha = alpha ;
        render.requestPathVariable = requestPathVariable ;
        render.totalFrames = totalFrames == undefined ? 0 : totalFrames ;
        render.velocity = velocity == undefined ? 0 : velocity ;
    } ,

} ;
