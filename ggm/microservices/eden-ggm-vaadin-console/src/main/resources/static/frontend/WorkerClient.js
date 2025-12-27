self.onmessage = function ( event ) {

}

function postLayerRequest ( offscreenDrawing ) {

}


function getDrawingRequestPathVariable ( offscreenDrawing ) {
    return offscreenDrawing.style.pen.toFixed(2) +
            '/' + offscreenDrawing.style.color.replace ( '#' , '' ) +
            '/' + offscreenDrawing.style.alpha.toFixed(2) + '/' + offscreenDrawing.transform.x.toFixed(2) +
            '/' +  offscreenDrawing.transform.y.toFixed(2) + '/' + offscreenDrawing.boundingBox.width +
            '/' + offscreenDrawing.boundingBox.height + '/' + ( offscreenDrawing.transform.scale / 150000 ).toFixed(7) ;
}


