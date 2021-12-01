package caroserver.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DanhCoRequest {
	private int x;
	private int y;
	private String type;
}
