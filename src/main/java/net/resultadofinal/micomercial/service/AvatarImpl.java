package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Avatar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AvatarImpl implements AvatarS {
	@Override
	public List<Avatar> listar(){
		List<Avatar> lista = new ArrayList<Avatar>();
		lista.add(new Avatar(100, "Avatar X", "avatarx.jpg"));
		lista.add(new Avatar(0, "Avatar 0", "avatar00.jpg"));
		lista.add(new Avatar(1, "Avatar 1", "avatar01.svg"));
		lista.add(new Avatar(2, "Avatar 2", "avatar02.svg"));
		lista.add(new Avatar(3, "Avatar 3", "avatar03.svg"));
		lista.add(new Avatar(4, "Avatar 4", "avatar04.png"));
		lista.add(new Avatar(5, "Avatar 5", "avatar05.jpg"));
		lista.add(new Avatar(6, "Avatar 6", "avatar06.png"));
		lista.add(new Avatar(7, "Avatar 7", "avatar07.svg"));
		lista.add(new Avatar(8, "Avatar 8", "avatar08.svg"));
		lista.add(new Avatar(9, "Avatar 9", "avatar09.svg"));
		lista.add(new Avatar(10, "Avatar 10", "avatar10.svg"));
		lista.add(new Avatar(11, "Avatar 11", "avatar11.svg"));
		lista.add(new Avatar(12, "Avatar 12", "avatar12.svg"));
		lista.add(new Avatar(13, "Avatar 13", "avatar13.svg"));
		lista.add(new Avatar(14, "Avatar 14", "avatar14.svg"));
		lista.add(new Avatar(15, "Avatar 15", "avatar15.png"));
		lista.add(new Avatar(16, "Avatar 16", "avatar16.png"));
		lista.add(new Avatar(17, "Avatar 17", "avatar17.png"));
		lista.add(new Avatar(18, "Avatar 18", "avatar18.png"));
		lista.add(new Avatar(19, "Avatar 19", "avatar19.jpg"));
		lista.add(new Avatar(20, "Avatar 20", "avatar20.jpg"));
		lista.add(new Avatar(21, "Avatar 21", "avatar21.png"));
		lista.add(new Avatar(22, "Avatar 22", "avatar22.jpg"));
		lista.add(new Avatar(23, "Avatar 23", "avatar23.png"));
		lista.add(new Avatar(24, "Avatar 24", "avatar24.jpg"));
		lista.add(new Avatar(25, "Avatar 25", "avatar25.png"));
		lista.add(new Avatar(26, "Avatar 26", "man.jpg"));
		lista.add(new Avatar(27, "Avatar 27", "user.png"));
		lista.add(new Avatar(28, "Avatar 28", "woman.jpg"));
		lista.add(new Avatar(29, "Avatar 29", "avatar25.png"));
		return lista;
	}
	@Override
	public String getValuePorSexo(boolean sexo) {
		return sexo ? "man.jpg" : "woman.jpg";
	}
}
