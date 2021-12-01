package caroserver.controller;

import caroserver.request.DanhCoRequest;
import caroserver.response.DanhCoResponse;
import caroserver.response.RestartGameResponse;
import caroserver.service.BoardService;
import com.google.gson.Gson;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
	@Autowired
	private BoardService boardService;

	@RequestMapping(value = "/danhCo", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public DanhCoResponse danhCo(@RequestBody DanhCoRequest danhCoRequest) {
		System.out.println("DanhCoRequest from client: " + new Gson().toJson(danhCoRequest));
		DanhCoResponse danhCoResponse = new DanhCoResponse();
		try {
			danhCoResponse = boardService.danhCo(danhCoRequest.getX(), danhCoRequest.getY(), danhCoRequest.getType());
		} catch (Exception e) {
			e.printStackTrace();
			danhCoResponse.setCode("500");
			danhCoResponse.setDesc("Có lỗi xảy ra...");
		}
		System.out.println("DanhCoResponse to client: " + new Gson().toJson(danhCoResponse));
		return danhCoResponse;
	}

	@RequestMapping(value = "/restart", method = RequestMethod.POST)
	public RestartGameResponse restartGame() {
		RestartGameResponse restartGameResponse = new RestartGameResponse();
	}
}
