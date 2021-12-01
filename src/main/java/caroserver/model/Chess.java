package caroserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
public class Chess {
	// Khai báo thuộc tính tọa độ quân cờ
	private int x;
	private int y;

	// Khai báo loại quân cờ
	private String typeChess;
}
