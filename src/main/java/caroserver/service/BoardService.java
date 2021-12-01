package caroserver.service;

import caroserver.common.Constant;
import caroserver.model.Board;
import caroserver.model.Chess;
import caroserver.model.Player;
import caroserver.response.DanhCoResponse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
	private Board board;

	@PostConstruct
	public void init() {
		board = new Board();
	}

	public DanhCoResponse danhCo(int x, int y, String typeChess) {
		DanhCoResponse danhCoResponse = new DanhCoResponse();
		danhCoResponse.setCode("200");
		danhCoResponse.setDesc("Đánh cờ...");
		boolean result;
		boolean isValid = isValidCoordinate(x, y);
		if (!isValid) {
			danhCoResponse.setCode("403");
			danhCoResponse.setDesc("Nước cờ không hợp lệ");
			return danhCoResponse;
		}
		addChess(x, y, typeChess);
		result = checkWin(x, y, typeChess);
		if (result) {
			danhCoResponse.setCode("201");
			danhCoResponse.setDesc("Quân " + typeChess + " thắng");
		}
		return danhCoResponse;
	}

	private boolean checkWin(int x, int y, String typeChess) {
		// Tính toán vị trí hàng và cột tương ứng với tọa độ trên bàn cờ
		int posRow = y / Constant.SIZE_O_CO;
		int posCol = x / Constant.SIZE_O_CO;

		/**
		 * Kiểm tra thắng cuộc theo đường chéo, hàng ngang, hàng dọc Nếu một trong các
		 * trường hợp thỏa mãn thì trả về true
		 */
		// Kiểm tra thắng cuộc theo đường chéo
		if (checkWinByDiagonal(posCol, posRow, typeChess)) {
			return true;
			// Kiểm tra thằng cuộc theo hàng ngang
		} else if (checkWinByHorizontal(posCol, posRow, typeChess)) {
			return true;
			// Kiểm tra thắng cuộc theo hàng dọc
		} else if (checkWinByVertical(posCol, posRow, typeChess)) {
			return true;
		}
		// Nếu không thỏa mãn trường hợp thắng nào thì trả về false
		return false;
	}

	/**
	 * Kiểm tra thắng cuộc theo đường chéo
	 *
	 * @param col
	 *            vị trí cột của quân cờ trên bàn cờ
	 * @param row
	 *            vị trí hàng của quân cờ trên bàn cờ
	 * @param loaiQuan
	 *            loại quân cờ của người chơi
	 * @return giá trị true nếu thỏa mãn chiến thắng theo đường chéo và ngược lại
	 *         thì trả về false
	 */
	private boolean checkWinByDiagonal(int col, int row, String loaiQuan) {
		// Khai báo biến đếm số quân cờ theo chéo phải
		int count = 0;
		// Khai báo biến chứa giá trị vị trí hàng trừ đi 4
		int j = row - 4;
		// Đếm số quân cờ của đường chéo phải
		for (int i = col - 4; i <= col + 4; i++) {
			// Kiểm tra điều kiện các vị trí cột nằm trong bàn cờ
			if (i >= 0 && i < Constant.SO_COT) {
				// Kiểm tra điều kiện các vị trí hàng nằm trong bàn cờ
				if (j >= 0 && j < Constant.SO_HANG) {
					// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
					// tương ứng
					int index = i + j * Constant.SO_HANG;
					// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
					if (loaiQuan.equals(board.getChessList().get(index).getTypeChess())) {
						count++;
					} else {
						// Nếu gặp quân cờ không trùng thì gán lại count = 0
						count = 0;
					}
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
			// Tăng vị trí hàng lên 1
			j++;
		}

		// Gán lại giá trị biến đếm số quân cờ theo chéo trái
		count = 0;

		// Gán giá trị biến chứa giá trị vị trí hàng trừ đi 4
		j = row - 4;
		// Đếm số quân cờ của đường chéo trái
		for (int i = col + 4; i >= 0; i--) {
			// Kiểm tra điều kiện vị trí cột nằm trong bàn cờ
			if (i < Constant.SO_COT) {
				// Kiểm tra điều kiện các vị trí hàng nằm trong bàn cờ
				if (j >= 0 && j < Constant.SO_HANG) {
					// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
					// tương ứng
					int index = i + j * Constant.SO_HANG;
					// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
					if (loaiQuan.equals(board.getChessList().get(index).getTypeChess())) {
						count++;
					} else {
						// Nếu gặp quân cờ không trùng thì gán lại count = 0
						count = 0;
					}
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
			// Tăng vị trí hàng lên 1
			j++;
		}

		// Nếu không thỏa mãn 2 trường hợp trên thì return false;
		return false;

	}

	/**
	 * Kiểm tra chiến thắng theo hàng dọc
	 *
	 * @param col
	 *            vị trí cột của quân cờ trên bàn cờ
	 * @param row
	 *            vị trí hàng của quân cờ trên bàn cờ
	 * @param loaiQuan
	 *            loại quân cờ của người chơi
	 * @return giá trị true nếu thỏa mãn chiến thắng theo hàng ngang và ngược lại
	 */
	private boolean checkWinByVertical(int col, int row, String loaiQuan) {
		// Khai báo biến đếm số quân cờ theo hàng dọc
		int count = 0;

		// Đếm số quân cờ theo hàng dọc
		for (int i = row - 4; i <= row + 4; i++) {
			// Kiểm tra điều kiện các vị trí hàng nằm trong bàn cờ
			if (i >= 0 && i < Constant.SO_HANG) {
				// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
				// tương ứng
				int index = col + i * Constant.SO_HANG;
				// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
				if (loaiQuan.equals(board.getChessList().get(index).getTypeChess())) {
					count++;
				} else {
					// Nếu gặp quân cờ không trùng thì gán lại count = 0
					count = 0;
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
		}
		// Nếu không thắng trả về false
		return false;
	}

	/**
	 * Kiểm tra thắng cuộc theo hàng ngang
	 *
	 * @param col
	 *            vị trí cột của quân cờ trên bàn cờ
	 * @param row
	 *            vị trí hàng của quân cờ trên bàn cờ
	 * @param loaiQuan
	 *            loại quân cờ của người chơi
	 * @return giá trị true nếu thỏa mãn chiến thắng theo hàng ngang và ngược lại
	 */
	private boolean checkWinByHorizontal(int col, int row, String loaiQuan) {
		// Khai báo biến đếm số quân cờ theo hàng ngang
		int count = 0;

		// Đếm số quân cờ theo hàng ngang
		for (int i = col - 4; i <= col + 4; i++) {
			// Kiểm tra điều kiện các vị trí cột nằm trong bàn cờ
			if (i >= 0 && i < Constant.SO_COT) {
				// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
				// tương ứng
				int index = i + row * Constant.SO_HANG;
				// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
				if (loaiQuan.equals(board.getChessList().get(index).getTypeChess())) {
					count++;
				} else {
					// Nếu gặp quân cờ không trùng thì gán lại count = 0
					count = 0;
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
		}
		// Nếu không thắng trả về false
		return false;
	}

	private void addChess(int x, int y, String typeChess) {
		if (typeChess.equals(Constant.QUAN_X)) {
			Player player1 = board.getPlayer1();
			player1.getListChesses().add(new Chess(x, y, typeChess));
		} else if (typeChess.equals(Constant.QUAN_O)) {
			Player player2 = board.getPlayer1();
			player2.getListChesses().add(new Chess(x, y, typeChess));
		}
		// Tính giá trị vị trí cột, hàng trên bàn cờ tương ứng với tọa độ x,y
		int row = y / Constant.SIZE_O_CO;
		int col = x / Constant.SIZE_O_CO;

		// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
		// tương ứng
		int index = col + row * Constant.SO_HANG;
		// Kiểm tra điều kiện vị trí cột, vị trí hàng nằm trong bàn cờ
		if (col >= 0 && col < Constant.SO_HANG) {
			if (row >= 0 && row < Constant.SO_COT) {
				// Cập nhật lại kiểu quân cờ tương ứng với vị trí tọa độ x,y người chơi đánh
				board.getChessList().get(index).setTypeChess(typeChess);
			}
		}
	}

	private boolean isValidCoordinate(int x, int y) {
		// Kiểm tra tọa độ quân cờ, nếu không hợp lệ thì trả về false
		if (!checkCoordChess(x, y)) {
			return false;
		}

		// Kiểm tra vị trí quân cờ có bị trùng không?, nếu không hợp lệ trả về false
		if (kiemTraCoTrung(x, y)) {
			return false;
		}

		return true;
	}

	/**
	 * Kiếm tra tọa độ quân cờ đã có trên bàn cờ chưa
	 *
	 */
	private boolean kiemTraCoTrung(int x, int y) {
		// Tìm kiếm tọa độ quân cờ trong list quân cờ người chơi
		Player player1 = board.getPlayer1();
		if (player1 != null) {
			List<Chess> chessList = player1.getListChesses();
			Optional<Chess> optChess = chessList.stream().filter(c -> c.getX() == x && c.getY() == y).findFirst();
			return optChess.isPresent();
		}
		Player player2 = board.getPlayer2();
		if (player2 != null) {
			List<Chess> chessList = player1.getListChesses();
			Optional<Chess> optChess = chessList.stream().filter(c -> c.getX() == x && c.getY() == y).findFirst();
			return optChess.isPresent();
		}
		// Nếu không tìm thấy cờ trùng thì trả về false
		return false;
	}

	/**
	 * Kiểm tra tọa độ quân cờ có đánh đúng trong bàn cờ không?
	 *
	 */
	private boolean checkCoordChess(int x, int y) {
		// Nếu tọa độ âm thì tọa độ không hợp lệ, trả về false
		if (x < 0 || y < 0) {
			return false;
		}
		// Tính chiều rộng, chiều dài của bàn cờ
		int widBoard = Constant.SO_HANG * Constant.SIZE_O_CO;
		int heiBoard = Constant.SO_COT * Constant.SIZE_O_CO;

		// Nếu tọa độ x,y nằm trong bàn cờ thì tọa độ đó hợp lệ. Trả về true
		if (x >= 0 && x <= widBoard) {
			if (y >= 0 && y <= heiBoard) {
				return true;
			}
		}
		// Nếu không thỏa mãn thì trả về false
		return false;
	}
}
