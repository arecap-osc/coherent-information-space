var gridLayers = {

    showGridLetters : function ( gridId ) {
        gridLayers.showHideGridLetters ( gridId, true ) ;
    } ,

    hideGridLetters : function ( gridId ) {
        gridLayers.showHideGridLetters ( gridId, false ) ;
    } ,

    showHideGridLetters : function ( gridId, show ) {
        canvasWorker.plotter.worker.postMessage (
            name : 'letters' ,
            gridId : gridId ,
            show : show
        )
    }

} ;