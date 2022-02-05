/*
 * jQuery SimpleCalculadora
 * @author dimti28@gmail.com - http://develoteca.com
 * @version 1.0
 * @date Julio 10, 2015
 * @category jQuery plugin
 * @copyright (c) 2015 dimti28@gmail.com (http://develoteca.com)
 * @license CC Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0) - http://creativecommons.org/licenses/by-nc-sa/3.0/
 */
 jQuery.fn.extend({Calculadora: function(op) {
					var LaCalculadora=this;
					var idInstancia=$(LaCalculadora).attr('id');
					var NombreBotonesClase=idInstancia+'tcl';
					var Clase;
    				var Botones;
					var Signos;
					
					defaults = {
						TituloHTML:'<div class="row"><div class="col-md-11 col-sm-9 col-xs-8"><a class="btn-block btn3d btn btn-success" data-dismiss="modal" href="#">Calculadora</a></div><div class="col-md-1 col-sm-3 col-xs-4"><a class="btn-block btn3d btn btn-danger" data-dismiss="modal" href="#"><i class="fa fa-times"></i></a></div></div>',
						Botones:["7","8","9","+","4","5","6","-","3","2","1","*","0",".","=","/"],
						Signos:["+", "-", "*", "/"],
						ClaseBtns1: 'primary',
						ClaseBtns2: 'success',
						ClaseBtns3: 'warning',
						ClaseColumnas:'col-md-3 col-xs-3 mbottom',
						ClaseBotones:'btn3d btn-lg btn-block btn btn-',
						txtSalida:idInstancia+'txtResultado',
						ClasetxtSalida:'form-control txtr',
						InputBorrar:idInstancia+'Borrar',
						ClaseInputBorrar:'btn3d btn btn-danger btn-lg btn-block',
						EtiquetaBorrar:'Borrar',
						InputCopiar:idInstancia+'Copiar',
						ClaseInputCopiar:'btn3d btn btn-info btn-lg btn-block',
						EtiquetaCopiar:'Copiar'
					}
					
                    var op = $.extend({}, defaults, op);
					Botones=op.Botones;
					Signos=op.Signos;
                    $(LaCalculadora).append('<input type="text" class="'+op.ClasetxtSalida+'" id="'+op.txtSalida+'" value="0" >');
					$(LaCalculadora).append('<div class="row" id="'+idInstancia+'btns"></div>');					$.each(Botones, function(index,value) {	
						Clase=op.ClaseBtns1			
						if(Signos.indexOf(value)>-1){Clase=op.ClaseBtns2;}
						if(value=='='){Clase=op.ClaseBtns3;}
						$('#'+idInstancia+'btns').append('<div class="'+op.ClaseColumnas+'"><input type="button" class="'+NombreBotonesClase+' '+op.ClaseBotones+Clase+'" value="'+value+'"/></div>');
					});
					$(LaCalculadora).append('<div class="row"><div class="col-md-6 col-sm-12"><input type="button" id="'+op.InputCopiar+'" class="'+op.ClaseInputCopiar+'" value="'+op.EtiquetaCopiar+'"></div><div class="col-md-6 col-sm-12"><input type="button" id="'+op.InputBorrar+'" class="'+op.ClaseInputBorrar+'" value="'+op.EtiquetaBorrar+'"></div></div>');
					$(LaCalculadora).html('<div class="panel panel-primary btn-block calculadoraBase  mtop">'+op.TituloHTML+'<div class="panel-body"><div class="col-md-12" style="margin-bottom: 10px;">'+$(LaCalculadora).html()+'</div></div> </div>');
					
					$('.'+NombreBotonesClase).click(function(){
						var vTecla=$(this).val();
						var salida=$('#'+op.txtSalida);
						if(vTecla=='='){salida.val(eval(salida.val()));}
						else{if((salida.val()==0)){
							if(Signos.indexOf(vTecla)>-1){salida.val(0)}
							else{salida.val(vTecla);}
						}else{salida.val(salida.val()+vTecla);} 
						}
					});
					$("#"+op.InputBorrar).click(function(){$('#'+op.txtSalida).val("0");});
		 			$("#"+op.InputCopiar).click(function(){
		 				//this.copyToClipboard($('#'+op.txtSalida).val());
		 				var elem =document.getElementById("idCalculadoratxtResultado");
						var targetId = "_hiddenCopyText_";
						var isInput = elem.tagName === "INPUT" || elem.tagName === "TEXTAREA";
						var origSelectionStart, origSelectionEnd;
						if (isInput) {
							// can just use the original source element for the selection and copy
							target = elem;
							origSelectionStart = elem.selectionStart;
							origSelectionEnd = elem.selectionEnd;
						} else {
							// must use a temporary form element for the selection and copy
							target = document.getElementById(targetId);
							if (!target) {
								var target = document.createElement("textarea");
								target.style.position = "absolute";
								target.style.left = "-9999px";
								target.style.top = "0";
								target.id = targetId;
								document.body.appendChild(target);
							}
							target.textContent = elem.textContent;
						}
						// select the content
						var currentFocus = document.activeElement;
						target.focus();
						target.setSelectionRange(0, target.value.length);

						// copy the selection
						var succeed;
						try {
							succeed = document.execCommand("copy");
						} catch(e) {
							succeed = false;
						}
						// restore original focus
						if (currentFocus && typeof currentFocus.focus === "function") {
							currentFocus.focus();
						}

						if (isInput) {
							// restore prior selection
							elem.setSelectionRange(origSelectionStart, origSelectionEnd);
						} else {
							// clear temporary content
							target.textContent = "";
						}
						return succeed;
					});
	}
});