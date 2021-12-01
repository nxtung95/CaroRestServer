package caroserver.model;

import caroserver.common.Constant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board {
	private Player player1;
	private Player player2;
	private List<Chess> chessList;

	public Board() {
		// Khởi tạo đối tượng người chơi
		player1 = new Player(Constant.QUAN_O);
		player2 = new Player(Constant.QUAN_X);
		// Khởi tạo bàn cờ với giá trị mặc định
		chessList = new ArrayList<>();
		initialBoard();
	}

	private void initialBoard() {
		// Khởi tạo bàn cờ với quân cờ mặc định theo cột, hàng
		for (int i = 0; i < Constant.SO_COT; i++) {
			for (int j = 0; j < Constant.SO_HANG; j++) {
				// Thêm quân cờ mặc định theo cột, hàng vào bàn cờ
				chessList.add(new Chess(i, j, Constant.DEFAULT));
			}
		}
	}
}
