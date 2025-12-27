
self.onmessage = function ( event ) {
    if(event.data.config != undefined) {
        offscreenDrawing.dataUrl = event.data.dataUrl ;
        offscreenDrawing.name = event.data.name;
        offscreenDrawing.selected = event.data.selected;
        offscreenDrawing.style = event.data.style ;
        offscreenDrawing.transform = event.data.transform ;
        offscreenDrawing.to = event.data.to ;
        offscreenDrawing.boundingBox = event.data.boundingBox ;
        canvasTransform.scale = offscreenDrawing.transform.scale ;
        canvasTransform.x = offscreenDrawing.transform.x ;
        canvasTransform.y = offscreenDrawing.transform.y ;
        setTimeout(requestNetwork, 0);
    } else {
//        if ( topology.request == 60 ) {
                var curr = new Date ( ).getTime ( ) ;

            offscreenDrawing = event.data;
            canvasTransform.scale = offscreenDrawing.transform.scale ;
            canvasTransform.x = offscreenDrawing.transform.x ;
            canvasTransform.y = offscreenDrawing.transform.y ;
//            console.log(offscreenDrawing);
            render ( ) ;
                    console.log ( 'worker time out : ' , (new Date ( ).getTime ( ) - curr ) );

//        }
    }
}

var messages = [ ] ;

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


var topology = {

    data : [ ] ,

    request : 0 ,

 } ;

var canvasTransform = {

    scale : 1 ,

    x : 0 ,

    y : 0 ,

    jitter : 256 ,

    boundingBox : function ( canvas ) {
        return {
            width : canvas.width ,
            height : canvas.height ,
        }
    } ,

    canvasPoint : function ( point , boundingBox ) {
                return { x : point.x * canvasTransform.scale + canvasTransform.x + boundingBox.width / 2 ,
                        y : point.y * canvasTransform.scale + canvasTransform.y + boundingBox.height / 2 }
    } ,

    windowPoint : function( point , boundingBox ) {
                return { x : point.x - canvasTransform.x - boundingBox.width / 2 ,
                        y : point.y - canvasTransform.y - boundingBox.height / 2 }
    } ,

    splitPoint : function ( head , tail , part , total ) {
        return { x: head.x + ( tail.x - head.x ) / total * part ,
                y: head.y + ( tail.y - head.y ) / total * part }
    } ,

}

var colorAdj = {

    rgb : function ( color ) {
        return { r : parseInt ( "0x" + color[ 1 ] + color[ 2 ] , 16 ) ,
                 g : parseInt ( "0x" + color[ 3 ] + color[ 4 ] , 16 ) ,
                 b : parseInt ( "0x" + color[ 5 ] + color[ 6 ] , 16 ) }
    } ,

    blend : function ( color ) {
        var rgb = colorAdj.rgb ( color ) ;
        return { rgb : rgb ,
                 r : rgb.r ,
                 g : rgb.g ,
                 b : rgb.b ,
                 mix : Math.max ( rgb.r , rgb.g , rgb.b ) + Math.min ( rgb.r , rgb.g , rgb.b ) }
    } ,

    complementary : function ( color ) {
        var blend = colorAdj.blend ( color ) ;
        return "#" + ( blend.mix - blend.r ).toString( 16 ) +
                     ( blend.mix - blend.g ).toString( 16 ) +
                     ( blend.mix - blend.b ).toString( 16 ) ;
    } ,

    desaturate : function ( color ) {
        var blend = colorAdj.blend ( color ) ;
        return "#" + ( blend.mix ).toString( 16 ) +
                     ( blend.mix ).toString( 16 ) +
                     ( blend.mix ).toString( 16 ) ;
    } ,

}

var xyCtx = {

    line : function ( ctx , head , tail ) {
        ctx.moveTo ( head.x , head.y ) ;
        ctx.lineTo ( tail.x , tail.y ) ;
    } ,

    point : function ( ctx , point , radius ) {
        ctx.arc ( point.x , point.y , radius , 0 , 2 * Math.PI , false );
    } ,

    stroke : function ( ctx , vectors ) {
        for ( var i = 0 ; i < vectors.length ; i++ ) {
            xyCtx.line ( ctx , vectors[ i ].head , vectors[ i ].tail );
        }
        ctx.stroke ( ) ;
    } ,

    fill : function ( ctx , points, radius ) {
        for ( var i = 0 ; i < points.length ; i++ ) {
            xyCtx.point ( ctx , points[ i ] , radius );
        }
        ctx.fill ( ) ;
    } ,

    strokeFill : function ( ctx , points, radius ) {
        for ( var i = 0 ; i < points.length ; i++ ) {
            ctx.beginPath ( ) ;
            xyCtx.point ( ctx , points[ i ] , radius );
            ctx.fill ( ) ;
            ctx.stroke ( ) ;
            ctx.closePath ( ) ;
        }
    }

}

function requestNetwork() {
//    var name_ = offscreenDrawing.name.includes ( 'SUSTAINABLE::' ) ?
//                                offscreenDrawing.name.replace ( 'SUSTAINABLE::' , 'SUSTAINABLE_VERTICES::' ) :
//                                ( offscreenDrawing.name.includes ('METABOLIC::') ?
//                                    offscreenDrawing.name.replace ('METABOLIC::' , 'METABOLIC_VERTICES::' ) :
//                                        offscreenDrawing.name ) ;
//    for ( var i = 0 ; i < 60 ; i ++ ) {
//        var reqDomain = '' + ( i + 1 ) ;
        fetch(offscreenDrawing.dataUrl  + 'topology/'+
                                                    offscreenDrawing.name.replace ( '::' , '/' ) +
                                                   '/0')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            validateExchange ( myJson ) ;
        });
//    }

}


function validateExchange ( topologyData ) {

//    topology.request ++ ;
//    for ( var i = 0 ; i < topologyData.length ; i ++  ) {
//        var push = true;
//        for ( var j = 0 ; j < topology.data.length ; j++ ) {
//            if ( topologyData [ i ].index == topology.data [ j ].index) {
//                push = false ;
//                break ;
//            }
//        }
//        if ( push ) {
//            topology.data.push ( topologyData [ i ] ) ;
//        }
//    }

//    if ( topology.request == 60 ) {
        topology.data = topologyData ;

        render ( ) ;
//    }

}


function render ( ) {
   connectorsFrame ( ) ;
   vertexCanvas ( ) ;
       connectorAnimationFrame ( 1 ) ;
       connectorAnimationFrame ( 2 ) ;
       connectorAnimationFrame ( 3 ) ;
       connectorAnimationFrame ( 4 ) ;
   setTimeout (  function () {
       self.postMessage ( messages ) ;

            } ,  15 ) ;
//   connectorAnimationFrame ( 2 ) ;
//   connectorAnimationFrame ( 3 ) ;
//   connectorAnimationFrame ( 4 ) ;
//    setTimeout ( connectorsFrame ( ) , offscreenDrawing.to) ;
//    setTimeout ( vertexCanvas ( ) , offscreenDrawing.to + 500 ) ;
//    setTimeout ( function ( ) {
//            connectorAnimationFrame ( 1 )
//        } , offscreenDrawing.to + 1000 );
//    setTimeout ( function ( ) {
//            connectorAnimationFrame ( 2 )
//        } , offscreenDrawing.to + 2500 );
//
//    setTimeout ( function ( ) {
//            connectorAnimationFrame ( 3 )
//        } , offscreenDrawing.to + 4000 );
//
//    setTimeout ( function ( ) {
//            connectorAnimationFrame ( 4 )
//        } , offscreenDrawing.to + 4500 );
}


function connectorsFrame ( ) {

        var canvas = new OffscreenCanvas ( offscreenDrawing.boundingBox.width , offscreenDrawing.boundingBox.height ) ;
        ctx = canvas.getContext ( '2d' ) ;
        ctx.lineWidth = offscreenDrawing.style.pen ;
        ctx.strokeStyle = offscreenDrawing.style.color ;
        ctx.fillStyle = offscreenDrawing.style.color ;
        ctx.globalAlpha = offscreenDrawing.style.alpha ;
        ctx.font = "bolder 10pt Trebuchet MS,Tahoma,Verdana,Arial,sans-serif";
        ctx.textAlign = "center";
        ctx.textBaseline = 'middle';
        ctx.save ( ) ;

        connectors = function ( boundingBox , verticesConnectors ) {
            var connectors = [ ] ;
            for ( var i = 0 ; i < verticesConnectors.length ; i ++ ) {
                connectors.push ( {
                    head : canvasTransform.canvasPoint ( verticesConnectors[ i ].head.point , boundingBox )  ,
                    tail : canvasTransform.canvasPoint ( verticesConnectors[ i ].tail.point , boundingBox )
                } ) ;
            }
            return connectors;
        }

        drawConnectors = function ( ctx , connectors ) {
            ctx.restore ( ) ;
            ctx.beginPath ( ) ;
            xyCtx.stroke ( ctx , connectors ) ;
            ctx.closePath ( ) ;
        }


        for ( var i = 0 ; i < topology.data.length ; i ++ ) {
            var boundingBox = canvasTransform.boundingBox ( canvas ) ;
            var cnts = connectors ( boundingBox , topology.data [ i ].outerVerticesConnectors  ) ;
            drawConnectors ( ctx , cnts ) ;
            var center = canvasTransform.canvasPoint ( topology.data [ i ].center , boundingBox ) ;
            var headlen = 2.5 * offscreenDrawing.style.pen ;
            var cnts = connectors ( boundingBox , topology.data [ i ].connectors  ) ;
            for ( var j = 0 ; j < cnts.length ; j ++ ) {
                var angle =  Math.atan2 ( cnts [ j ].tail.y - cnts [ j ].head.y ,
                                          cnts [ j ].tail.x - cnts [ j ].head.x );
                var middle = canvasTransform.splitPoint ( cnts [ j ].head , cnts [ j ].tail , 1 , 2) ;
                ctx.beginPath();
                ctx.moveTo ( middle.x , middle.y ) ;
                ctx.lineTo( middle.x - headlen * Math.cos ( angle - Math.PI / 6 ) ,
                            middle.y - headlen * Math.sin ( angle - Math.PI / 6 ) ) ;
                ctx.moveTo ( middle.x , middle.y ) ;
                ctx.lineTo( middle.x - headlen * Math.cos ( angle + Math.PI / 6 ) ,
                            middle.y - headlen * Math.sin ( angle + Math.PI / 6 ) ) ;
                ctx.closePath ( ) ;
                ctx.stroke ( ) ;
            }

//            ctx.beginPath ( ) ;
//            ctx.fillText ( topology.data [ i ].index + '' ,
//                            center.x , center.y ) ;
//            ctx.fillText ( '[' + topology.data [ i ].row + ',' + topology.data [ i ].column + ']' ,
//                            center.x , center.y ) ;
//            ctx.closePath ( ) ;
//            ctx.fillStyle = colorAdj.complementary( offscreenDrawing.style.color ) ;
//            ctx.font = "bolder 12pt Trebuchet MS,Tahoma,Verdana,Arial,sans-serif";
//            for ( var j = 0 ; j < topology.data [ i ].vertices.length ; j ++ ) {
//                var point = canvasTransform.canvasPoint ( topology.data [ i ].vertices [ j ].point , boundingBox ) ;
//                ctx.beginPath ( ) ;
//                ctx.fillText ( '' + topology.data [ i ].vertices [ j ].index ,
//                                point.x - 15 , point.y - 15 ) ;
//                ctx.closePath ( ) ;
//            }
//            ctx.restore ( ) ;
//            ctx.fillStyle = offscreenDrawing.style.color ;
//            ctx.font = "bolder 14pt Trebuchet MS,Tahoma,Verdana,Arial,sans-serif";





        }
//                ctx.beginPath ( ) ;
//            for ( var j = 0 ; j < topology.data.length ; j ++ ) {
//            }
//                ctx.closePath ( ) ;


        messages.push ( {
            name : offscreenDrawing.name ,
            layer : 'CONNECTORS' ,
            bound : 1 ,
            frame : 0 ,
            imageData :  ctx.getImageData ( 0 , 0 , canvas.width , canvas.height )
        } );
}


function vertexCanvas ( ) {

        var vertexCanvas = new OffscreenCanvas ( offscreenDrawing.boundingBox.width , offscreenDrawing.boundingBox.height ) ;
        var ctx = vertexCanvas.getContext ( "2d" ) ;

        ctx = vertexCanvas.getContext ( '2d' ) ;
        ctx.lineWidth = offscreenDrawing.style.pen ;
        ctx.strokeStyle = colorAdj.complementary ( offscreenDrawing.style.color ) ;
        ctx.fillStyle = offscreenDrawing.style.color ;
        ctx.globalAlpha = offscreenDrawing.style.alpha ;
        ctx.font = "bolder 10pt Trebuchet MS,Tahoma,Verdana,Arial,sans-serif";
        ctx.textAlign = "center";
        ctx.textBaseline = 'middle';
        ctx.save ( ) ;

        vertices = function ( boundingBox , verticesTopology ) {
            var vertices = [ ] ;
            for ( var i = 0 ; i < verticesTopology.length ; i ++ ) {
                vertices.push ( canvasTransform.canvasPoint ( verticesTopology[ i ].point , boundingBox ) );
            }
            return vertices;
        }

        drawVertices = function ( ctx , vertices , pen  ) {
            xyCtx.strokeFill ( ctx , vertices , 1.2 * pen ) ;
        }

        ctx.restore ( ) ;
        ctx.globalCompositeOperation = "source-over" ;
        for ( var i = 0 ; i < topology.data.length ; i ++ ) {
            var vTopology = vertices ( canvasTransform.boundingBox ( vertexCanvas ) , topology.data [ i ].vertices )
            drawVertices ( ctx , vTopology , offscreenDrawing.style.pen ) ;
            var center = canvasTransform.canvasPoint (topology.data [ i ].center , canvasTransform.boundingBox ( vertexCanvas ) );
//            for ( var j = 0 ; j < topology.data [ i ].vertices.length ; j ++ ) {
//                ctx.beginPath ( ) ;
//                ctx.fillText ( topology.data [ i ].vertices [ j ].index , vTopology [ j ].x - 20, vTopology [ j ].y - 20 ) ;
//                ctx.closePath ( ) ;
//            }
        }

        messages.push ( {
            name : offscreenDrawing.name ,
            layer : 'VERTICES' ,
            bound : 1 ,
            frame : 0 ,
            imageData :  ctx.getImageData ( 0 , 0 , vertexCanvas.width , vertexCanvas.height )
        } );

}



function connectorAnimationFrame( frame ) {

        var canvas = new OffscreenCanvas ( offscreenDrawing.boundingBox.width , offscreenDrawing.boundingBox.height ) ;
        ctx = canvas.getContext ( '2d' ) ;
        ctx.lineWidth = offscreenDrawing.style.pen ;
        ctx.strokeStyle = colorAdj.complementary ( offscreenDrawing.style.color ) ;
        ctx.fillStyle = offscreenDrawing.style.color ;
        ctx.globalAlpha = offscreenDrawing.style.alpha ;
        ctx.save ( ) ;

        frameParts = function ( frame ) {
            if ( frame == 1 ) {
                return [ 0 , 1 , 4 , 5 , 8 , 9 ] ;
            } else if ( frame == 2 ) {
                return [ 1 , 2 , 5 , 6 , 9 , 10 ] ;
            } else if ( frame == 3 ) {
                return [ 2 , 3 , 6 , 7 , 10 , 11 ] ;
            } else if ( frame == 4 ) {
                return [ 3 , 4 , 7 , 8 , 11 , 12 ] ;
            }
            return [ 0 , 12 ] ;
        }

        connectors = function ( boundingBox , verticesConnectors , frameParts ) {
            var connectors = [ ] ;
            for ( var i = 0 ; i < verticesConnectors.length ; i ++ ) {
                var head = canvasTransform.canvasPoint ( verticesConnectors[ i ].head.point , boundingBox ) ;
                var tail = canvasTransform.canvasPoint ( verticesConnectors[ i ].tail.point , boundingBox ) ;
                for ( var j = 0 ; j < frameParts.length ; j = j + 2 ) {
                    connectors.push ( {
                        head : canvasTransform.splitPoint ( head , tail , frameParts[ j ] , 12 ) ,
                        tail : canvasTransform.splitPoint ( head , tail , frameParts[ j + 1 ] , 12 )
                    } ) ;
                }
            }
            return connectors;
        }

        drawConnectors = function ( ctx , connectors ) {
            ctx.restore ( ) ;
            ctx.beginPath ( ) ;
            xyCtx.stroke ( ctx , connectors ) ;
            ctx.closePath ( ) ;
            var headlen = 2.5 * offscreenDrawing.style.pen  ;
            for ( var i = 0 ; i < connectors.length ; i ++ ) {
                var angle =  Math.atan2 ( connectors [ i ].tail.y - connectors [ i ].head.y ,
                                          connectors [ i ].tail.x - connectors [ i ].head.x );
                ctx.beginPath();
                ctx.moveTo ( connectors [ i ].tail.x , connectors [ i ].tail.y ) ;
                ctx.lineTo( connectors [ i ].tail.x - headlen * Math.cos ( angle - Math.PI / 6 ) ,
                            connectors [ i ].tail.y - headlen * Math.sin ( angle - Math.PI / 6 ) ) ;
                ctx.moveTo ( connectors [ i ].tail.x , connectors [ i ].tail.y ) ;
                ctx.lineTo( connectors [ i ].tail.x - headlen * Math.cos ( angle + Math.PI / 6 ) ,
                            connectors [ i ].tail.y - headlen * Math.sin ( angle + Math.PI / 6 ) ) ;
                ctx.closePath ( ) ;
                ctx.stroke ( ) ;
            }
        }

        var frames =  frameParts ( frame ) ;

        for ( var i = 0 ; i < topology.data.length ; i ++ ) {
            drawConnectors ( ctx ,
                             connectors ( canvasTransform.boundingBox ( canvas ) ,
                                          topology.data [ i ].connectors ,
                                          frames )
                             ) ;
        }

        messages.push ( {
            name : offscreenDrawing.name ,
            layer : 'ANIMATION_FRAME' ,
            bound : 1 ,
            frame : frame ,
            totalFrames : 4 ,
            imageData :  ctx.getImageData ( 0 , 0 , canvas.width , canvas.height )
        } );

}
