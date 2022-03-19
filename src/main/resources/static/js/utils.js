function exportTableToExcel(tableID, filename = ''){
    let downloadLink;
    let dataType = 'application/vnd.ms-excel';
    let tableSelect = document.getElementById(tableID);
    let tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');

    // Specify file name
    filename = filename?filename+'_'+moment().format('DDMMYYYYHHmm')+'.xls':'excel_data.xls';

    // Create download link element
    downloadLink = document.createElement("a");

    document.body.appendChild(downloadLink);

    if(navigator.msSaveOrOpenBlob){
        var blob = new Blob(['ufeff', tableHTML], {
            type: dataType
        });
        navigator.msSaveOrOpenBlob( blob, filename);
    }else{
        // Create a link to the file
        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;

        // Setting the file name
        downloadLink.download = filename;

        //triggering the function
        downloadLink.click();
    }
}
function reportXLS (xurl, xname) {
    fetch(xurl)
        .then(resp => resp.blob())
        .then(blob => {
            cerrarLoad();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = xname+'_'+moment().format('DDMMYYYYHHmm')+'.xls';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(() => {
            console.log('Error al generar reporte excel');
            cerrarLoad();
        });
}
function roundMoney(valueMoney){
	let valueRound2 = valueMoney.toFixed(3);
	let valueDecimal2 = parseInt(valueRound2.charAt(valueRound2.length-1));
	if(valueDecimal2 > 0){
		let resto = 10 - valueDecimal2;
		let resultado = parseFloat(valueRound2) + parseFloat('0.0'+resto);
		return resultado;
	}else{
		return parseFloat(valueRound2);
	}
}
function imageExists(image_url) {
    var http = new XMLHttpRequest();
    http.open('HEAD', image_url, false);
    http.send();
    return http.status != 404;
}
var UtilBrowser = {
    isMobil : function(){
        return (/android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(navigator.userAgent.toLowerCase()));
    }
}
var UtilDate = {
    formatDateLiteral: function(valueFecha){
        if(valueFecha) {
            let fecha = moment(valueFecha).format('LLL');
            return fecha.substring(0,fecha.length-6);
        } else {
            return '';
        }
    },
    formatTimestampLiteral: function(valueFecha){
        if(valueFecha) {
            let fecha = moment(valueFecha).format('LLLL');
            return fecha;
        } else {
            return '';
        }
    },
    formatDate:function(valueFecha, format){
        if(valueFecha && format){
            return moment(valueFecha).format(format);
        }else{
            return '';
        }
    },
    inputFormatData: function(id,data){
        $("#"+id).on("change", function() {
            this.setAttribute(
                "data-date",
                moment(this.value, "YYYY-MM-DD")
                    .format( this.getAttribute(data ?? "data-date-format") )
            )
        }).trigger("change")
    },
    inputFormatData: function(fromId,formato,toId){
        $("#"+fromId).on("change", function() {
            $('#'+toId).val(moment(this.value, "YYYY-MM-DD").format( formato ?? 'DD/MM/YYYY'));
        }).trigger("change")
    },
    inputTime: function(fromId,formato,toId){
        $("#"+fromId).on("change", function() {
            $('#'+toId).val(moment(this.value, "HH:mm:ss").format( formato ?? 'HH:mm:ss'));
        }).trigger("change")
    }
};
var UtilForm = {
		getFormData: function(xform){
			let unindexedArray = xform.serializeArray();
			let indexedArray = {};
			$.map(unindexedArray, function(n,i){
				indexedArray[n['name']] = n['value'];
			})
			return indexedArray;
		},
		updateField:function(form,field,status){
			$('#'+form).data('formValidation').enableFieldValidators(field,status).revalidateField(field);
		}
}
var UtilString = {
    isNull: function(cad,alternative){
        if(cad) {
            return cad;
        }else{
            if(alternative){
                return alternative;
            }else{
                return '';
            }
        }
    }
}
var UtilNumeros = {
    formatNumber: function(num) {
        if(!num){
            num = 0;
        }
        return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')
    },
    roundMoney: function(valueMoney){
    let valueRound2 = valueMoney.toFixed(3);
    let valueDecimal2 = parseInt(valueRound2.charAt(valueRound2.length-1));
    if(valueDecimal2 > 0){
        let resto = 10 - valueDecimal2;
        let resultado = parseFloat(valueRound2) + parseFloat('0.0'+resto);
        return resultado;
    }else{
        return parseFloat(valueRound2);
    }
}
}

function estaVacio(obj) {
    for (var key in obj) {
        if (obj.hasOwnProperty(key))
            return false;
    }
    return true;
}
var numeroALetras = (function() {

    // Código basado en https://gist.github.com/alfchee/e563340276f89b22042a
    function Unidades(num) {

        switch (num) {
            case 1:
                return 'UN';
            case 2:
                return 'DOS';
            case 3:
                return 'TRES';
            case 4:
                return 'CUATRO';
            case 5:
                return 'CINCO';
            case 6:
                return 'SEIS';
            case 7:
                return 'SIETE';
            case 8:
                return 'OCHO';
            case 9:
                return 'NUEVE';
        }

        return '';
    } //Unidades()

    function Decenas(num) {

        let decena = Math.floor(num / 10);
        let unidad = num - (decena * 10);

        switch (decena) {
            case 1:
                switch (unidad) {
                    case 0:
                        return 'DIEZ';
                    case 1:
                        return 'ONCE';
                    case 2:
                        return 'DOCE';
                    case 3:
                        return 'TRECE';
                    case 4:
                        return 'CATORCE';
                    case 5:
                        return 'QUINCE';
                    default:
                        return 'DIECI' + Unidades(unidad);
                }
            case 2:
                switch (unidad) {
                    case 0:
                        return 'VEINTE';
                    default:
                        return 'VEINTI' + Unidades(unidad);
                }
            case 3:
                return DecenasY('TREINTA', unidad);
            case 4:
                return DecenasY('CUARENTA', unidad);
            case 5:
                return DecenasY('CINCUENTA', unidad);
            case 6:
                return DecenasY('SESENTA', unidad);
            case 7:
                return DecenasY('SETENTA', unidad);
            case 8:
                return DecenasY('OCHENTA', unidad);
            case 9:
                return DecenasY('NOVENTA', unidad);
            case 0:
                return Unidades(unidad);
        }
    } //Unidades()

    function DecenasY(strSin, numUnidades) {
        if (numUnidades > 0)
            return strSin + ' Y ' + Unidades(numUnidades)

        return strSin;
    } //DecenasY()

    function Centenas(num) {
        let centenas = Math.floor(num / 100);
        let decenas = num - (centenas * 100);

        switch (centenas) {
            case 1:
                if (decenas > 0)
                    return 'CIENTO ' + Decenas(decenas);
                return 'CIEN';
            case 2:
                return 'DOSCIENTOS ' + Decenas(decenas);
            case 3:
                return 'TRESCIENTOS ' + Decenas(decenas);
            case 4:
                return 'CUATROCIENTOS ' + Decenas(decenas);
            case 5:
                return 'QUINIENTOS ' + Decenas(decenas);
            case 6:
                return 'SEISCIENTOS ' + Decenas(decenas);
            case 7:
                return 'SETECIENTOS ' + Decenas(decenas);
            case 8:
                return 'OCHOCIENTOS ' + Decenas(decenas);
            case 9:
                return 'NOVECIENTOS ' + Decenas(decenas);
        }

        return Decenas(decenas);
    } //Centenas()

    function Seccion(num, divisor, strSingular, strPlural) {
        let cientos = Math.floor(num / divisor)
        let resto = num - (cientos * divisor)

        let letras = '';

        if (cientos > 0)
            if (cientos > 1)
                letras = Centenas(cientos) + ' ' + strPlural;
            else
                letras = strSingular;

        if (resto > 0)
            letras += '';

        return letras;
    } //Seccion()

    function Miles(num) {
        let divisor = 1000;
        let cientos = Math.floor(num / divisor)
        let resto = num - (cientos * divisor)

        let strMiles = Seccion(num, divisor, 'UN MIL', 'MIL');
        let strCentenas = Centenas(resto);

        if (strMiles == '')
            return strCentenas;

        return strMiles + ' ' + strCentenas;
    } //Miles()

    function Millones(num) {
        let divisor = 1000000;
        let cientos = Math.floor(num / divisor)
        let resto = num - (cientos * divisor)

        let strMillones = Seccion(num, divisor, 'UN MILLON DE', 'MILLONES DE');
        let strMiles = Miles(resto);

        if (strMillones == '')
            return strMiles;

        return strMillones + ' ' + strMiles;
    } //Millones()

    return function NumeroALetras(num, currency) {
        currency = currency || {};
        let data = {
            numero: num,
            enteros: Math.floor(num),
            centavos: (((Math.round(num * 100)) - (Math.floor(num) * 100))),
            letrasCentavos: '',
            letrasMonedaPlural: currency.plural || 'BOLIVIANOS', //'PESOS', 'Dólares', 'Bolívares', 'etcs'
            letrasMonedaSingular: currency.singular || 'BOLIVIANO', //'PESO', 'Dólar', 'Bolivar', 'etc'
            letrasMonedaCentavoPlural: currency.centPlural || 'CENTAVOS',
            letrasMonedaCentavoSingular: currency.centSingular || 'CENTAVO'
        };

        if (data.centavos > 0) {
            data.letrasCentavos = 'CON ' + (function() {
                if (data.centavos == 1)
                    return Millones(data.centavos) + ' ' + data.letrasMonedaCentavoSingular;
                else
                    return Millones(data.centavos) + ' ' + data.letrasMonedaCentavoPlural;
            })();
        };

        if (data.enteros == 0)
            return 'CERO ' + data.letrasMonedaPlural + ' ' + data.letrasCentavos;
        if (data.enteros == 1)
            return Millones(data.enteros) + ' ' + data.letrasMonedaSingular + ' ' + data.letrasCentavos;
        else
            return Millones(data.enteros) + ' ' + data.letrasMonedaPlural + ' ' + data.letrasCentavos;
    };

})();