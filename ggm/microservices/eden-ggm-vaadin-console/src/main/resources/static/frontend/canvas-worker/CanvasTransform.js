canvasWorker.canvasTransform = {

    scale : 1 ,

    x : 0 ,

    y : 0 ,

    boundingBox : function ( canvas ) {
        return {
            width : canvas.width ,
            height : canvas.height ,
        }
    } ,

    canvasPoint : function ( point , boundingBox ) {
                return { x : point.x * canvasWorker.canvasTransform.scale + canvasWorker.canvasTransform.x + boundingBox.width / 2 ,
                        y : point.y * canvasWorker.canvasTransform.scale + canvasWorker.canvasTransform.y + boundingBox.height / 2 }
    } ,

    windowPoint : function( point , boundingBox ) {
                return { x : (point.x - canvasWorker.canvasTransform.x - boundingBox.width / 2 ) ,
                        y : (point.y - canvasWorker.canvasTransform.y - boundingBox.height / 2 ) }
    } ,

}