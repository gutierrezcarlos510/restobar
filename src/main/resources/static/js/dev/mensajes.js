/*Area de configuracion de alertas*/
var Toast = Swal.mixin({
    toast: true,
    position: 'bottom-end',
    showConfirmButton: false,
    timer: 10000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})
const swalWithBootstrapButtons = Swal.mixin({
    cancelButtonColor: '#d33',
})
function mostrarMensaje(tipo, msg) {
    Toast.fire({
        icon:tipo,
        title: msg,
        position: UtilBrowser.isMobil()?'top-end':'bottom-end'
    })
}
function mostrarMensajeMobil(tipo, msg) {
    Toast.fire({
        icon:tipo,
        title: msg,
        position:'top-end'
    })
}
function modalAlert(msgtitle, msgText, icon) {
    swalWithBootstrapButtons.fire(
        msgtitle,
        msgText,
        icon
    )
}
function alertEliminacion(msgTitle, msgText, callBackSuccess){
    swalWithBootstrapButtons.fire({
        title: msgTitle,
        text: msgText,
        icon: 'error',
        showCancelButton: true,
        confirmButtonText: 'Si, aceptar',
        cancelButtonText: 'No, cancelar!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            callBackSuccess();

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelado',
                'Se ha cancelado la eliminacion del registro',
                'error'
            )
        }
    })
}
function alertActivacion(msgTitle, msgText, callBackSuccess){
    swalWithBootstrapButtons.fire({
        title: msgTitle,
        text: msgText,
        icon: 'success',
        showCancelButton: true,
        confirmButtonText: 'Si, aceptar',
        cancelButtonText: 'No, cancelar!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            callBackSuccess();

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelado',
                'Se ha cancelado la eliminacion del registro',
                'error'
            )
        }
    })
}
function alertEliminacionHtml(msgTitle, msgText, callBackSuccess){
    swalWithBootstrapButtons.fire({
        title: msgTitle,
        html: msgText,
        icon: 'error',
        showCancelButton: true,
        confirmButtonText: 'Si, aceptar',
        cancelButtonText: 'No, cancelar!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            callBackSuccess();

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelado',
                'Se ha cancelado la eliminacion del registro',
                'error'
            )
        }
    })
}
function alertAdicion(msgTitle, msgText, callBackSuccess){
    swalWithBootstrapButtons.fire({
        title: msgTitle,
        text: msgText,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Si, aceptar',
        cancelButtonText: 'No, cancelar!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            callBackSuccess();

        } else if (
            /* Read more about handling dismissals below */
            result.dismiss === Swal.DismissReason.cancel
        ) {
            swalWithBootstrapButtons.fire(
                'Cancelado',
                'Se ha cancelado la adicion del registro',
                'error'
            )
        }
    })
}
function alertCancelacion(msgTitle, msgText, callBackSuccess){
    swalWithBootstrapButtons.fire({
        title: msgTitle,
        text: msgText,
        icon: 'warning',
        animation: true,
        showCancelButton: true,
        confirmButtonText: 'Si, aceptar',
        cancelButtonText: 'No, cancelar!',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            callBackSuccess();
        }
    })
}
function finishTransaction (resp, callbackSuccess) {
    if(resp.status) {
        mostrarMensaje('success', resp.msg);
        callbackSuccess();
    } else {
        modalAlert('Transaccion fallida', resp.msg, 'error');
    }
}