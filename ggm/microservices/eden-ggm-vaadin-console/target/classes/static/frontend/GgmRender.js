var ggmRender =  {

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
                    }
            } );
        }
        canvasWorker.animationFrame.draw = true ;

    } ,

    configureRender : function ( name , selected , pen , color , alpha ) {
        canvasWorker.animationFrame.draw = true ;
        if ( canvasWorker.plotter.render ( name ) == undefined ) {
            canvasWorker.plotter.renders.push ( {
                name : name ,
                selected : selected ,
                style: {
                        pen : pen ,
                        color : color ,
                        alpha : alpha
                    }
            } );
            canvasWorker.frameRequest.requestWorker ( name ) ;
        }
        render = canvasWorker.plotter.render ( name ) ;
        render.selected = selected ;
        render.style.pen = pen ;
        render.style.color = color ;
        render.style.alpha = alpha ;
    } ,

    configure : function ( renders ) {
        renders = JSON.parse ( renders ) ;
        for ( var i = 0 ; i < renders.length ; i ++ ) {
            ggmRender.configureRender ( renders [ i ].name , renders [ i ].selected ,
                    renders [ i ].pen , renders [ i ].color ,  renders [ i ].alpha ) ;
        }
    } ,

} ;