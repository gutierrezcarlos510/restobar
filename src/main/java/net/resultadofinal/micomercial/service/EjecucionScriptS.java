package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.util.DataResponse;

public interface EjecucionScriptS {

	DataResponse script1(int num[]);

	DataResponse script2();
	DataResponse script3(int num[],Integer sucursalId);

}