package vn.iotstar.controllers.admin;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeAdminController {
	@GetMapping("/admin/home")
	public String index() {
		return "admin/home";
	}

}
