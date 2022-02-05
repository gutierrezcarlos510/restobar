package net.resultadofinal.micomercial.rest;

import net.resultadofinal.micomercial.service.ServicioS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/servicio")
public class ServicioRest {

	@Autowired
	private ServicioS servicioS;
//	@Autowired
//	private CotizacionS cotizacionS;
//	@Autowired
//	private EstudianteS estudianteS;
	
//	@GetMapping("/listAll")
//	public DataResponse listAll() {
//		return new DataResponse(true, servicioS.listar(-1, true, "", 0),"Se realizo con exitio");
//	}
//	//String celular,String  ciudad,String  tipoEstudio,Integer codTipSer,String  materia,String  tema,String  docente,String  pfecha,String  phora
//	@PostMapping("/guardarCotizacion")
//	public DataResponse guardarCotizacion(@RequestBody Cotizacion obj) {
//		try {
//			obj.setGestion(2021);
//			obj.setCodSuc(1);
//			obj.setCreatedBy(1L);
//			Estudiante estudiante = estudianteS.buscarPorCelular(obj.getCelular());
//			if(estudiante != null) {
//				obj.setCelularPropietario(estudiante.getXestudiante());
//			}else {
//				obj.setCelularPropietario("Nuevo estudiante");
//			}
//			Long cotizacionId = cotizacionS.adicionar(obj, obj.getPfecha()+" "+obj.getPhora());
//			if(cotizacionId > 0) {
//				Cotizacion cotizacion = cotizacionS.obtener(cotizacionId);
//				return new DataResponse(true, cotizacion, "Se realizo con exito el registro");
//			}else {
//				return new DataResponse(true,"No se logro realizar el registro de la cotizacion, intentelo mas tarde por favor.");
//			}
//		} catch (Exception e) {
//			return new DataResponse(false, e.getMessage(), "No se logro realizar el registro.");
//		}
//	}
//	@PostMapping("/demo")
//	public DataResponse demo(@RequestBody Cotizacion obj) {
//		try {
//			System.out.println(obj.getCelular());
//			return new DataResponse(true, "Mesaje demo");
//		} catch (Exception e) {
//			System.out.println("error :"+e.toString());
//			return new DataResponse(false, e.getMessage());
//		}
//	}
//	@PostMapping(value="buscarPorCelular",consumes = "application/json",produces = "application/json")
//	public @ResponseBody
//    DataResponse buscarPorCelular(HttpServletRequest request, @RequestBody ServiceApiWrap obj){
//		try {
//			System.out.println(obj.getCelular());
//			return new DataResponse(true, estudianteS.buscarPorCelular(obj.getCelular()), "Se realizo con exito la obtencion del estudiante");
//		} catch (Exception e) {
//			return new DataResponse(false, "Error al obtener estudiante");
//		}
//	}
}
