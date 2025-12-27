canvasWorker.plotter = {

    element : null ,

    canvasElement : null ,

    workerUrl : null ,

    dataUrl : null ,

    animationFrame : null ,

    renders : [ ] ,

    layers : [ ] ,

    dragStart : null ,

    worker : null ,

    translate : { x : 0 , y : 0 } ,

    dragged : false ,

    transformViewer : document.createElement ( 'div' ) ,

    canvas : function ( ) {
        if ( canvasWorker.plotter.canvas_ == undefined ) {
            canvasWorker.plotter.canvas_ = document.createElement('canvas') ;
            canvasWorker.plotter.canvas_.addEventListener ( "mousedown" , canvasWorker.plotter.onMouseDown , false ) ;
            canvasWorker.plotter.canvas_.addEventListener ( "mousemove" , canvasWorker.plotter.onMouseMove , false ) ;
            canvasWorker.plotter.canvas_.addEventListener ( "mouseup" , canvasWorker.plotter.onMouseUp , false ) ;
            canvasWorker.plotter.canvas_.addEventListener ( "DOMMouseScroll" , canvasWorker.plotter.onMouseScroll , false ) ;
            canvasWorker.plotter.canvas_.addEventListener ( "mousewheel" , canvasWorker.plotter.onMouseScroll , false ) ;
            canvasWorker.plotter.canvas_.setAttribute("style","width:100%; height:100%;")
        }
        canvasWorker.plotter.canvas_.width = canvasWorker.plotter.canvasElement.clientWidth;
        canvasWorker.plotter.canvas_.height = canvasWorker.plotter.canvasElement.clientHeight;
        return canvasWorker.plotter.canvas_ ;
    } ,

    init : function ( clientElement, element , workerUrl , dataUrl ) {
        canvasWorker.plotter.element = clientElement;
        canvasWorker.plotter.canvasElement = element;
        canvasWorker.plotter.workerUrl = workerUrl ;
        canvasWorker.plotter.dataUrl = dataUrl ;
        var canvas_ = canvasWorker.plotter.canvas ( ) ;
        element.appendChild ( canvas_ ) ;
        element.appendChild ( canvasWorker.plotter.transformViewer ) ;
        canvasWorker.plotter.hideTransformViewer ( ) ;
        canvasWorker.frameRequest.postMessage ( ) ;
        canvasWorker.animationFrame.raster ( ) ;
        canvasWorker.plotter.configWorker ( ) ;
        canvasWorker.plotter.resume ( ) ;
    } ,

    configWorker : function ( workerUrl , dataUrl ) {
        canvasWorker.plotter.worker = new Worker ( canvasWorker.plotter.workerUrl ) ;
        canvasWorker.plotter.worker.onmessage = canvasWorker.frameRequest.onmessage ;
        canvasWorker.plotter.worker.postMessage ( {
            config : 'layer' ,
            dataUrl : dataUrl ,
        }) ;
    } ,

    draw : function ( ) {
        var context_ = canvasWorker.plotter.canvas ( ).getContext("2d") ;
        canvasWorker.plotter.clean ( ) ;
        context_.drawImage ( canvasWorker.animationFrame.frame ( ) , 0 , 0 ) ;
    } ,

    pause : function ( ) {
        clearInterval( canvasWorker.plotter.animationFrame ) ;
        canvasWorker.plotter.animationFrame = null ;
    } ,

    resume : function ( ) {
        canvasWorker.plotter.animationFrame = setInterval ( canvasWorker.plotter.draw , 16 ) ;
    } ,

    clean : function ( ) {
        var canvas_ = canvasWorker.plotter.canvas ( );
        var context_ = canvas_.getContext ( '2d' ) ;
        context_.clearRect ( 0 , 0 , canvas_.width , canvas_.height ) ;
    } ,

    render : function ( name ) {
        for ( var i = 0 ; i < canvasWorker.plotter.renders.length ; i ++ ) {
            if ( canvasWorker.plotter.renders [ i ].name == name )
                return canvasWorker.plotter.renders [ i ] ;
        }
    } ,

    layer : function ( name ) {
        for ( var i = 0 ; i < canvasWorker.plotter.layers.length ; i ++ ) {
            if ( canvasWorker.plotter.layers [ i ].name == name )
                return canvasWorker.plotter.layers [ i ] ;
        }
    } ,

    point : function ( x , y ) {
        return canvasWorker.canvasTransform.windowPoint ( {
                                   x : x ,
                                   y : y ,
                                   } , canvasWorker.canvasTransform.boundingBox ( canvasWorker.plotter.canvas ( ) ) ) ;
    } ,

    onMouseDown : function ( event ) {
        canvasWorker.plotter.translate = { x : 0 , y : 0 } ;
        document.body.style.mozUserSelect = document.body.style.webkitUserSelect = document.body.style.userSelect = "none" ;
        canvasWorker.plotter.dragStart = canvasWorker.plotter.point ( event.offsetX , event.offsetY ) ;
        canvasWorker.plotter.dragged = false ;

    } ,

    onMouseUp : function ( event ) {
        if (  canvasWorker.plotter.translate.x != 0 &&
                        canvasWorker.plotter.translate.y != 0 ) {
            canvasWorker.canvasTransform.x += canvasWorker.plotter.translate.x ;
            canvasWorker.canvasTransform.y += canvasWorker.plotter.translate.y ;
            canvasWorker.plotter.translate = { x : 0 , y : 0 } ;
            canvasWorker.plotter.displayTransformViewer ( ) ;
            console.log ( 'translate \tx\t:' + canvasWorker.canvasTransform.x + ' \ty\t:' + canvasWorker.canvasTransform.y ) ;
        }
        canvasWorker.plotter.dragStart = null ;
        var point = canvasWorker.plotter.point ( event.offsetX , event.offsetY ) ;
        canvasWorker.plotter.element.$server.click ( point.x , point.y , canvasWorker.canvasTransform.scale ) ;
    } ,

    onMouseMove : function ( event ) {
        canvasWorker.plotter.dragged = true ;
        if ( canvasWorker.plotter.dragStart != null ) {
            var dragPoint = canvasWorker.plotter.point ( event.offsetX , event.offsetY ) ;
            canvasWorker.plotter.translate.x = dragPoint.x - canvasWorker.plotter.dragStart.x ;
            canvasWorker.plotter.translate.y = dragPoint.y - canvasWorker.plotter.dragStart.y ;
        }
    } ,

    onMouseScroll : function ( event ) {
        var delta = event.wheelDelta ? event.wheelDelta / 200 : event.detail ? - event.detail : 0 ;
        if ( Math.abs ( delta ) > 0.05  ) {
            canvasWorker.canvasTransform.scale = delta > 0 ?
                                canvasWorker.canvasTransform.scale + 0.2 : canvasWorker.canvasTransform.scale - 0.2 ;
            if ( canvasWorker.canvasTransform.scale < 0.2 ) {
                canvasWorker.canvasTransform.scale = 0.2 ;
            }
            canvasWorker.plotter.displayTransformViewer ( ) ;
            console.log ( 'scale' + canvasWorker.canvasTransform.scale ) ;
        }
        return event.preventDefault ( ) && false ;
    } ,

    displayTransformViewer : function ( ) {
        canvasWorker.plotter.hideTransformViewer ( ) ;
        canvasWorker.plotter.transformViewer.setAttribute( 'style' , 'position: absolute; width : 300px; height: 40px; ' +
                                'right: 5px; top: 5px; padding-left: 5px; margin: 5px; display: block; ' +
                                'background-color: #fff; box-shadow: 0 0 20px rgba(0, 0, 0, 0.3); border-style: solid; ') ;
        var paraS = document.createElement( 'P' );                       // Create a <p> node
        var tS = document.createTextNode('Scale\t:\t'+ canvasWorker.canvasTransform.scale.toFixed(1) +
                                          'Pan X\t:\t' + canvasWorker.canvasTransform.x.toFixed(2) +
                                             '\tY\t:'  + canvasWorker.canvasTransform.y.toFixed(2) );      // Create a text node
        paraS.appendChild(tS);                                          // Append the text to <p>
        canvasWorker.plotter.transformViewer.appendChild(paraS);           // Append <p> to <div> with id="myDIV"
    } ,

    hideTransformViewer : function ( ) {
        canvasWorker.plotter.transformViewer.setAttribute( 'style' , 'position: absolute; display: none;' ) ;
         while (canvasWorker.plotter.transformViewer.hasChildNodes()) {
              canvasWorker.plotter.transformViewer.removeChild(canvasWorker.plotter.transformViewer.firstChild);
         }
    } ,


}