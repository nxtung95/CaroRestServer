package caroserver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player {
	// Khai báo list quân cờ và kiểu quân cờ của người chơi
	private List<Chess> listChesses;

	private String typeChess;

	public Player() {

	}

	public Player(String typeChess) {
		this.typeChess = typeChess;
		listChesses = new ArrayList<>();
	}
}
